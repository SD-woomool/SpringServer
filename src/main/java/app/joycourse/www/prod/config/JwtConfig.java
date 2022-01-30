package app.joycourse.www.prod.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("jwt")
public class JwtConfig {
    private String type;
    private String algorithm;
    private String secretKey;
    private Cookie cookie;

    @Getter
    @Setter
    public static class Cookie {
        private String name;
        private Integer ttlMillis;
    }
}
