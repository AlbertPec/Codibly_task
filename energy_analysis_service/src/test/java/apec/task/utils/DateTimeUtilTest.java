package apec.task.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;



class DateTimeUtilTest {
    @Test
    void shouldFormatAccordingToIso() {
        // GIVEN
        LocalDateTime date = LocalDateTime.of(2016, 8, 25, 12, 35, 0);

        // WHEN
        String result = DateTimeUtil.toUtcIsoString(date);

        // THEN
        assertEquals("2016-08-25T12:35Z", result);
    }

    @Test
    void midnightShouldBelongToSameDay() {
        // GIVEN
        LocalDateTime midnight = LocalDateTime.of(2016, 3, 10, 0, 0, 0);

        // WHEN
        String result = DateTimeUtil.toUtcIsoString(midnight);

        // THEN
        assertEquals("2016-03-10T00:00Z", result);
    }

    @Test
    void shouldRemoveNanoseconds() {
        // GIVEN
        LocalDateTime dateWithNanos =
                LocalDateTime.of(2016, 3, 10, 15, 42, 10, 123_000_000);

        // WHEN
        String result = DateTimeUtil.toUtcIsoString(dateWithNanos);

        // THEN
        assertEquals("2016-03-10T15:42Z", result);
    }

    @Test
    void shouldEndWithZSuffix() {
        // GIVEN
        LocalDateTime date = LocalDateTime.of(2016, 1, 1, 8, 30);

        // WHEN
        String result = DateTimeUtil.toUtcIsoString(date);

        // THEN
        assertTrue(result.endsWith("Z"));
    }
}