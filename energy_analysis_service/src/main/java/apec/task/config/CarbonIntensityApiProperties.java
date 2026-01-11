package apec.task.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "carbon-intensity.api")
@Getter
@Setter
public class CarbonIntensityApiProperties {
    private String baseUrl;
}

