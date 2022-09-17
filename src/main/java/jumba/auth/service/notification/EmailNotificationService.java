package jumba.auth.service.notification;

import app.kyosk.libs.messaging.EmailMessage;
import app.kyosk.libs.messaging.MessageWrapper;
import app.kyosk.libs.messaging.OutBoundMessage;
import app.kyosk.user.config.KafkaConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationService {
    private final KafkaConfig kafkaConfig;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ObjectMapper mapper = new ObjectMapper();

    public void publish(final List<EmailMessage> emailMessages, final String senderContext, final KafkaTemplate<String,String> kafkaTemplate) {

        emailMessages
            .forEach( emailMessage -> {
                Map<String, OutBoundMessage> message = new HashMap<>();
                message.put("EMAIL", emailMessage);

                MessageWrapper messageWrapper = new MessageWrapper();
                messageWrapper.setMessages(message);

                messageWrapper.setSenderContext(senderContext);
                messageWrapper.setSenderName(applicationName);

                publishEmail(kafkaTemplate, messageWrapper);
            } );

    }

    private void publishEmail(KafkaTemplate<String, String> kafkaTemplate, MessageWrapper messageWrapper) {
        try {
            final String data = mapper.writeValueAsString(messageWrapper);
            kafkaTemplate.send(kafkaConfig.getKafkaTopicName(), data)
                .addCallback(new ListenableFutureCallback<>() {

                    @Override
                    public void onSuccess(SendResult<String, String> result) {
                        final RecordMetadata recordMetadata = result.getRecordMetadata();
                        log.info("KafkaTemplate sent message='{}' to topic='{}' and partition='{}' with offset='{}'", data,
                            recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
                    }

                    @Override
                    public void onFailure(Throwable ex) {
                        log.error("KafkaTemplate unable to send message='{}'",ex.getMessage());
                    }
                });
        } catch (JsonProcessingException jpe) {
            log.error("Could not publish email to Kafka topic", jpe.getCause());
        }
    }

}
