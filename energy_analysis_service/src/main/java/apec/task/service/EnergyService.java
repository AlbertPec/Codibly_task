package apec.task.service;

import apec.task.dto.ChargingWindowResponseDto;
import apec.task.dto.EnergyMixResponseDto;


public interface EnergyService {

    /**
     * @return Average share for all sources of power and sum of share of green sources for today, tomorrow and
     * the day after tomorrow.
     */
    EnergyMixResponseDto getEnergyMix();

    /**
     * @param chargeHours Length of charging window in hours. Min 1h, Max 6h.
     * @return Date and time of start and end of the window, average green sources generation share in the window.
     */
    ChargingWindowResponseDto getChargingWindow(int chargeHours);
}
