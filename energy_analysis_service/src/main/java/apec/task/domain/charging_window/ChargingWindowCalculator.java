package apec.task.domain.charging_window;

import apec.task.dto.carbon_intensity.GenerationDataEntry;

import java.util.List;

public interface ChargingWindowCalculator {

    double calculateWindowScore(List<GenerationDataEntry> data, int start, int windowSize);

    int findBestWindowStartIndex(List<GenerationDataEntry> data, int windowSize);

}
