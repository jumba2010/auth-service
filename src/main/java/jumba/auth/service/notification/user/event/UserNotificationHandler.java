package jumba.auth.service.notification.user.event;

import jumba.auth.service.notification.BaseEmailTemplateMapper;
import jumba.auth.service.notification.EmailNotification;
import jumba.auth.service.notification.user.constants.UserConstants;
import jumba.auth.service.notification.user.mapper.UserCreatedTemplateMapper;
import jumba.auth.service.notification.user.mapper.UserDomainMapper;
import jumba.auth.service.notification.user.mapper.UserRoleChangeTemplateMapper;
import jumba.auth.service.notification.utils.NotificationHandlerUtils;
import jumba.auth.service.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserNotificationHandler {

    private final UserDomainMapper domainMapper;
    private final NotificationHandlerUtils notificationHandlerUtils;
    private final UserCreatedTemplateMapper templateMapper;
    private final UserRoleChangeTemplateMapper userRoleChangeTemplateMapper;
    private final BaseEmailTemplateMapper deactivationTemplateMapper;

    @Value("${notification.email.user.created-template}")
    private String template;

    @Value("${notification.email.user.role-change-template}")
    private String roleChangeTemplate;

    @Value("${notification.email.user.deactivated-template}")
    private String deactivationTemplate;

    @Async
    @EventListener
    public void handleUserCreationEvent(UserCreationEvent userCreationEvent) {
        try {
            final User createdUser = userCreationEvent.getUser();
            final Optional<EmailNotification> emailNotification = mapToNotification(createdUser);

//            notificationHandlerUtils.handleNotificationPublishing(templateMapper, emailNotification, "User created Notification email recieved.Details:\n",
//                template, UserConstants.USER_CREATED_EMAIL_SUBJECT, UserConstants.USER_CREATED_EMAIL_SENDER_CONTEXT);
        } catch (Exception exception) {
            log.error("Error publishing user created notification", exception);
        }
    }

    private Optional<EmailNotification> mapToNotification(User user) {
        return domainMapper.toUserNotification(user, UserConstants.USER_CREATED_RECEIPIENT);
    }

    @Async
    @EventListener
    public void handleUserRoleChangeEvent(UserRoleChangeEvent<List<String>, List<String>, User> userRoleChangeEvent) {
        try {
            final List<String> existingRoles = userRoleChangeEvent.getExistingRoles();
            final List<String> newRoles = userRoleChangeEvent.getNewRoles();
            final User user = userRoleChangeEvent.getUser();

            final Optional<EmailNotification> emailNotification = mapToRoleChangeNotification(existingRoles, newRoles, user);
//            notificationHandlerUtils.handleNotificationPublishing(userRoleChangeTemplateMapper, emailNotification, "user role change notification revieved.Details:\n",
//                roleChangeTemplate, UserConstants.USER_ROLE_CHANGE_SUBJECT, UserConstants.USER_ROLE_CHANGE_EMAIL_SENDER_CONTEXT);

        } catch (Exception exception) {
            log.error("Error publishing user role change notification", exception);
        }
    }

    private Optional<EmailNotification> mapToRoleChangeNotification(List<String> existingRoles, List<String> newRoles, User user) {
        return domainMapper.toUserRoleNotification(existingRoles, newRoles, user);
    }

    @Async
    @EventListener
    public void handleUserDeactivationEvent(UserDeactivationEvent userDeactivationEvent) {
        try {
            final User user = userDeactivationEvent.getUser();
            final Optional<EmailNotification> deactivationNotiifcation = mapToDeactivationNotification(user);
//            notificationHandlerUtils.handleNotificationPublishing(deactivationTemplateMapper, deactivationNotiifcation, "user deactivation notification revieved.Details:\n",
//            deactivationTemplate, UserConstants.USER_DEACTIVATION_SUBJECT,UserConstants.USER_DEACTIVATION_SENDER_CONTEXT );

        } catch (Exception exception) {
            log.error("Error publishing user deactivation notification", exception.getCause());
        }
    }

    private Optional<EmailNotification> mapToDeactivationNotification(User user) {
        return domainMapper.toUserDeactivationNotification(user, UserConstants.USER_DEACTIVATION_RECIPIENTS);
    }
}
