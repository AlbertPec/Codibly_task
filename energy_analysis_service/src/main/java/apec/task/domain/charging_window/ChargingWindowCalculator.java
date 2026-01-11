package apec.task.domain.charging_window;

import apec.task.dto.ChargingWindowResponseDto;
import apec.task.dto.GenerationDataEntry;

import java.util.List;

public interface ChargingWindowCalculator {

    ChargingWindowResponseDto getChargingWindow(List<GenerationDataEntry> data, int windowHours);

}
