package app.joycourse.www.prod.service.auth;

import app.joycourse.www.prod.config.EndpointConfig;
import app.joycourse.www.prod.entity.UserAgent;
import app.joycourse.www.prod.util.UserAgentUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final EndpointConfig endpointConfig;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        UserAgent userAgent = UserAgentUtil.parse(request.getHeader(HttpHeaders.USER_AGENT));

        // web이냐 app이냐에 따라 redirection 다르게
        String failureUrl = Optional.ofNullable(endpointConfig.getRedirect().get(userAgent.getType().toLowerCase() + "Failure")).orElse(endpointConfig.getRedirect().get("webFailure"));

        getRedirectStrategy().sendRedirect(request, response, failureUrl);
    }
}
