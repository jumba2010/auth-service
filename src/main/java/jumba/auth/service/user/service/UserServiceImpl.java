package jumba.auth.service.user.service;

import jumba.auth.service.generic.entity.LifeCyCleState;
import jumba.auth.service.notification.user.event.UserDeactivationEvent;
import jumba.auth.service.notification.user.event.UserEventPublisher;
import jumba.auth.service.user.domain.User;
import jumba.auth.service.user.dto.UserDto;
import jumba.auth.service.user.dto.UserUpdateDTO;
import jumba.auth.service.user.repository.UserRepository;
import jumba.auth.service.user.repository.VerificationRepository;
import jumba.auth.service.utils.RandomUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final VerificationRepository verificationRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserEventPublisher userEventPublisher;

    @Override
    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
                .map(user -> {
                    user.setActive(LifeCyCleState.ACTIVE.isActive());
                    user.setActivationKey(null);
                    log.debug("Activated user: {}", user);
                    return user;
                });
    }

    @Override
    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailIgnoreCase(mail)
                .filter(user -> user.isActive())
                .map(user -> {
                    user.setResetKey(RandomUtil.generateResetKey());
                    user.setResetDate(Instant.now());
                    return user;
                });
    }

    @Override
    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository.findOneByResetKey(key)
                .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    user.setResetKey(null);
                    user.setResetDate(null);
                    return user;
                });
    }

    @Override
    @Transactional()
    public User registerUser(UserDto userDto, String password) {
        userRepository.findOneByUsername(userDto.getUsername().toLowerCase()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with the same username exists");
            }
        });
        userRepository.findOneByEmailIgnoreCase(userDto.getEmail()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with the same email exists");
            }
        });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setUserName(userDto.getUsername().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDto.getFirstName());
        newUser.setLastName(userDto.getLastName());
        newUser.setEmail(userDto.getEmail().toLowerCase());
        newUser.setImageUrl(userDto.getImageUrl());
        newUser.setLangKey(userDto.getLangKey());
        // new user is not active
        newUser.setState(LifeCyCleState.INACTIVE.getState());
        newUser.setActive(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        userRepository.save(newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.getState() == LifeCyCleState.INACTIVE.getState()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        return true;
    }


    @Override
    @Transactional()
    public void updateUser(UserUpdateDTO toUpdate) {
        User oldUser = fetchOrThrow(toUpdate.getUsername());
        if (toUpdate.getPassword() != null) {
            toUpdate.setPassword(passwordEncoder.encode(toUpdate.getPassword()));
        }
        if (!oldUser.isTermsAccepted() && toUpdate.isTermsAccepted()) {
            oldUser.setTermsAcceptedDate(Instant.now());
            oldUser.setTermsAccepted(true);
        }

        if (toUpdate.getPhoneNumber() != null) {
            oldUser.setPhoneNumber(toUpdate.getPhoneNumber());
        }

        oldUser = this.userRepository.save(oldUser);

        if (oldUser.getState() == LifeCyCleState.INACTIVE.getState()) {
            try {
                userEventPublisher.publishEvent(new UserDeactivationEvent(oldUser));
            } catch (Exception e) {
                log.error("Error publishing User Deactivation notification", e);
            }
        }
    }

    private User fetchOrThrow(String username) {
        return userRepository.findOneByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("User with username %s not found", username)));
    }

    @Override
    public void inactivateUser(String username) {

    }
}
