package app.joycourse.www.prod.filter;


import app.joycourse.www.prod.entity.User;
import app.joycourse.www.prod.repository.UserRepository;
import app.joycourse.www.prod.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

//@WebFilter({"/accounts/logout/", "/accounts/", "/course/", "/course/my-course"})
@Order(1)
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException, RuntimeException {
        System.out.println("authorizationFilter is running!!!");

        if (request.getMethod().equalsIgnoreCase("POST") && request.getRequestURI().equals("/accounts/")) {
            filterChain.doFilter(request, response);
            return;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            String target = request.getRequestURI();
            throw new ServletException("NO_TOKEN");
        }

        int flag = 0;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) { // token 말고 미리 지정해둔 이름 있음 그걸로 바꾸자
                try {
                    flag = 1;
                    String jwt = cookie.getValue();
                    Map<String, Object> payloadData = jwtService.getPayload(jwt);
                    System.out.println("#########" + payloadData.get("id") + "##########");
                    Optional<User> user = userRepository.findBySeq(Long.valueOf(String.valueOf(payloadData.get("id"))));
                    if (user.isPresent()) {
                        request.setAttribute("user", user.orElse(null));
                        filterChain.doFilter(request, response);
                    } else {
                        throw new ServletException("NO_USER");
                    }
                } catch (NullPointerException e) {
                    throw e;  //  필터 에러 꾸미고 다시 오자
                }
            }
        }
        if (flag == 0) {
            throw new RuntimeException("INVALID_TOKEN");
        }
    }
}
