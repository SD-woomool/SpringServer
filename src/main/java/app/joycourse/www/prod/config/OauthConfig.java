package app.joycourse.www.prod.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties("oauth2")
public class OauthConfig {
    @Getter
    @Setter
    public static class Provider {
        private String clientId;
        private String clientSecret;
        private String redirectUri;
        private String loginUri;
        private String tokenUri;
        private String userInfoUri;
        private String unlinkUri;
    }

    private Map<String, Provider> providers = new HashMap<>();

}
