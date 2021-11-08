package app.joycourse.www.prod.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Controller
public class AccountController {

    @GetMapping("/accounts/{provider}/login")
    public String login(@RequestParam String provider){
        provider = provider.toLowerCase();
        List<String> providerList = Arrays.asList("naver", "kakao", "google");
        if(!providerList.contains(provider)){
            // 에러 띄우기
            System.out.println("error");
        }


        return "redirect";
    }
}
