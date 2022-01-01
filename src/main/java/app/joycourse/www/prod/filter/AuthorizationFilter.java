package app.joycourse.www.prod.filter;


import app.joycourse.www.prod.domain.User;
import app.joycourse.www.prod.repository.AccountRepository;
import app.joycourse.www.prod.repository.JpaAccountRepository;
import app.joycourse.www.prod.service.JwtService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@WebFilter("/*")
//@Order(1)
public class AuthorizationFilter extends OncePerRequestFilter {
    AccountRepository accountRepository;
    JwtService jwtService;



    public AuthorizationFilter(JpaAccountRepository jpaAccountRepository, JwtService service){
        this.accountRepository = jpaAccountRepository;
        this.jwtService = service;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException, RuntimeException {


        //final String tokenHeader = req.getHeader("token");

        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            String target = request.getRequestURI();
            throw new ServletException("NO_TOKEN");
        }

        int flag = 0;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("token")){ // token 말고 미리 지정해둔 이름 있음 그걸로 바꾸자
                try {
                    flag = 1;
                    String jwt = cookie.getValue();
                    Map<String, Object> payloadData = new HashMap<>();
                    payloadData = jwtService.getPayload(jwt);
                    System.out.println("#########"+String.valueOf(payloadData.get("id")) + "##########");
                    Optional<User> user = accountRepository.findById(Long.valueOf(String.valueOf(payloadData.get("id")))); // 유저를 못찾는중
                    if (user.isPresent()){
                        filterChain.doFilter(request, response);
                    }else{
                        throw new ServletException("NO_USER"); // dispatchServlet 지나기 전이라 IOException으로 해야할지도?
                    }
                }
                catch(NullPointerException e){
                    throw e;  //  필터 에러 꾸미고 다시 오자
                }


            }
        }
        if(flag == 0){
            throw new RuntimeException("INVALID_TOKEN");
        }

    }


}
