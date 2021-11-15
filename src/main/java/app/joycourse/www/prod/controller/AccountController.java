package app.joycourse.www.prod.controller;

import app.joycourse.www.prod.config.OauthConfig;
import app.joycourse.www.prod.constants.Constants;
import app.joycourse.www.prod.domain.User;
import app.joycourse.www.prod.dto.UserInfo;
import app.joycourse.www.prod.repository.JpaAccountRepository;
import app.joycourse.www.prod.service.AccountService;
import app.joycourse.www.prod.service.JwtService;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.util.*;

@Controller
@RequestMapping("/accounts")
public class AccountController {
    private AccountService service;
    private JwtService jwtService;
    private OauthConfig oauthConfig;
    private JpaAccountRepository jpaAccountRepository;

    public AccountController(OauthConfig oauthConfig, AccountService service, JpaAccountRepository jpaAccountRepository, JwtService jwtService) {
        this.oauthConfig = oauthConfig;
        this.service = service;
        this.jpaAccountRepository = jpaAccountRepository;
        this.jwtService = jwtService;
    }

    @GetMapping("/hello")
    @ResponseBody
    public String hello2() {
        System.out.println(oauthConfig.getProviders().keySet());
        return "hello";
    }

    @GetMapping("/{provider}/login")
    public String login(@PathVariable("provider") String provider) {

        List<String> providerList = new ArrayList<>(oauthConfig.getProviders().keySet());
        RestTemplate rt = new RestTemplate();

        System.out.println(providerList);
        if (!providerList.contains(provider)) {
            System.out.println("error");
        }
        OauthConfig.Provider providers = oauthConfig.getProviders().get(provider);
        String redirectUri = String.format("%s?response_type=code&client_id=%s&state=hello&redirect_uri=%s", providers.getLoginUri(), providers.getClientId(), providers.getRedirectUri());

        return "redirect:" + redirectUri;
    }

    @GetMapping("/{provider}/callback")
    @ResponseBody
    public UserInfo callback(@RequestParam(value = "code", required = false) String code, @PathVariable("provider") String provider, HttpServletResponse setCookieResponse) throws HttpException {
        Map<String, String> response = service.getToken(code, "hello", provider);
        String accessToken = response.get("access_token");
        String expiresIn = response.get("expires_in");
        String profileImageUrl = null;
        String nickname = null;
        if (accessToken == null || accessToken.isEmpty()) {
            String error = response.get("error");
            String errorDescription = response.get("error_description");
            throw new HttpException("이메일 없어요");
        }

        String userInfo = service.getUserInfo(accessToken, provider);
        String email = userInfo.split("\"email\":")[1].split("\"")[1];
        System.out.println(email);
        Optional<User> user = jpaAccountRepository.findByEmail(email);
        String jwtToken;
        boolean login;
        if (user.isPresent()) {
            jwtToken = jwtService.createToken(user.get().getId().toString(), Constants.getTtlMillis());
            nickname = user.get().getNickname();
            login = true;

        } else {
            jwtToken = jwtService.createToken(email, Constants.getTtlMillis());
            login = false;
        }
        Cookie jwtCookie = new Cookie(Constants.getCookieName(), jwtToken);
        jwtCookie.setMaxAge(3000);
        jwtCookie.setPath("/");
        setCookieResponse.addCookie(jwtCookie);

        return UserInfo.builder().login(login).email(email).profileImageUrl(profileImageUrl).nickname(nickname).build();
    }

}
