package app.joycourse.www.prod.exception;


import app.joycourse.www.prod.dto.Response;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    String error;
    String errorDescription;

    @ExceptionHandler({CustomException.class})
    public Response<Map<String, Integer>> customExceptionHandler(CustomException e) {
        this.errorDescription = e.getMessage();
        this.error = e.getCustomError().getError();
        Map<String, Integer> status = new HashMap<>();
        status.put("status", e.getCustomError().getStatus());

        if (e.getMessage().equals("")) {
            this.errorDescription = null;
        }
        return new Response<Map<String, Integer>>(this.error, this.errorDescription, status);
    }

    @ExceptionHandler({RuntimeException.class})
    public Response<Map<String, Integer>> RuntimeException(Exception e){
        this.errorDescription = e.getMessage();
        this.error = "SERVER_ERROR";
        Map<String, Integer> status = new HashMap<>();
        status.put("status", 500);
        return new Response<Map<String, Integer>>(this.error, this.errorDescription, status);
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    public Response<Map<String, Integer>> noHandlerFoundException(Exception e) {
        CustomException customException = new CustomException("PAGE_NOT_FOUND", CustomException.CustomError.PAGE_NOT_FOUND);
        this.errorDescription = customException.getMessage();
        this.error = customException.getCustomError().getError();
        Map<String, Integer> status = new HashMap<>();
        status.put("status", customException.getCustomError().getStatus());

        return new Response<Map<String, Integer>>(this.error, this.errorDescription, status);
    }

    @ExceptionHandler({AuthenticationException.class, AccessDeniedException.class})
    public Response<Map<String, Integer>> wrongAccessExceptionHandler(Exception e) {
        this.error = "UNAUTHORIZED";
        this.errorDescription = "UNAUTHORIZED";
        Map<String, Integer> status = new HashMap<>();
        status.put("status", 401);

        return new Response<Map<String, Integer>>(this.error, this.errorDescription, status);
    }

    @ExceptionHandler({Exception.class})
    public Response<Map<String, Integer>> exceptionHandler(Exception e) {
        this.error = "SERVER_ERROR";
        this.errorDescription = "IOException";//e.getMessage();
        Map<String, Integer> status = new HashMap<>();
        status.put("status", 500);

        return new Response<Map<String, Integer>>(this.error, this.errorDescription, status);
    }
}
