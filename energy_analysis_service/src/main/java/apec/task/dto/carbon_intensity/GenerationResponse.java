package apec.task.dto.carbon_intensity;

import apec.task.dto.carbon_intensity.GenerationDataEntry;

import java.util.List;

public record GenerationResponse(List<GenerationDataEntry> data) {
}
