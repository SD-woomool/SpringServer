package app.joycourse.www.prod.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(OauthProperties.class)
public class OauthConfig {
    private final OauthProperties properties;

    public OauthConfig(OauthProperties properties){
        this.properties = properties;
    }
}
