package apec.task.dto;

public record EnergyMixResponseDto(
        DailyEnergyMixDto today,
        DailyEnergyMixDto tomorrow,
        DailyEnergyMixDto afterTomorrow
) {
}
