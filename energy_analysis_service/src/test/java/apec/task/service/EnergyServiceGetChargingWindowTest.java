package apec.task.service;

import apec.task.client.CarbonIntensityClient;
import apec.task.config.EnergyProperties;
import apec.task.domain.charging_window.ChargingWindowCalculator;
import apec.task.domain.charging_window.ChargingWindowCalculatorImpl;
import apec.task.domain.energy_mix.EnergyMixCalculator;
import apec.task.domain.energy_mix.EnergyMixCalculatorImpl;
import apec.task.dto.carbon_intensity.FuelShare;
import apec.task.dto.ChargingWindowResponseDto;
import apec.task.dto.carbon_intensity.GenerationDataEntry;
import apec.task.dto.carbon_intensity.GenerationResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EnergyServiceGetChargingWindowTest {
    @Test
    void shouldComputeRealChargingWindow() {
        // GIVEN
        EnergyProperties props = new EnergyProperties();
        props.setGreenSources(Set.of("solar", "wind"));

        ChargingWindowCalculator windowCalc = new ChargingWindowCalculatorImpl(props);
        EnergyMixCalculator mixCalc = new EnergyMixCalculatorImpl(props);

        CarbonIntensityClient client = mock(CarbonIntensityClient.class);

        List<GenerationDataEntry> data = List.of(
                entry(10),
                entry(80),
                entry(70),
                entry(20)
        );

        when(client.getGeneration(any(), any()))
                .thenReturn(new GenerationResponse(data));

        EnergyServiceImpl service =
                new EnergyServiceImpl(client, windowCalc, mixCalc);

        // WHEN
        ChargingWindowResponseDto result = service.getChargingWindow(1);

        // THEN
        assertEquals(data.get(1).from(), result.windowDateTimeStart());
        assertEquals(data.get(2).to(), result.windowDateTimeEnd());
        assertEquals(75.0, result.avgCleanEnergyPercentage());
    }

    private GenerationDataEntry entry(double green) {
        LocalDateTime t = LocalDateTime.now();
        return new GenerationDataEntry(
                t,
                t.plusMinutes(30),
                List.of(
                        new FuelShare("solar", green),
                        new FuelShare("coal", 100-green)
                )
        );
    }
}
