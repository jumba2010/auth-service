package jumba.auth.service.notification.utils;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeFormatterUtils {
    private DateTimeFormatterUtils() {}

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static String formatDateTime(Instant instant) {
        final var trqLocalDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("Africa/Nairobi"));
        return trqLocalDateTime.format(formatter);
    }
}
