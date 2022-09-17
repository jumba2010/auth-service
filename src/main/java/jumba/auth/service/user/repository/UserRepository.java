package jumba.auth.service.user.repository;

import jumba.auth.service.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findOneByActivationKey(String key);

    Optional<User> findOneByEmailIgnoreCase(String email);

    Optional<User> findOneByResetKey(String key);

    Optional<User> findOneByUsername(String toLowerCase);
}
