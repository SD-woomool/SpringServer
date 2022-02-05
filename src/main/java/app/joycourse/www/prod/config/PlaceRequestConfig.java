package app.joycourse.www.prod.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("place-api")
public class PlaceRequestConfig {

    private RequestParameter requestParameter;

    @Getter
    @Setter
    public static class RequestParameter {
        private String requestUri;
        private String restApiKey;
    }

}
