package apec.task.domain.energy_mix;

import apec.task.config.EnergyProperties;
import apec.task.dto.FuelShare;
import apec.task.dto.GenerationDataEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EnergyMixCalculatorImpl implements EnergyMixCalculator{

    private final EnergyProperties energyProperties;

    public double calculateAverageDailyGreenShare(List<GenerationDataEntry> data){
        return data.stream()
                .mapToDouble(x -> x.generationmix()
                        .stream()
                        .filter(s -> this.energyProperties.getGreenSources().contains(s.fuel()))
                        .mapToDouble(FuelShare::perc)
                        .findFirst()
                        .orElse(0.0))
                .average()
                .orElse(0.0);
    }

    public List<FuelShare> calculateAverageFuelsShare(List<GenerationDataEntry> data){
        return data.stream()
                .flatMap(entry -> entry.generationmix().stream())
                .collect(Collectors.groupingBy(
                        FuelShare::fuel,
                        Collectors.averagingDouble(FuelShare::perc)
                ))
                .entrySet()
                .stream()
                .map(e -> new FuelShare(e.getKey(), e.getValue()))
                .toList();
    }

}
