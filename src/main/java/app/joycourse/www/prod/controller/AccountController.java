package app.joycourse.www.prod.controller;


import app.joycourse.www.prod.config.JwtConfig;
import app.joycourse.www.prod.dto.Response;
import app.joycourse.www.prod.entity.User;
import app.joycourse.www.prod.exception.CustomException;
import app.joycourse.www.prod.repository.UserRepository;
import app.joycourse.www.prod.service.JwtService;
import app.joycourse.www.prod.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {
    private final UserService service;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final JwtConfig jwtConfig;

    @GetMapping("/nickname")
    @ResponseBody
    public Response<Map<String, Boolean>> checkNickname(@RequestParam(value = "nickname", required = false) String nickname) { // 닉네임 길이 체크 등등 추가할게 있나?
        Optional<User> user = userRepository.findByNickname(nickname);
        Map<String, Boolean> data = new HashMap<>();
        if (user.isPresent() || nickname == null || nickname.length() < 3) {
            data.put("login", false);
            data.put("check", false);
        } else {
            data.put("login", false);
            data.put("check", true);
        }
        return new Response<>(data);
    }

    @PostMapping("/logout")
    @ResponseBody
    public void logout(HttpServletRequest request, HttpServletResponse response) { // response가 있나?  여기 권한체크
        System.out.println("###logout works####");
        this.service.deleteCookie(response);
    }

    @PutMapping("/")
    @ResponseBody
    public Response<Map<String, Boolean>> edit(@RequestBody User userInfo, HttpServletRequest request) {
        try {
            Optional<User> optionalUser = Optional.ofNullable((User) request.getAttribute("user"));
            User user = optionalUser.orElseThrow(() -> new CustomException("NO_USER", CustomException.CustomError.MISSING_PARAMETERS));

            service.updateUser(user, userInfo);
        } catch (ClassCastException e) {
            throw new CustomException("UPDATE_ERROR", CustomException.CustomError.SERVER_ERROR);
        }
        Map<String, Boolean> data = new HashMap<>();
        data.put("edit", true);
        return new Response<>(data);
    }
}
