package apec.task.domain.charging_window;

import apec.task.config.EnergyProperties;
import apec.task.dto.carbon_intensity.FuelShare;
import apec.task.dto.carbon_intensity.GenerationDataEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ChargingWindowCalculatorImplTest {
    private ChargingWindowCalculatorImpl calculator;

    @BeforeEach
    void setup() {
        EnergyProperties props = new EnergyProperties();
        props.setGreenSources(Set.of("solar", "wind"));
        calculator = new ChargingWindowCalculatorImpl(props);
    }

    @Test
    void calculateWindowScore_shouldReturnAverageGreenShareInWindow() {
        // GIVEN
        List<GenerationDataEntry> data = List.of(
                entry(10),
                entry(20),
                entry(30),
                entry(40)
        );

        // WHEN
        double result = calculator.calculateWindowScore(data, 1, 2);

        // THEN
        assertEquals(25.0, result);
    }

    @Test
    void findBestWindowStartIndex_shouldReturnIndexOfHighestGreenWindow() {
        // GIVEN
        List<GenerationDataEntry> data = List.of(
                entry(10),
                entry(80),
                entry(70),
                entry(20)
        );

        // WHEN
        int result = calculator.findBestWindowStartIndex(data, 2);

        // THEN
        assertEquals(1, result);
    }

    @Test
    void findBestWindowStartIndex_windowLengthMustFitInsideData() {
        // GIVEN
        List<GenerationDataEntry> data = List.of(
                entry(10),
                entry(20),
                entry(30)
        );

        // WHEN
        int windowSize = 2;
        int startIndex = calculator.findBestWindowStartIndex(data, windowSize);

        // THEN
        assertTrue(startIndex + windowSize <= data.size());
    }

    @Test
    void calculateWindowScore_windowMustNotExceedDataRange() {
        // GIVEN
        List<GenerationDataEntry> data = List.of(
                entry(10),
                entry(20)
        );

        // WHEN + THEN
        assertThrows(IndexOutOfBoundsException.class,
                () -> calculator.calculateWindowScore(data, 1, 5));
    }

    @Test
    void greenFuelFilteringMustIgnoreNonGreenSources() {
        // GIVEN
        GenerationDataEntry entry = new GenerationDataEntry(
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(30),
                List.of(
                        new FuelShare("solar", 40.0),
                        new FuelShare("coal", 50.0),
                        new FuelShare("wind", 10.0)
                )
        );
        // WHEN
        double result = calculator.calculateWindowScore(List.of(entry), 0, 1);

        // THEN
        assertEquals(50.0, result);
    }

    // HELPERS

    private GenerationDataEntry entry(double greenPercent) {
        return new GenerationDataEntry(
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(30),
                List.of(
                        new FuelShare("solar", greenPercent),
                        new FuelShare("coal", 100.0 - greenPercent)
                )
        );
    }
}