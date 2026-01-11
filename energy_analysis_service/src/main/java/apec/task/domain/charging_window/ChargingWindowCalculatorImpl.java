package apec.task.domain.charging_window;

import apec.task.dto.ChargingWindowResponseDto;
import apec.task.dto.FuelShare;
import apec.task.dto.GenerationDataEntry;
import apec.task.config.EnergyProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class ChargingWindowCalculatorImpl implements ChargingWindowCalculator {

    private final EnergyProperties energyProperties;

    @Override
    public ChargingWindowResponseDto getChargingWindow(List<GenerationDataEntry> data, int windowHours) {
        int windowSize = windowHours * 2;

        record WindowResult(int startIndex, double sum) {}

        WindowResult best = IntStream
                .range(0, data.size() - windowSize + 1)
                .mapToObj(i -> {
                    double sum = IntStream.range(0, windowSize)
                            .mapToDouble(j -> data.get(i + j)
                                    .generationmix().stream()
                                    .filter(s -> this.energyProperties.getGreenSources().contains(s.fuel()))
                                    .mapToDouble(FuelShare::perc)
                                    .findFirst()
                                    .orElse(0.0)
                            ).sum();
                    return new WindowResult(i, sum);
                })
                .max(Comparator.comparingDouble(WindowResult::sum))
                .orElseThrow();

        int bestIndex = best.startIndex();
        double maxSum = best.sum();

        LocalDateTime start = data.get(bestIndex).from();
        LocalDateTime end   = data.get(bestIndex + windowSize - 1).to();
        double avgPercentage = maxSum / windowSize;

        return new ChargingWindowResponseDto(start, end, avgPercentage);
    }
}
