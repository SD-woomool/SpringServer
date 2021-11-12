package app.joycourse.www.prod.controller;

import app.joycourse.www.prod.config.OauthConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Array;
import java.util.*;

@Controller
public class AccountController {

    OauthConfig oauthConfig;
    public AccountController(OauthConfig oauthConfig){
        this.oauthConfig = oauthConfig;
    }

    @GetMapping("/hello")
    @ResponseBody
    public String hello2(){
        System.out.println(oauthConfig.getProviders().keySet());
        return "hello";
    }

    @GetMapping("/accounts/{provider}/login")
    @ResponseBody
    public String hello(){
        System.out.println(oauthConfig.getProviders().keySet());
        return "hello";
    }
/*
    @GetMapping("/accounts/{provider}/login")
    public String login(@PathVariable("provider") String provider){
        //oauthConfig.getProvider().keySet();
        provider = provider.toLowerCase();
        //List<String> providerList = Arrays.asList("naver", "kakao", "google");

        List<String> providerList = new ArrayList<>(oauthConfig.getProvider().keySet());
        System.out.println("hi");
        System.out.println(oauthConfig.getProvider().keySet());
        System.out.println(providerList);
        //if(!providerList.contains(provider)){
            // 에러 띄우기
         //   System.out.println("error");
        //}


        return "redirect";
    }*/
}
