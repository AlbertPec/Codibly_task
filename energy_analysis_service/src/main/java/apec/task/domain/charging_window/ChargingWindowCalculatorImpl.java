package apec.task.domain.charging_window;

import apec.task.dto.carbon_intensity.FuelShare;
import apec.task.dto.carbon_intensity.GenerationDataEntry;
import apec.task.config.EnergyProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class ChargingWindowCalculatorImpl implements ChargingWindowCalculator {

    private final EnergyProperties energyProperties;
    @Override
    public double calculateWindowScore(List<GenerationDataEntry> data, int start, int windowSize) {
        return data.subList(start, start + windowSize)
                .stream()
                .mapToDouble(this::getGenerationGreenFuelsShare)
                .average()
                .orElseThrow();
    }

    @Override
    public int findBestWindowStartIndex(List<GenerationDataEntry> data, int windowSize) {
        double curSum = 0;
        for (int i = 0; i < windowSize; i++) {
            curSum += getGenerationGreenFuelsShare(data.get(i));
        }

        double bestSum = curSum;
        int bestIndex = 0;

        for (int i = 1; i <= data.size() - windowSize; i++) {
            curSum -= getGenerationGreenFuelsShare(data.get(i-1));
            curSum += getGenerationGreenFuelsShare(data.get(i + windowSize - 1));

            if (curSum > bestSum) {
                bestSum = curSum;
                bestIndex = i;
            }
        }

        return bestIndex;
    }

    private double getGenerationGreenFuelsShare(GenerationDataEntry generation) {
        return generation.generationmix().stream()
                .filter(x -> this.energyProperties.isGreenSource(x.fuel()))
                .mapToDouble(FuelShare::perc)
                .sum();
    }
}
