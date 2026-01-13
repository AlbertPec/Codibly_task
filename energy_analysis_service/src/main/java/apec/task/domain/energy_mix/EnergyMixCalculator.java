package apec.task.domain.energy_mix;

import apec.task.dto.carbon_intensity.FuelShare;
import apec.task.dto.carbon_intensity.GenerationDataEntry;

import java.util.List;

public interface EnergyMixCalculator {

    /**
     * @param data List of every data record FOR ONE DAY
     * @return Percentage share of green energies for one day
     */
    double calculateAverageDailyGreenShare(List<GenerationDataEntry> data);

    /**
     * @param data List of every data record FOR ONE DAY
     * @return List of average fuel share in generation for each source
     */
    List<FuelShare> calculateAverageFuelsShare(List<GenerationDataEntry> data);

}
