package jumba.auth.service.user.dto;

import jumba.auth.service.user.domain.VerificationStatus;
import jumba.auth.service.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserDto {

    private UUID id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @EqualsAndHashCode.Include
    private String username;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    private String name;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    private String phoneNumber;

    @Size(max = 256)
    private String imageUrl;

    private boolean active;

    @Size(min = 2, max = 10)
    private String langKey;

    private String newPassword;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private boolean termsAccepted;

    private Instant termsAcceptedDate;

    private Set<String> authorities;

    private VerificationStatus verificationStatus;
}
