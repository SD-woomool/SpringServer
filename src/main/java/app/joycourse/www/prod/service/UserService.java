package app.joycourse.www.prod.service;

import app.joycourse.www.prod.config.JwtConfig;
import app.joycourse.www.prod.entity.User;
import app.joycourse.www.prod.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final JwtConfig jwtConfig;

    public void deleteCookie(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie(jwtConfig.getCookie().getName(), null);
        jwtCookie.setMaxAge(0);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);
    }

    public void updateUser(User user, User userInfo) {
        userRepository.updateUser(user, userInfo);
    }
}
