package apec.task.dto;

import apec.task.dto.carbon_intensity.FuelShare;

import java.time.LocalDate;
import java.util.List;

public record DailyEnergyMixDto(
        LocalDate date,
        List<FuelShare> avgFuelShare,
        double cleanEnergyPercentage
) {
}