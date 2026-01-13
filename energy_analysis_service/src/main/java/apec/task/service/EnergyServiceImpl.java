package apec.task.service;

import apec.task.client.CarbonIntensityClient;
import apec.task.domain.energy_mix.EnergyMixCalculator;
import apec.task.dto.*;
import apec.task.domain.charging_window.ChargingWindowCalculator;
import apec.task.dto.carbon_intensity.GenerationDataEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnergyServiceImpl implements EnergyService{

    private final CarbonIntensityClient client;
    private final ChargingWindowCalculator chargingWindowCalculator;
    private final EnergyMixCalculator energyMixCalculator;

    public EnergyMixResponseDto getEnergyMix(){
        LocalDate today = LocalDate.now();
        LocalDateTime startDate = today.atStartOfDay();
        LocalDateTime endDate = today.atStartOfDay().plusDays(3);

        List<GenerationDataEntry> data = client.getGeneration(startDate, endDate).data();

        Map<LocalDate, List<GenerationDataEntry>> dataByDay = data.stream()
                .collect(Collectors.groupingBy(e -> e.from().toLocalDate()));

        DailyEnergyMixDto todayEnergy = this.buildDailyEnergyMixDto(today, dataByDay);
        DailyEnergyMixDto tomorrowEnergy = this.buildDailyEnergyMixDto(today.plusDays(1), dataByDay);
        DailyEnergyMixDto afterTomorrowEnergy = this.buildDailyEnergyMixDto(today.plusDays(2), dataByDay);

        return new EnergyMixResponseDto(todayEnergy, tomorrowEnergy, afterTomorrowEnergy);
    }

    private DailyEnergyMixDto buildDailyEnergyMixDto(LocalDate day,Map<LocalDate, List<GenerationDataEntry>> dataByDay) {

        List<GenerationDataEntry> dayData = dataByDay.getOrDefault(day, List.of());

        return new DailyEnergyMixDto(
                day,
                energyMixCalculator.calculateAverageFuelsShare(dayData),
                energyMixCalculator.calculateAverageDailyGreenShare(dayData)
        );
    }

    public ChargingWindowResponseDto getChargingWindow(int chargeHours){
        int chargeWindowLength = chargeHours * 2;
        List<GenerationDataEntry> data = this.client.getGeneration(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(48)
        ).data();

        int windowStartIdx = this.chargingWindowCalculator.findBestWindowStartIndex(data, chargeWindowLength);
        double avgGreenEnergy = this.chargingWindowCalculator.calculateWindowScore(data,windowStartIdx,chargeWindowLength);

        return new ChargingWindowResponseDto(
                data.get(windowStartIdx).from(),
                data.get(windowStartIdx + chargeWindowLength - 1).to(),
                avgGreenEnergy
        );
    }

}
