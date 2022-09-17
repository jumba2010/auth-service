package jumba.auth.service.notification.utils;

import app.kyosk.libs.messaging.EmailMessage;
import app.kyosk.libs.messaging.Message;
import app.kyosk.user.notification.BaseEmailTemplateMapper;
import app.kyosk.user.notification.EmailNotification;
import app.kyosk.user.notification.EmailNotificationService;
import app.kyosk.user.notification.dto.EmailTemplateMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationHandlerUtils {
    private final EmailNotificationService emailNotificationService;
    private final KafkaTemplate<String,String> kafkaTemplate;

    @Value("${notification.email.from-email}")
    private String fromEmail;

    public void handleNotificationPublishing(final BaseEmailTemplateMapper emailTemplateMapper,
                                             final Optional<EmailNotification> notificationOptional,
                                             final String message,
                                             final String template,
                                             final String subject,
                                             final String emailSenderContext) {

        if ( notificationOptional.isPresent() ) {
            final EmailNotification notification = notificationOptional.get();
            log.info(message + notification);

            EmailTemplateMetadata templateMetadata = EmailTemplateMetadata.builder()
                .templateName(template)
                .subject(subject)
                .fromEmail(fromEmail)
                .build();

            final List<EmailMessage> emailMessages =
                emailTemplateMapper.getEmailMessageList(notification, templateMetadata);

            emailNotificationService.publish(emailMessages, emailSenderContext, kafkaTemplate);
        }

    }

    public Message getMessage(Map<String, String> metadata) {
        Message message = new Message();
        message.setMetadata(metadata);
        message.setContents("");

        return message;
    }

    public void invokeNotificationService(final BaseEmailTemplateMapper emailTemplateMapper,
                                          final EmailNotification notification,
                                          final String message,
                                          final String template,
                                          final String subject,
                                          final String emailSenderContext) {
        log.info(message + notification.toString());

        EmailTemplateMetadata templateMetadata = EmailTemplateMetadata.builder()
            .templateName(template)
            .subject(subject)
            .fromEmail(fromEmail)
            .build();

        final List<EmailMessage> emailMessages = emailTemplateMapper
            .getEmailMessageList(notification, templateMetadata);

        emailNotificationService.publish(emailMessages, emailSenderContext, kafkaTemplate);
    }
}
