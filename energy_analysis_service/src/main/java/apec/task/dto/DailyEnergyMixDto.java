package apec.task.dto;

import java.time.LocalDate;
import java.util.Map;

public record DailyEnergyMixDto(
        LocalDate date,
        Map<String, Double> avgSources,
        double cleanEnergyPercentage
) {
}