package app.joycourse.www.prod.filter;

import app.joycourse.www.prod.dto.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//@Component
//@WebFilter("/*")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    public ExceptionHandlerFilter(){
        System.out.println("################ExceptionFilter is Running!!##################");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException{
        try{
            System.out.println("################ExceptionFilter is Running!!##################");
            filterChain.doFilter(request, response);
        }
        catch(IOException e){
            setErrorResponse(response, e);
        }
        catch(ServletException e){
            setErrorResponse(response, e);
        }
        catch(Exception e){
            setErrorResponse(response, e);
        }

    }

    public void setErrorResponse(HttpServletResponse response, Throwable e) throws IOException{
        response.setStatus(500);
        response.setContentType("application/json");
        String errorMessage = e.getMessage();
        Response errorResponse = new Response("SERVER_ERROR", errorMessage);
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(errorResponse));
        try{
            response.getWriter().write(mapper.writeValueAsString(errorResponse));
        }
        catch (IOException ex){
            throw ex;
        }
    }
}
