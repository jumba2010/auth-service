package jumba.auth.service.notification.user.event;

import jumba.auth.service.user.domain.User;
import lombok.Getter;

@Getter
public class UserDeactivationEvent {
    private final User user;

    public UserDeactivationEvent(User user) {
        this.user = user;
    }
}
