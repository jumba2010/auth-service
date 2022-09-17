package jumba.auth.service.notification.user.event;

import jumba.auth.service.user.domain.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class UserRoleChangeEvent<M, S, U> extends ApplicationEvent {

    private final List<String> existingRoles;
    private final List<String> newRoles;
    private final User user;

    public UserRoleChangeEvent(List<String> existingRoles, List<String> newRoles, User user) {
        super(newRoles);
        this.existingRoles = existingRoles;
        this.newRoles = newRoles;
        this.user = user;
    }
}
