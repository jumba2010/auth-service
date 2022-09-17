package jumba.auth.service.notification;

import app.kyosk.libs.messaging.EmailMessage;
import app.kyosk.libs.messaging.Message;
import app.kyosk.libs.messaging.Recipient;
import app.kyosk.user.notification.dto.EmailTemplateMetadata;
import app.kyosk.user.notification.utils.MessagingTemplateUtils;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class BaseEmailTemplateMapper {

    public List<EmailMessage> getEmailMessageList(EmailNotification emailNotification, EmailTemplateMetadata templateMetadata) {
        List<EmailMessage> emailMessageList = new ArrayList<>();

        Map<String, String> metadata = MessagingTemplateUtils.getPopulatedMetadata(templateMetadata.getFromEmail(),
            templateMetadata.getTemplateName(), templateMetadata.getSubject());
        Message message = MessagingTemplateUtils.getMessage(metadata);
        List<Recipient> recipients = getRecipients(emailNotification);

        recipients.forEach(mailRecipient -> {
            Map<String, Object> populatedModel = getPopulatedModel(emailNotification, mailRecipient.getName());

            // If email is valid, create notification
            if ( EmailValidator.getInstance().isValid(mailRecipient.getAddress()) ) {
                var emailMessage = new EmailMessage(getEmailName(), message, Collections.singletonList(mailRecipient), populatedModel);
                emailMessageList.add(emailMessage);
            }
        });

        return emailMessageList;
    }

    protected abstract List<Recipient> getRecipients(EmailNotification emailNotification);
    protected abstract Map<String, Object> getPopulatedModel(EmailNotification emailNotification, String name);
    protected abstract String getEmailName();
}
