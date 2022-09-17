package jumba.auth.service.notification.user.mapper;

import app.kyosk.libs.messaging.Recipient;
import app.kyosk.user.auth.domain.Role;
import app.kyosk.user.notification.EmailNotification;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Set;

@Value
@Builder
public class UserNotification implements EmailNotification {
    String name;
    String userName;
    String login;
    Set<Role> userRole;
    String creationTime;
    List<Recipient> recipients;
    String url;
}
