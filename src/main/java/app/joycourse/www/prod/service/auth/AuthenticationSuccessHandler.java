package app.joycourse.www.prod.service.auth;

import app.joycourse.www.prod.config.EndpointConfig;
import app.joycourse.www.prod.entity.UserAgent;
import app.joycourse.www.prod.entity.auth.Auth;
import app.joycourse.www.prod.service.UserService;
import app.joycourse.www.prod.util.UserAgentUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final EndpointConfig endpointConfig;
    private final AuthService authService;
    private final UserService userService;
    private final ManageAuthService manageAuthService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Auth auth = authService.saveAuth(authentication);
        // auth의 uid 기반으로 user 동기화한다. authorization도 진행. - 향후 msa 환경이 되면 이 부분은 통신으로 바뀔 예정
        Boolean isBlocked = userService.syncUser(auth.getUid());

        String targetUrl;

        // web과 app을 구분지어서 작동 현재는 web만 존재한다. 추후에 app 어떤식으로 token 줄지 결정
        UserAgent userAgent = UserAgentUtil.parse(request.getHeader(HttpHeaders.USER_AGENT));

        if (!isBlocked) {
            // 정상 유저인 경우
            targetUrl = Optional.ofNullable(endpointConfig.getRedirect().get(userAgent.getType().toLowerCase())).orElse(endpointConfig.getRedirect().get("web"));
            // 정상 유저이면 auth 정보도 전달해준다.
            manageAuthService.deliverAuth(request, response, auth);
        } else {
            // 유저가 block 되어서 사용하지 못하는 경우
            targetUrl = Optional.ofNullable(endpointConfig.getRedirect().get(userAgent.getType().toLowerCase() + "Failure")).orElse(endpointConfig.getRedirect().get("webFailure")) + "?error=BLOCKED_USER";
            // auth 정보 삭제
            manageAuthService.clearAuth(request, response);
        }

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
