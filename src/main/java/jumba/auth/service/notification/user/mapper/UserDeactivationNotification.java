package jumba.auth.service.notification.user.mapper;

import app.kyosk.libs.messaging.Recipient;
import app.kyosk.user.notification.EmailNotification;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class UserDeactivationNotification implements EmailNotification {
    List<Recipient> recipients;
    String name;
    String userName;
    String[] userRole;
    String url;
    String deactivationComment;
    String creationTime;
    String login;
}
