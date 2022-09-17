package jumba.auth.service.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import jumba.auth.service.generic.entity.LifeCycleEntity;
import jumba.auth.service.utils.Constants;
import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.BatchSize;

import javax.management.relation.Role;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "t_user")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class User extends LifeCycleEntity<UUID> implements Serializable {

    private static final long serialVersionUID = 1L;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user", fetch = FetchType.LAZY)
    List<UserVerification> verifications;
    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    @EqualsAndHashCode.Include
    private String username;
    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    private String password;
    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;
    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;
    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    private String email;
    @Size(min = 5, max = 15)
    @Column(name = "phone_number", length = 15)
    private String phoneNumber;
    @Size(min = 2, max = 10)
    @Column(name = "lang_key", length = 10)
    private String langKey;
    @Size(max = 256)
    @Column(name = "image_url", length = 256)
    private String imageUrl;
    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    private String activationKey;
    @Size(max = 20)
    @Column(name = "reset_key", length = 20)

    @JsonIgnore
    private String resetKey;
    @Column(name = "reset_date")
    private Instant resetDate = null;
    @Column(name = "national_id")
    private String nationalID;
    @Column(name = "terms_accepted")
    private boolean termsAccepted;
    @Column(name = "terms_accepted_date")
    private Instant termsAcceptedDate;
    @Column(name = "name_update_count")
    private Integer updateNameCount;


    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    @BatchSize(size = 20)
    Set<Role> oldAuthsToDel = new HashSet<>();
    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status")
    private VerificationStatus verificationStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "match_status")
    private MatchStatus matchStatus = MatchStatus.UN_MATCHED;
    @OneToMany(orphanRemoval = true, mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserEntityRole> uers;

    public void setUserName(String username) {
        this.username = StringUtils.lowerCase(username, Locale.ENGLISH);
    }

    public String getFirstName() {
        return firstName != null ? firstName : "";
    }

    public String getLastName() {
        return lastName != null ? lastName : "";
    }

    public String getFullNames() {
        return "" + getFirstName() + " " + getLastName();
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", activated='" + this.isActive() + '\'' +
                ", langKey='" + langKey + '\'' +
                ", activationKey='" + activationKey + '\'' +
                "}";
    }
}
