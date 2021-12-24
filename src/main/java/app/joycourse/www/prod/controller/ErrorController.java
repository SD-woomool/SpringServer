package app.joycourse.www.prod.controller;


import app.joycourse.www.prod.Exception.CustomException;
import app.joycourse.www.prod.dto.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@ControllerAdvice
@RestControllerAdvice
@EnableWebMvc
public class ErrorController {

    String error;
    String errorDescription;

    @ExceptionHandler
    public Response<Map<String, Integer>> customExceptionHandler(CustomException e){
        this.errorDescription = e.getMessage();
        this.error = e.getCustomError().getError();
        Map<String ,Integer> status = new HashMap<>();
        status.put("status", e.getCustomError().getStatus());

        if(e.getMessage() == ""){
            this.errorDescription = null;
        }
        return new Response<Map<String, Integer>>(this.error, this.errorDescription, status);
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    public Response<Map<String, Integer>> noHandlerFoundException(Exception e){
        CustomException customException = new CustomException("PAGE_NOT_FOUND", CustomException.CustomError.PAGE_NOT_FOUND);
        this.errorDescription = customException.getMessage();
        this.error = customException.getCustomError().getError();
        Map<String, Integer> status = new HashMap<>();
        status.put("status", customException.getCustomError().getStatus());

        return new Response<Map<String, Integer>>(this.error, this.errorDescription, status);
    }

    @ExceptionHandler
    public Response exceptionHandler(Exception e){
        this.error = "IOException";
        this.errorDescription = e.getMessage();

        if(this.errorDescription == ""){
            this.errorDescription = null;
        }
        return new Response(this.error, this.errorDescription);
    }
}
