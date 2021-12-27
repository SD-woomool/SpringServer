package app.joycourse.www.prod.filter;


import app.joycourse.www.prod.Exception.CustomException;
import app.joycourse.www.prod.domain.User;
import app.joycourse.www.prod.repository.AccountRepository;
import app.joycourse.www.prod.repository.JpaAccountRepository;

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

//@WebFilter
public class AuthorizationFilter implements Filter {
    AccountRepository accountRepository;

    public AuthorizationFilter(JpaAccountRepository jpaAccountRepository){
        this.accountRepository = jpaAccountRepository;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException, CustomException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse rep = (HttpServletResponse)response;

        Cookie[] cookies = req.getCookies();
        if(cookies == null){
            String target = req.getRequestURI();
            throw new CustomException("NO_COOKIE", CustomException.CustomError.UNAUTHORIZED);
        }

        int flag = 0;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("token")){
                flag = 1;
                Base64.Decoder decoder = Base64.getDecoder();
                String jwt = cookie.getValue();
                System.out.println(jwt);
                String data = jwt.split("\\.")[1]; // 디코딩이 문제있음
                String decodeValue = new String(decoder.decode(data));
                //decodeValue = decoder.decode(data)[0];
                Optional<User> user = accountRepository.findById(decodeValue);
                if (user.isPresent()){
                    filterChain.doFilter(request, response);
                }else{
                    throw new CustomException("NO_USER", CustomException.CustomError.UNAUTHORIZED); // dispatchServlet 지나기 전이라 IOException으로 해야할지도?
                }
            }
        }
        if(flag == 0){
            throw new CustomException("INVALID_TOKEN", CustomException.CustomError.UNAUTHORIZED);
        }

    }


}
