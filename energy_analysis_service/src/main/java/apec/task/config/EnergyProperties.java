package apec.task.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
@ConfigurationProperties(prefix = "energy")
@Getter
@Setter
public class EnergyProperties {
    private Set<String> greenSources;

    public boolean isGreenSource(String fuel){
        return this.greenSources.contains(fuel);
    }
}
