package jumba.auth.service.user.domain;

import jumba.auth.service.generic.entity.LifeCycleEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_verification")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class UserVerification extends LifeCycleEntity<UUID> {

    @ManyToOne(optional = false, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_login", referencedColumnName = "username")
    private User user;

    @Column(nullable = false, length = 15, name = "phone_number")
    private String phoneNumber;
    @Column(columnDefinition = "timestamp")
    private Instant expiry;

    @Column(name = "verification_code")
    private String verificationCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_sms_status")
    private VerificationSMSStatus verificationSMSStatus;
    @Column(
            columnDefinition = "timestamp", name = "verified_on"
    )
    private Instant verifiedOn;

}