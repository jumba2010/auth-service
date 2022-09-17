package jumba.auth.service.notification.user.mapper;

import jumba.auth.service.notification.EmailNotification;
import jumba.auth.service.notification.user.constants.UserConstants;
import jumba.auth.service.notification.utils.DateTimeFormatterUtils;
import jumba.auth.service.notification.utils.DomainMapperUtils;
import jumba.auth.service.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.cms.Recipient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDomainMapper {
    private final DateTimeFormatterUtils dateTimeFormatterUtils;
    private final DomainMapperUtils domainMapperUtils;

    @Value("${notification.email.user.url}")
    private String url;

    public Optional<EmailNotification> toUserNotification(User user, String[] userRole) {
        List<Recipient> recipients = new ArrayList<>();
        for (String role : userRole) {
            Set<Recipient> roleRecipients = domainMapperUtils.fetchRecipientInfo(UserConstants.GLOBAL_SERVICE_ZONE, role,
                    "Error: Role " + role + " email not found request notification");
            recipients.addAll(roleRecipients);
        }
//
//        UserNotification userNotification = new UserNotification.UserNotificationBuilder()
//                .userName(user.getFirstName())
//                .login(user.getUsername())
//                .creationTime(DateTimeFormatterUtils.formatDateTime(user.getCreatedAt()))
//                .userRole(user.getOldAuthsToDel())
//                .recipients(new ArrayList<>(recipients))
//                .url(url + "?elId=" + user.getUsername() + "&short=true")
//                .build();

        return Optional.empty();
    }

    public Optional<EmailNotification> toUserRoleNotification(List<String> existingRoles, List<String> newRoles, User user) {
//        Set<Recipient> admin = domainMapperUtils.fetchRecipientInfo(UserConstants.GLOBAL_SERVICE_ZONE,
//                UserConstants.USER_ROLE_CHANGE_RECEIPIENT, "Admin role change recipients email not found");
//
//        UserRoleNotification notification = new UserRoleNotification.UserRoleNotificationBuilder()
//                .existingRoles(existingRoles)
//                .newRoles(newRoles)
//                .username(user.getFullNames())
//                .login(user.getLogin())
//                .url(url + "?elId=" + user.getLogin() + "&short=true")
//                .recipients(new ArrayList<>(admin))
//                .build();
//
        return Optional.empty();
    }

    public Optional<EmailNotification> toUserDeactivationNotification(User user, String[] deactivationRecipientRoles) {
//        List<Recipient> recipients = new ArrayList<>();
//        for (String role : deactivationRecipientRoles) {
//            Set<Recipient> roleRecipients = domainMapperUtils.fetchRecipientInfo(UserConstants.GLOBAL_SERVICE_ZONE, role,
//                    "Error: Role " + role + " email not found request notification");
//            recipients.addAll(roleRecipients);
//        }
//
//        UserDeactivationNotification deactivationNotification = new UserDeactivationNotification.UserDeactivationNotificationBuilder()
//                .recipients(new ArrayList<>(recipients))
//                .deactivationComment("Deactivated")
//                .userName(user.getFullNames())
//                .login(user.getLogin())
//                .creationTime(DateTimeFormatterUtils.formatDateTime(user.getLastModifiedDate()))
//                .url(url + "?elId=" + user.getLogin() + "&short=true")
//                .build();
//
       return Optional.empty();
    }
}
