package apec.task.dto;

import java.time.LocalDateTime;

public record ChargingWindowDto(
        LocalDateTime windowStart,
        LocalDateTime windowEnd,
        double avgCleanEnergyPercentage
) {
}
