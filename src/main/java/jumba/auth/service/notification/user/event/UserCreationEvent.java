package jumba.auth.service.notification.user.event;

import jumba.auth.service.user.domain.User;
import lombok.Getter;

@Getter
public class UserCreationEvent {

    private final User user;

    public UserCreationEvent(User user) {
        this.user = user;
    }
}
