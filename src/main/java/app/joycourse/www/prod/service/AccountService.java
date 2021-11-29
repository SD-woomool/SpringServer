package app.joycourse.www.prod.service;

import app.joycourse.www.prod.config.OauthConfig;
import app.joycourse.www.prod.domain.User;
import app.joycourse.www.prod.dto.OauthToken;
import app.joycourse.www.prod.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.html.Option;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountService {
    private OauthConfig oauthConfig;
    private RestTemplate restTemplate;
    private AccountRepository repository;

    public String getAccessToken(String provider, String code, String state) {
        OauthConfig.Provider providerConfig = oauthConfig.getProviders().get(provider);
        HttpHeaders headers = new HttpHeaders();
        headers.add("grant_type", "authorization_code");
        headers.add("client_id", providerConfig.getClientId());
        headers.add("client_secret", providerConfig.getClientSecret());
        headers.add("code", code);
        headers.add("state", state);
        headers.add("redirect_uri", providerConfig.getRedirectUri());

        String accessToken = null;
        try {
            ResponseEntity<OauthToken> oauthResult = restTemplate.postForEntity(providerConfig.getTokenUri(), headers, OauthToken.class);
            accessToken = oauthResult.getBody().getAccessToken();
        } catch (HttpClientErrorException e) {
            return null;
        }
        return accessToken;
    }

    public String getEmail(String provider, String accessToken) {
        OauthConfig.Provider providerConfig = oauthConfig.getProviders().get(provider);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<Object> entity = new HttpEntity<>(null, headers);
        HttpMethod method = provider.equals("google") ? HttpMethod.GET : HttpMethod.POST;

        String email = null;
        try {
            ResponseEntity<String> infoResult = restTemplate.exchange(providerConfig.getUserInfoUri(), method, entity, String.class);
            String jsonString = infoResult.getBody();
            if (Objects.isNull(jsonString)) {
                return null;
            }
            jsonString = jsonString.replaceAll("\\s", "");
            int index = jsonString.indexOf("\"email\":");
            if (index == -1) {
                return null;
            }
            index += 9;
            int lastIndex = jsonString.indexOf("\"", index);
            email = jsonString.substring(index, lastIndex);
        } catch (HttpClientErrorException e) {
            return null;
        }
        return email;
    }

    public Optional<User> getUserByEmail(String email) {
        return repository.findByEmail(email);
    }
}
