package jumba.auth.service.user.service;

import jumba.auth.service.user.domain.User;
import jumba.auth.service.user.dto.UserDto;
import jumba.auth.service.user.dto.UserUpdateDTO;

import java.util.Optional;

public interface UserService {

    Optional<User> activateRegistration(String key);

    Optional<User> requestPasswordReset(String mail);

    Optional<User> completePasswordReset(String newPassword, String key);

    User registerUser(UserDto userDto, String password);

    void updateUser(UserUpdateDTO toUpdate);

    void inactivateUser(String username);
}
