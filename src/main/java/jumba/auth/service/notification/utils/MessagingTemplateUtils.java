package jumba.auth.service.notification.utils;

import app.kyosk.libs.messaging.Message;

import java.util.Map;

public final class MessagingTemplateUtils {
    private MessagingTemplateUtils() {}

    public static Message getMessage(Map<String, String> metadata) {
        Message message = new Message();
        message.setMetadata(metadata);
        message.setContents("");

        return message;
    }

    public static Map<String, String> getPopulatedMetadata(String fromEmail, String templateName, String subject) {
        return Map.of(
            "from", fromEmail,
            "subject", subject,
            "templateId", templateName
        );
    }

}

