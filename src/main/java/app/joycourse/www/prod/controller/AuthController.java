package app.joycourse.www.prod.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @PostMapping("/logout")
    @ResponseBody
    public void logout(HttpServletRequest request, HttpServletResponse response) { // response가 있나?  여기 권한체크
        System.out.println("###logout works####");
    }

}
