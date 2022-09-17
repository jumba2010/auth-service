package jumba.auth.service.user.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String username;

    private String firstName;

    private String lastName;

    private String password;

    private int state;

    private String phoneNumber;

    private boolean termsAccepted;

}
