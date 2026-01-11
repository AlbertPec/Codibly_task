package apec.task.client;

import apec.task.dto.GenerationResponse;
import apec.task.utils.DateTimeUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

@Service
public class CarbonIntensityClient {

    private final WebClient webClient;

    public CarbonIntensityClient(WebClient carbonWebClient) {
        this.webClient = carbonWebClient;
    }

    public GenerationResponse getGeneration(LocalDateTime from, LocalDateTime to) {

        return webClient.get()
                .uri("/generation/" + DateTimeUtil.toUtcIsoString(from) + "/" + DateTimeUtil.toUtcIsoString(to))
                .retrieve()
                .bodyToMono(GenerationResponse.class)
                .block();
    }
}
