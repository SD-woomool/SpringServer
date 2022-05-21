package app.joycourse.www.prod.controller;

import app.joycourse.www.prod.dto.Response;
import app.joycourse.www.prod.service.auth.ManageAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "인증")
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
    @ApiOperation(
            value = "인증 정보를 지운다.",
            notes = "기존에 저장되어 있는 인증 정보를 지운다.\n" +
                    "- web: 쿠키에 있는 인증 정보를 지운다.\n" +
                    "- app: 현재는 앱은 인증 정보가 없다...",
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공")
    })
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
    @ApiOperation(
            value = "인증 정보를 갱신한다.",
            notes = "인증 정보를 갱신한다.\n" +
                    "- web: 쿠키에 있는 refresh token을 이용하여 인증 정보를 갱신한다.\n" +
                    "- app: 현재는 앱은 지원되지 않는다...",
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 정보가 없는 경우")
    })
    @PostMapping("/refresh")
    public Response<Void> refresh(HttpServletRequest request, HttpServletResponse response) {
        manageAuthService.refreshAuth(request, response);
        return new Response<>();
    }
}
