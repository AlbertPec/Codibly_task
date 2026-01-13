package apec.task.domain.charging_window;

import apec.task.dto.carbon_intensity.GenerationDataEntry;

import java.util.List;

public interface ChargingWindowCalculator {

    /**
     * @param data list of every generation record
     * @param start the index of generation which starts the window
     * @param windowSize the number of generation records included in window
     * @return Average share of green fuels in window data < start,start+windowSize )
     */
    double calculateWindowScore(List<GenerationDataEntry> data, int start, int windowSize);

    /**
     * @param data list of every generation record in searched time-window
     * @param windowSize Length of window (in number of records, NOT hours)
     * @return index in data list of starting point for the optimal window
     */
    int findBestWindowStartIndex(List<GenerationDataEntry> data, int windowSize);

}
