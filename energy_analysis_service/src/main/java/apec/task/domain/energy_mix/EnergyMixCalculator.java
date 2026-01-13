package apec.task.domain.energy_mix;

import apec.task.dto.carbon_intensity.FuelShare;
import apec.task.dto.carbon_intensity.GenerationDataEntry;

import java.util.List;

public interface EnergyMixCalculator {

    double calculateAverageDailyGreenShare(List<GenerationDataEntry> data);

    List<FuelShare> calculateAverageFuelsShare(List<GenerationDataEntry> data);

}
