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
public class UserDeactivationTemplateMapper extends BaseEmailTemplateMapper {

    @Override
    protected Map<String, Object> getPopulatedModel(EmailNotification emailNotification, String name) {
        Map<String, Object> templateModel = new HashMap<>();
        UserDeactivationNotification userNotification = (UserDeactivationNotification) emailNotification;
        templateModel.put("name", name);
        templateModel.put("userName", userNotification.getUserName());
        templateModel.put("userRole", userNotification.getUserRole());
        templateModel.put("creationTime", userNotification.getCreationTime());
        templateModel.put("login", userNotification.getLogin());
        templateModel.put("deactivationComment", userNotification.getDeactivationComment());
        templateModel.put("url", userNotification.getUrl());

        return templateModel;
    }


    @Override
    protected List<Recipient> getRecipients(EmailNotification emailNotification) {
        UserDeactivationNotification userNotification = (UserDeactivationNotification) emailNotification;

        final List<Recipient> userRecipients = userNotification.getRecipients();
        Set<Recipient> recipients = new HashSet<>(userRecipients);
        return new ArrayList<>(recipients);
    }

    @Override
    protected String getEmailName() {
        return UserConstants.USER_DEACTIVATION_EMAIL_NAME;
    }

}
