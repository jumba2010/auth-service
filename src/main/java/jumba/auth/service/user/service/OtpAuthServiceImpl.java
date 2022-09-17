package jumba.auth.service.user.service;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jumba.auth.service.generic.entity.LifeCyCleState;
import jumba.auth.service.user.domain.User;
import jumba.auth.service.user.domain.UserVerification;
import jumba.auth.service.user.domain.VerificationStatus;
import jumba.auth.service.user.dto.OtpAuthRequest;
import jumba.auth.service.user.repository.UserRepository;
import jumba.auth.service.user.repository.VerificationRepository;
import liquibase.repackaged.org.apache.commons.lang3.RandomStringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import static com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL;
import static jumba.auth.service.user.domain.VerificationSMSStatus.SMS_FAILED;
import static jumba.auth.service.user.domain.VerificationSMSStatus.SMS_SENT;

@Service
@Slf4j
@RequiredArgsConstructor
public class OtpAuthServiceImpl implements OtpAuthService {

    private final VerificationRepository verificationRepository;
    private final UserRepository userRepository;
    private final SmsService smsService;
    private final SecureRandom secureRandom;

    private final KeycloakService keycloakService;

    private final PasswordEncoder passwordEncoder;
    @Value("${verification.code.length}")
    private int codeLength;

    @Value("${verification.code.validity}")
    private int codeValidityInMinutes;

    @Value("${sms.sender}")
    private String msSender;

    @Override
    public void verifyUserPhoneNumber(String phoneNumber, String appName) {
        final String validPhoneNumber = validatePhoneNumber(phoneNumber);
        Boolean verificationExists = verificationRepository.existsUserVerificationByPhoneNumberEqualsAndVerificationSMSStatusEqualsAndExpiryGreaterThan(validPhoneNumber, SMS_SENT, Instant.now());

        if (verificationExists == Boolean.TRUE) {
            throw new IllegalStateException(String.format("Code has already been sent." +
                    " Wait for %s minutes before requesting another code", codeValidityInMinutes));
        }

        sendAuthOtp(validPhoneNumber, appName);
    }

    private String validatePhoneNumber(final String phoneNumber) {
        final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber parsedPhoneNumber = null;
        try {
            parsedPhoneNumber = phoneNumberUtil.parse(phoneNumber, "MZ");
        } catch (NumberParseException e) {
            e.printStackTrace();
        }

        if (!phoneNumberUtil.isValidNumber(parsedPhoneNumber)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Phone Number");
        }
        log.info("Request to authenticate {} received", phoneNumber);
        return phoneNumberUtil.format(parsedPhoneNumber, INTERNATIONAL).replaceAll("\\s", "");
    }

    private void sendAuthOtp(final String phoneNumber, final String appName) {
        final String authCode = generateRandomAlphanumericString();
        final User user = getOrCreateUser(phoneNumber, authCode, appName);
        UserVerification verification = new UserVerification();
        verification.setPhoneNumber(phoneNumber);
        verification.setVerificationCode(authCode);
        verification.setUser(user);
        verification.setExpiry(Instant.now().plus(codeValidityInMinutes, ChronoUnit.MINUTES));
        try {
            sendAuthOtpCode(new String[]{phoneNumber}, authCode);
            verification.setVerificationSMSStatus(SMS_SENT);
        } catch (IOException e) {
            e.printStackTrace();
            verification.setVerificationSMSStatus(SMS_FAILED);
        }
        log.info("Verification to be saved {}", verification);
        verificationRepository.save(verification);
    }


    private User getOrCreateUser(final String phoneNumber, final String authCode, final String appName) {
        User existingUser = getUserWithPhoneNumber(phoneNumber);
        if(Objects.isNull(existingUser)) {
            //create new user on keycloak
            keycloakService.createUser(phoneNumber.substring(1));

            User newUser = new User();
            newUser.setPhoneNumber(phoneNumber);
            newUser.setUserName(phoneNumber.substring(1));
            newUser.setVerificationStatus(VerificationStatus.UNVERIFIED);
            newUser.setPassword(passwordEncoder.encode(authCode));
            log.info("Creating new  User {}", newUser);
            final User savedUser = userRepository.save(newUser);
            return savedUser;
        }
        //create existing user if need be on keycloak
        keycloakService.createUser(existingUser.getPhoneNumber().substring(1));
        return existingUser;
    }

    private User getUserWithPhoneNumber(String phoneNumber) {
        return  userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Could not find user by Phone number: %s", phoneNumber)));
    }
    private void sendAuthOtpCode(final String[] phoneNumbers, String code) throws IOException {
        String message = String.format("Your Authentication code is: %s", code);
        smsService.sendMessage(message, msSender, phoneNumbers);
        log.info("OTP sent successfully for phone numbers", phoneNumbers);
    }

    private String generateRandomAlphanumericString() {
        return RandomStringUtils.random(codeLength, 0, 0, true, false, null, secureRandom).toUpperCase(Locale.ROOT);
    }

    @Override
    public void authenticateUser(OtpAuthRequest otpAuthRequest) {
        final String validPhoneNumber = validatePhoneNumber(otpAuthRequest.getPhoneNumber());
        UserVerification verification = verificationRepository
                .findUserVerificationByPhoneNumberAndVerificationCodeAndExpiryAfter(validPhoneNumber, otpAuthRequest.getAuthCode(), Instant.now())
                .orElseThrow(()->new AccessDeniedException("The code provided is invalid or has expired"));
        User user = getUserWithPhoneNumber(validPhoneNumber);
        user.setActive(LifeCyCleState.ACTIVE.isActive());
        user.setVerificationStatus(VerificationStatus.VERIFIED);
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);
        verification.setVerifiedOn(Instant.now());
        verification.setUpdatedAt(Instant.now());
        verificationRepository.save(verification);
    }
}
