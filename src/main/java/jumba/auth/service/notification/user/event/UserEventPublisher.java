package jumba.auth.service.notification.user.event;

import jumba.auth.service.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.management.relation.Role;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserEventPublisher {

    private final ApplicationEventPublisher publisher;

    public void publishEvent(UserCreationEvent event) {
        publisher.publishEvent(event);
    }

    public void publishEvent(UserRoleChangeEvent<List<Role>, List<Role>, User> event) {
        publisher.publishEvent(event);
    }

    public void publishEvent(UserDeactivationEvent event) {
        publisher.publishEvent(event);
    }
}
