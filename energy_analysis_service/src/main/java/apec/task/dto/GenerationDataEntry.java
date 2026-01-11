package apec.task.dto;

import java.time.LocalDateTime;
import java.util.List;

public record GenerationDataEntry(
        LocalDateTime from,
        LocalDateTime to,
        List<FuelShare> generationmix
) {
}
