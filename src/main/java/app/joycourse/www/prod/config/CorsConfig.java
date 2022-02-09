package app.joycourse.www.prod.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Setter
@Configuration
@ConfigurationProperties("cors")
public class CorsConfig {
    private String allowedOrigins;
    private String allowedMethods;
    private String allowedHeaders;
    private Long maxAge;

    public List<String> getAllowedOrigins() {
        return Arrays.asList(allowedOrigins.split(","));
    }

    public List<String> getAllowedMethods() {
        return Arrays.asList(allowedMethods.split(","));
    }

    public List<String> getAllowedHeaders() {
        return Arrays.asList(allowedHeaders.split(","));
    }

    public Long getMaxAge() {
        return maxAge;
    }
}
