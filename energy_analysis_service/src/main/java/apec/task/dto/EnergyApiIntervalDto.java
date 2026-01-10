package apec.task.dto;

import java.time.OffsetDateTime;
import java.util.Map;

public record EnergyApiIntervalDto(
        OffsetDateTime from,
        OffsetDateTime to,
        Map<String, Double> sources
) {
}
