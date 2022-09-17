package jumba.auth.service.notification.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EmailTemplateMetadata {
    String fromEmail;
    String templateName;
    String subject;
}
