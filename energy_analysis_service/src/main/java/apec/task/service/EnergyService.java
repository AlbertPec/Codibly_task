package apec.task.service;

import apec.task.dto.ChargingWindowResponseDto;
import apec.task.dto.EnergyMixResponseDto;

public interface EnergyService {

    EnergyMixResponseDto getEnergyMix();

    ChargingWindowResponseDto getChargingWindow(int windowLength);
}
