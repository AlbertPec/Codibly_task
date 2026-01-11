package apec.task.domain.energy_mix;

import apec.task.dto.FuelShare;
import apec.task.dto.GenerationDataEntry;

import java.util.List;

public interface EnergyMixCalculator {

    double calculateAverageDailyGreenShare(List<GenerationDataEntry> data);

    List<FuelShare> calculateAverageFuelsShare(List<GenerationDataEntry> data);

}
