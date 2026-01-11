package apec.task.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record DailyEnergyMixDto(
        LocalDate date,
        List<FuelShare> avgFuelShare,
        double cleanEnergyPercentage
) {
}