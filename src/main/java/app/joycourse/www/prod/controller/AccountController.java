package app.joycourse.www.prod.controller;

import app.joycourse.www.prod.config.OauthConfig;
import app.joycourse.www.prod.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.*;

@Controller
@RequestMapping("/accounts")
public class AccountController {
    private AccountService service;
    private OauthConfig oauthConfig;
    public AccountController(OauthConfig oauthConfig, AccountService service){
        this.oauthConfig = oauthConfig;
        this.service = service;
    }

    @GetMapping("/hello")
    @ResponseBody
    public String hello2(){
        System.out.println(oauthConfig.getProviders().keySet());
        return "hello";
    }

    @GetMapping("/{provider}/login")
    public String login(@PathVariable("provider") String provider){

        List<String> providerList = new ArrayList<>(oauthConfig.getProviders().keySet());
        RestTemplate rt = new RestTemplate();

        System.out.println(providerList);
        if(!providerList.contains(provider)) {
            System.out.println("error");
        }
        OauthConfig.Provider providers = oauthConfig.getProviders().get(provider);
        String redirectUri = String.format("%s?response_type=code&client_id=%s&state=hello&redirect_uri=%s", providers.getLoginUri(), providers.getClientId(), providers.getRedirectUri());

        return "redirect:" + redirectUri;
    }

    @GetMapping("/{provider}/callback")
    @ResponseBody
    public String callback(@RequestParam(value="code", required = false) String code, @PathVariable("provider") String provider){
        Map<String, String> response = service.getToken(code, "hello", provider);
        String accessToken = response.get("access_token");
        String expiresIn = response.get("expires_in");
        if(accessToken == null || accessToken.isEmpty()){
            String error = response.get("error");
            String errorDescription = response.get("error_description");
            return error + ": " + errorDescription;
        }
        System.out.println(accessToken);

        String userInfo = service.getUserInfo(accessToken, provider);
        System.out.println(userInfo);
        String email = userInfo.split("\"email\":")[1].split("\"")[1];
        System.out.println(email);

        return "hello";
    }

}
