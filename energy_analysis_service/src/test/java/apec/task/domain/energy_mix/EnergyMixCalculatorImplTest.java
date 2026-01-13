package apec.task.domain.energy_mix;

import apec.task.config.EnergyProperties;
import apec.task.dto.carbon_intensity.FuelShare;
import apec.task.dto.carbon_intensity.GenerationDataEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EnergyMixCalculatorImplTest {
    private EnergyMixCalculatorImpl calculator;

    @BeforeEach
    void setup() {
        EnergyProperties props = new EnergyProperties();
        props.setGreenSources(Set.of("solar", "wind", "hydro"));
        calculator = new EnergyMixCalculatorImpl(props);
    }

    @Test
    void calculateAverageDailyGreenShare_shouldReturnAverageGreenPercentage() {
        // GIVEN
        List<GenerationDataEntry> data = List.of(
                entry(greenMix(20), otherMix(80)),
                entry(greenMix(40), otherMix(60)),
                entry(greenMix(60), otherMix(40))
        );

        // WHEN
        double result = calculator.calculateAverageDailyGreenShare(data);

        // THEN
        assertEquals(40.0, result);
    }

    @Test
    void calculateAverageDailyGreenShare_shouldReturnZeroForEmptyData() {
        double result = calculator.calculateAverageDailyGreenShare(List.of());
        assertEquals(0.0, result);
    }

    @Test
    void calculateAverageDailyGreenShare_shouldIgnoreNonGreenFuels() {
        // GIVEN
        List<GenerationDataEntry> data = List.of(
                entry(new FuelShare("coal", 100.0))
        );

        // WHEN
        double result = calculator.calculateAverageDailyGreenShare(data);

        // THEN
        assertEquals(0.0, result);
    }

    @Test
    void calculateAverageFuelsShare_shouldReturnAveragePerFuel() {
        // GIVEN
        List<GenerationDataEntry> data = List.of(
                entry(new FuelShare("solar", 20.0), new FuelShare("coal", 80.0)),
                entry(new FuelShare("solar", 40.0), new FuelShare("coal", 60.0)),
                entry(new FuelShare("solar", 60.0), new FuelShare("coal", 40.0))
        );

        // WHEN
        List<FuelShare> result = calculator.calculateAverageFuelsShare(data);

        // THEN
        assertEquals(2, result.size());

        FuelShare solar = result.stream()
                .filter(f -> f.fuel().equals("solar"))
                .findFirst()
                .orElseThrow();

        FuelShare coal = result.stream()
                .filter(f -> f.fuel().equals("coal"))
                .findFirst()
                .orElseThrow();

        assertEquals(40.0, solar.perc());
        assertEquals(60.0, coal.perc());
    }

    @Test
    void calculateAverageFuelsShare_shouldReturnEmptyListForEmptyData() {
        List<FuelShare> result = calculator.calculateAverageFuelsShare(List.of());
        assertTrue(result.isEmpty());
    }

    @Test
    void calculateAverageFuelsShare_shouldHandleMultipleFuelTypes() {
        // GIVEN
        List<GenerationDataEntry> data = List.of(
                entry(new FuelShare("solar", 10), new FuelShare("wind", 20), new FuelShare("coal", 70)),
                entry(new FuelShare("solar", 30), new FuelShare("wind", 40), new FuelShare("coal", 30))
        );

        // WHEN
        List<FuelShare> result = calculator.calculateAverageFuelsShare(data);

        // THEN
        assertEquals(3, result.size());

        assertFuel(result, "solar", 20.0);
        assertFuel(result, "wind", 30.0);
        assertFuel(result, "coal", 50.0);
    }

    // HELPERS

    private GenerationDataEntry entry(FuelShare... shares) {
        return new GenerationDataEntry(
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(30),
                List.of(shares)
        );
    }

    private FuelShare greenMix(double greenPerc) {
        return new FuelShare("solar", greenPerc);
    }

    private FuelShare otherMix(double perc) {
        return new FuelShare("coal", perc);
    }

    private void assertFuel(List<FuelShare> list, String fuel, double expected) {
        FuelShare fs = list.stream()
                .filter(f -> f.fuel().equals(fuel))
                .findFirst()
                .orElseThrow();

        assertEquals(expected, fs.perc());
    }
}