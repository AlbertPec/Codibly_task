package apec.task.service;

import apec.task.client.CarbonIntensityClient;
import apec.task.config.EnergyProperties;
import apec.task.domain.energy_mix.EnergyMixCalculator;
import apec.task.dto.*;
import apec.task.domain.charging_window.ChargingWindowCalculator;
import apec.task.domain.charging_window.ChargingWindowCalculatorImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnergyServiceImpl implements EnergyService{

    private final CarbonIntensityClient client;
    private final ChargingWindowCalculator chargingWindowCalculator;
    private final EnergyMixCalculator energyMixCalculator;

    public EnergyMixResponseDto getEnergyMix(){
        LocalDate today = LocalDate.now();
        List<GenerationDataEntry> data;

        data = client.getGeneration(today.atStartOfDay(), today.atStartOfDay().plusHours(24)).data();
        DailyEnergyMixDto todayEnergy = new DailyEnergyMixDto(today, energyMixCalculator.calculateAverageFuelsShare(data), energyMixCalculator.calculateAverageDailyGreenShare(data));

        data = client.getGeneration(today.plusDays(1).atStartOfDay(), today.plusDays(1).atStartOfDay().plusHours(24)).data();
        DailyEnergyMixDto tomorrowEnergy = new DailyEnergyMixDto(today.plusDays(1), energyMixCalculator.calculateAverageFuelsShare(data), energyMixCalculator.calculateAverageDailyGreenShare(data));

        data = client.getGeneration(today.plusDays(2).atStartOfDay(), today.plusDays(2).atStartOfDay().plusHours(24)).data();

        DailyEnergyMixDto afterTomorrowEnergy = new DailyEnergyMixDto(today.plusDays(2), energyMixCalculator.calculateAverageFuelsShare(data), energyMixCalculator.calculateAverageDailyGreenShare(data));

        return new EnergyMixResponseDto(todayEnergy, tomorrowEnergy, afterTomorrowEnergy);
    }

    public ChargingWindowResponseDto getChargingWindow(int chargeWindowLength){
        List<GenerationDataEntry> data = client.getGeneration(LocalDateTime.now(), LocalDateTime.now().plusHours(48)).data();

        return this.chargingWindowCalculator.getChargingWindow(data, chargeWindowLength);
    }

}
