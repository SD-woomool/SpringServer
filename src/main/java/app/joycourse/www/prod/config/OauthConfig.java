package app.joycourse.www.prod.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Getter
@Configuration
@ConfigurationProperties(prefix = "oauth2")
public class OauthConfig {

    private Map<String, Provider> providers = new HashMap<>();

    public Map<String, Provider> getProviders() {
        return this.providers;
    }


    public void setProviders(Map<String, Provider> providers) {
        this.providers = providers;
    }

    @Getter
    @Setter
    public static class Provider {

        private String clientId;
        private String clientSecret;
        private String redirectUri;
        private String tokenUri;
        private String userInfoUri;
        private String loginUri;
        private String unlinkUri;

    }


}

