package jumba.auth.service.notification.user.mapper;


import app.kyosk.libs.messaging.Recipient;
import app.kyosk.user.notification.BaseEmailTemplateMapper;
import app.kyosk.user.notification.EmailNotification;
import app.kyosk.user.notification.user.constants.UserConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserRoleChangeTemplateMapper extends BaseEmailTemplateMapper {

    @Override
    protected Map<String, Object> getPopulatedModel(EmailNotification emailNotification, String name) {
        Map<String, Object> templateModel = new HashMap<>();
        UserRoleNotification userNotification = (UserRoleNotification) emailNotification;
        templateModel.put("name", name);
        templateModel.put("username", userNotification.getUsername());
        templateModel.put("existingRoles", userNotification.getExistingRoles());
        templateModel.put("newRoles", userNotification.getNewRoles());
        templateModel.put("login", userNotification.getLogin());

        return templateModel;
    }

    @Override
    protected List<Recipient> getRecipients(EmailNotification emailNotification) {
        var userRoleNotification = (UserRoleNotification) emailNotification;

        final List<Recipient> userRecipients = userRoleNotification.getRecipients();
        Set<Recipient> recipients = new HashSet<>(userRecipients);
        return new ArrayList<>(recipients);
    }

    @Override
    protected String getEmailName() {
        return UserConstants.USER_ROLE_CHANGE_NAME;
    }
}
