package apec.task.dto;

import java.time.LocalDateTime;

public record ChargingWindowResponseDto(
        LocalDateTime windowDateTimeStart,
        LocalDateTime windowDateTimeEnd,
        double avgCleanEnergyPercentage
) {
}
