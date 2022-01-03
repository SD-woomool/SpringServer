package app.joycourse.www.prod.service;

import app.joycourse.www.prod.constants.Constants;
import app.joycourse.www.prod.dto.OauthToken;
import app.joycourse.www.prod.exception.CustomException;
import app.joycourse.www.prod.config.OauthConfig;
import app.joycourse.www.prod.domain.User;
import app.joycourse.www.prod.repository.AccountRepository;
import app.joycourse.www.prod.repository.JpaAccountRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    public String getAccessToken(String provider, String code, String state) {
        OauthConfig.Provider providerConfig = oauthconfig.getProviders().get(provider);
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

    public Optional<User> getUserByEmail(String email) {
        return accountRepository.findByEmail(email);
    }


    public User saveUser(User userInfo){
        String email = userInfo.getEmail();
        if (email == null){
            throw new CustomException("Email is missing", CustomException.CustomError.MISSING_PARAMETERS);
        }
        userInfo.setCreateAt();
        Optional<User> user = this.accountRepository.findByEmail(userInfo.getEmail());
        if(user.isPresent()){
            throw new CustomException("User is already exist", CustomException.CustomError.BAD_REQUEST);
        }
        User newUser = this.accountRepository.newUser(userInfo);
        return newUser;
    }

    public void deleteCookie(HttpServletResponse response){
        Cookie jwtCookie = new Cookie(Constants.getCookieName(), null);
        jwtCookie.setMaxAge(0);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);
    }
}
