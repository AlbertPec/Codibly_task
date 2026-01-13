package apec.task.service;

import apec.task.client.CarbonIntensityClient;
import apec.task.config.EnergyProperties;
import apec.task.domain.charging_window.ChargingWindowCalculatorImpl;
import apec.task.domain.energy_mix.EnergyMixCalculatorImpl;
import apec.task.dto.EnergyMixResponseDto;
import apec.task.dto.carbon_intensity.FuelShare;
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

public class EnergyServiceGetEnergyMixTest {
    @Test
    void shouldComputeDailyEnergyMixForThreeDays() {
        // GIVEN
        EnergyProperties props = new EnergyProperties();
        props.setGreenSources(Set.of("solar", "wind"));

        CarbonIntensityClient client = mock(CarbonIntensityClient.class);

        List<GenerationDataEntry> data = List.of(
                entry(LocalDateTime.now().withHour(1), 20),
                entry(LocalDateTime.now().withHour(2), 40),

                entry(LocalDateTime.now().plusDays(1).withHour(1), 60),
                entry(LocalDateTime.now().plusDays(1).withHour(2), 80),

                entry(LocalDateTime.now().plusDays(2).withHour(1), 50),
                entry(LocalDateTime.now().plusDays(2).withHour(2), 70)
        );

        when(client.getGeneration(any(), any()))
                .thenReturn(new GenerationResponse(data));

        EnergyServiceImpl service = new EnergyServiceImpl(
                client,
                new ChargingWindowCalculatorImpl(props),
                new EnergyMixCalculatorImpl(props)
        );

        // WHEN
        EnergyMixResponseDto result = service.getEnergyMix();

        // THEN
        assertEquals(30.0, result.today().cleanEnergyPercentage());
        assertEquals(70.0, result.tomorrow().cleanEnergyPercentage());
        assertEquals(60.0, result.afterTomorrow().cleanEnergyPercentage());
    }

    private GenerationDataEntry entry(LocalDateTime time, double green) {
        return new GenerationDataEntry(
                time,
                time.plusMinutes(30),
                List.of(
                        new FuelShare("solar", green),
                        new FuelShare("coal", 100-green)
                )
        );
    }
}
