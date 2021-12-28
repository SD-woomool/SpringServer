package app.joycourse.www.prod.filter;


import app.joycourse.www.prod.Exception.CustomException;
import app.joycourse.www.prod.domain.User;
import app.joycourse.www.prod.repository.AccountRepository;
import app.joycourse.www.prod.repository.JpaAccountRepository;
import app.joycourse.www.prod.service.JwtService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebFilter
public class AuthorizationFilter implements Filter {
    AccountRepository accountRepository;
    JwtService jwtService;

    public AuthorizationFilter(JpaAccountRepository jpaAccountRepository, JwtService service){
        this.accountRepository = jpaAccountRepository;
        this.jwtService = service;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException, CustomException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse rep = (HttpServletResponse)response;

        //final String tokenHeader = req.getHeader("token");

        Cookie[] cookies = req.getCookies();
        if(cookies == null){
            String target = req.getRequestURI();
            throw new CustomException("NO_COOKIE", CustomException.CustomError.UNAUTHORIZED);
        }

        int flag = 0;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("token")){ // token 말고 미리 지정해둔 이름 있음 그걸로 바꾸자
                try {
                    flag = 1;
                    String jwt = cookie.getValue();
                    Map<String, Object> payloadData = new HashMap<>();
                    payloadData = jwtService.getPayload(jwt);
                    Optional<User> user = accountRepository.findById(Long.valueOf(String.valueOf(payloadData.get("id"))));
                    if (user.isPresent()){
                        filterChain.doFilter(request, response);
                    }else{
                        throw new CustomException("NO_USER", CustomException.CustomError.UNAUTHORIZED); // dispatchServlet 지나기 전이라 IOException으로 해야할지도?
                    }
                }
                catch(NullPointerException e){
                    throw e;  //  필터 에러 꾸미고 다시 오자
                }


            }
        }
        if(flag == 0){
            throw new CustomException("INVALID_TOKEN", CustomException.CustomError.UNAUTHORIZED);
        }

    }


}
