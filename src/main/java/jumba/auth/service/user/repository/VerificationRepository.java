package jumba.auth.service.user.repository;

import jumba.auth.service.user.domain.UserVerification;
import jumba.auth.service.user.domain.VerificationSMSStatus;
import org.springframework.data.repository.CrudRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface VerificationRepository extends CrudRepository<UserVerification, UUID> {
    boolean existsUserVerificationByPhoneNumberEqualsAndVerificationSMSStatusEqualsAndExpiryGreaterThan(final String validPhoneNumber, final VerificationSMSStatus smsSent, final Instant now);

    Optional<UserVerification> findUserVerificationByPhoneNumberAndVerificationCodeAndExpiryAfter(String validPhoneNumber, String authCode, Instant now);
}
