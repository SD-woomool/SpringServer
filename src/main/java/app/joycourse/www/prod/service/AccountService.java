package app.joycourse.www.prod.service;

import app.joycourse.www.prod.config.OauthConfig;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService {
    OauthConfig oauthconfig;

    RestTemplate restTemplate;


    public AccountService(OauthConfig oauthConfig, RestTemplate restTemplate) {
        this.oauthconfig = oauthConfig;
        this.restTemplate = restTemplate;

    }

    public Map<String, String> getToken(String code, String state, String provider) {
        OauthConfig.Provider providers = oauthconfig.getProviders().get(provider);
        String uri = String.format("%s?grant_type=authorization_code&code=%s&client_id=%s&client_secret=%s&state=%s",
                providers.getTokenUri(), code, providers.getClientId(), providers.getClientSecret(), state);
        return restTemplate.getForEntity(uri, Map.class).getBody();
    }

    public String getUserInfo(String accessToken, String provider) {
        OauthConfig.Provider providers = oauthconfig.getProviders().get(provider);
        String uri = providers.getUserInfoUri();
        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity(headers);
        headers.add("Authorization", "Bearer " + accessToken);
        return restTemplate.exchange(uri, HttpMethod.GET, entity, String.class).getBody();
    }
}