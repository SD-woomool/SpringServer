package app.joycourse.www.prod.controller;


import app.joycourse.www.prod.annotation.AuthorizationUser;
import app.joycourse.www.prod.dto.Response;
import app.joycourse.www.prod.entity.user.User;
import app.joycourse.www.prod.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Response<Void> me(@AuthorizationUser User user) {
        return new Response<>();
    }
}
