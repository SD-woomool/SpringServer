package app.joycourse.www.prod.controller;

import app.joycourse.www.prod.dto.Response;
import app.joycourse.www.prod.service.auth.ManageAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final ManageAuthService manageAuthService;

    /**
     * web인경우 cookie 초기화하여 로그아웃 한다.
     *
     * @param request  auth정보를 지우기 위해 사용
     * @param response auth정보를 지우기 위해 사용
     * @return {status: 200}
     */
    @PostMapping("/logout")
    public Response<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        manageAuthService.clearAuth(request, response);
        return new Response<>();
    }

    /**
     * refresh token을 이용하여 access token을 재발급 받는다.
     *
     * @param request  refresh token 가져오기 위해서
     * @param response access token 저장하기 위해서
     * @return {status: 200}
     */
    @PostMapping("/refresh")
    public Response<Void> refresh(HttpServletRequest request, HttpServletResponse response) {
        manageAuthService.refreshAuth(request, response);
        return new Response<>();
    }
}
