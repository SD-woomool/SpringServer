package app.joycourse.www.prod.service.auth;

import app.joycourse.www.prod.config.EndpointConfig;
import app.joycourse.www.prod.entity.Provider;
import app.joycourse.www.prod.entity.User;
import app.joycourse.www.prod.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final EndpointConfig endpointConfig;

    @Transactional
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        Provider provider = Provider.valueOf(authToken.getAuthorizedClientRegistrationId().toUpperCase());
        String uid = provider.name() + "_" + authToken.getPrincipal().getAttribute(authToken.getPrincipal().getName());

        Optional<User> savedUser = userRepository.findByUid(uid);
        if (savedUser.isEmpty()) {
            // DB에 존재하지 않는 경우 생성
            User user = new User();
            user.setUid(uid);
            user.setProvider(provider);
            userRepository.newUser(user);
        }

        Cookie cookie = new Cookie("userId", uid);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(3600); // 1hour

        response.addCookie(cookie);

        getRedirectStrategy().sendRedirect(request, response, endpointConfig.getRedirect().get("web"));
    }
}
