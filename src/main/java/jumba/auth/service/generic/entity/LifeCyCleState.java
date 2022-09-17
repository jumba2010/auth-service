package jumba.auth.service.generic.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LifeCyCleState {
    ACTIVE(true, 0),
    INACTIVE(false, 1),
    DELETED(false, 2),
    BANNED(false, 3);

    private final boolean active;

    private final int state;
}
