package app.joycourse.www.prod.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties("endpoint")
public class EndpointConfig {
    private Map<String, List<String>> authenticate;
    private Map<String, List<String>> nonAuthenticate;
    private Map<String, String> redirect;
}
