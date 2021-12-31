package app.joycourse.www.prod.service;

import app.joycourse.www.prod.Exception.CustomException;
import app.joycourse.www.prod.config.OauthConfig;
import app.joycourse.www.prod.domain.User;
import app.joycourse.www.prod.repository.AccountRepository;
import app.joycourse.www.prod.repository.JpaAccountRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class AccountService {
    OauthConfig oauthconfig;
    RestTemplate restTemplate;
    AccountRepository accountRepository;


    public AccountService(OauthConfig oauthConfig, RestTemplate restTemplate, JpaAccountRepository jpaAccountRepository) {
        this.oauthconfig = oauthConfig;
        this.restTemplate = restTemplate;
        this.accountRepository = jpaAccountRepository;
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

    public User saveUser(User userInfo){
        Optional<User> user = this.accountRepository.findByEmail(userInfo.getEmail());
        if(user.isPresent()){
            throw new CustomException("User is already exist", CustomException.CustomError.BAD_REQUEST);
        }
        User newUser = this.accountRepository.newUser(userInfo);
        return newUser;
    }
}