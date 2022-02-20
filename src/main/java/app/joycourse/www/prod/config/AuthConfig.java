package app.joycourse.www.prod.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("auth")
public class AuthConfig {
    private String accessTokenCookieName;
    private String refreshTokenCookieName;

    private String accessTokenKey;
    private String refreshTokenKey;

    private int accessTokenMaxAge;
    private int refreshTokenMaxAge;
}
