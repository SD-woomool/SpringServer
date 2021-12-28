package app.joycourse.www.prod.controller;

import app.joycourse.www.prod.Exception.CustomException;
import app.joycourse.www.prod.config.OauthConfig;
import app.joycourse.www.prod.constants.Constants;
import app.joycourse.www.prod.domain.User;
import app.joycourse.www.prod.dto.Response;
import app.joycourse.www.prod.dto.UserInfo;
import app.joycourse.www.prod.repository.AccountRepository;
import app.joycourse.www.prod.repository.JpaAccountRepository;
import app.joycourse.www.prod.service.AccountService;
import app.joycourse.www.prod.service.JwtService;
import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService service;
    private final JwtService jwtService;
    private final OauthConfig oauthConfig;
    private final AccountRepository accountRepository;

    public AccountController(OauthConfig oauthConfig, AccountService service, JpaAccountRepository jpaAccountRepository, JwtService jwtService) {
        this.oauthConfig = oauthConfig;
        this.service = service;
        this.accountRepository = jpaAccountRepository;
        this.jwtService = jwtService;
    }

    @GetMapping("/hello")
    @ResponseBody
    public String hello2() {
        System.out.println(oauthConfig.getProviders().keySet());
        return "hello";
    }

    @GetMapping("/{provider}/login")
    public void login(@PathVariable("provider") String provider, HttpServletResponse response)  throws IOException, RuntimeException{

        List<String> providerList = new ArrayList<>(oauthConfig.getProviders().keySet());
        RestTemplate rt = new RestTemplate();

        System.out.println(providerList);
        if (!providerList.contains(provider)) {
            throw new CustomException("INVALID_PROVIDER", CustomException.CustomError.INVALID_PROVIDER);
            //throw new RuntimeException();
        }
        OauthConfig.Provider providers = oauthConfig.getProviders().get(provider);
        String redirectUri = String.format("%s?response_type=code&client_id=%s&state=hello&redirect_uri=%s", providers.getLoginUri(), providers.getClientId(), providers.getRedirectUri());
        response.sendRedirect(redirectUri);

        //return "redirect:" + redirectUri;
    }

    @GetMapping("/{provider}/callback")
    @ResponseBody
    public Response<UserInfo> callback(@RequestParam(value = "code", required = false) String code, @PathVariable("provider") String provider, HttpServletResponse setCookieResponse) throws CustomException {
        Map<String, String> response = service.getToken(code, "hello", provider);
        String accessToken = response.get("access_token");
        String expiresIn = response.get("expires_in");
        String profileImageUrl = null;
        String nickname = null;
        if (accessToken == null || accessToken.isEmpty()) {
            String error = response.get("error");
            String errorDescription = response.get("error_description");
            throw new CustomException("GET_TOKEN_ERROR", CustomException.CustomError.GET_TOKEN_ERROR);
        }

        String userInfo = service.getUserInfo(accessToken, provider);
        String email = userInfo.split("\"email\":")[1].split("\"")[1];
        System.out.println(email);
        Optional<User> user = accountRepository.findByEmail(email);
        String jwtToken;
        boolean login;
        if (user.isPresent()) {
            jwtToken = jwtService.createToken("id:" + String.valueOf(user.get().getId()), Constants.getTtlMillis());
            nickname = user.get().getNickname();
            login = true;

        } else {
            jwtToken = jwtService.createToken("email:" + email, Constants.getTtlMillis());
            login = false;
        }
        Cookie jwtCookie = new Cookie(Constants.getCookieName(), jwtToken);
        jwtCookie.setMaxAge(3000);
        jwtCookie.setPath("/");
        setCookieResponse.addCookie(jwtCookie);
        UserInfo data = new UserInfo(login, email, profileImageUrl, nickname);

        return new Response<UserInfo>(data);
        //return UserInfo.builder().login(login).email(email).profileImageUrl(profileImageUrl).nickname(nickname).build();
    }

    @GetMapping("/nickname")
    @ResponseBody
    public Response<Map<String, Boolean>> checkNickname(@RequestParam(value = "nickname", required = false) String nickname){ // 닉네임 길이 체크 등등 추가할게 있나?
        Optional<User> user = accountRepository.findByNickname(nickname);
        Map<String, Boolean> data = new HashMap<>();
        if(user.isPresent() || nickname == null || nickname.length() < 3){
            data.put("login", false);
            data.put("check", false);
        }else{
            data.put("login", false);
            data.put("check", true);
        }
        return new Response<Map<String, Boolean>>(data);
    }

    @PostMapping("/")
    @Transactional
    @ResponseBody
    public Response<Map<String, String>> join(@RequestBody User userInfo) throws CustomException{ // 쿠키설정까지 해야함
        Optional<User> user = this.accountRepository.findByEmail(userInfo.getEmail());
        if(user.isPresent()){
            throw new CustomException("User is already exist", CustomException.CustomError.BAD_REQUEST);
        }
        User newUser = this.accountRepository.newUser(userInfo);
        Map<String, String> data = new HashMap<>();
        data.put("join", "true");
        data.put("login", "true");
        data.put("email", newUser.getEmail());
        return new Response<Map<String, String>>(data);
    }

}


