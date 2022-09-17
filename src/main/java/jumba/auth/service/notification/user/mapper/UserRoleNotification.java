package jumba.auth.service.notification.user.mapper;

import app.kyosk.libs.messaging.Recipient;
import app.kyosk.user.notification.EmailNotification;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class UserRoleNotification implements EmailNotification {
    String name;
    String username;
    String login;
    List<String> existingRoles;
    List<String> newRoles;
    List<Recipient> recipients;
    String url;
}
