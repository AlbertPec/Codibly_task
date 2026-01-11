package apec.task.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtil {

    private static final DateTimeFormatter ISO_UTC_FORMAT =
            DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    private DateTimeUtil() {
    }

    public static String toUtcIsoString(LocalDateTime date) {
        return date
                .atZone(ZoneOffset.UTC)
                .withNano(0)
                .format(ISO_UTC_FORMAT);
    }
}
