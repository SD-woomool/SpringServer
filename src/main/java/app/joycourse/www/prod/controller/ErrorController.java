package app.joycourse.www.prod.controller;


import app.joycourse.www.prod.Exception.CustomException;
import app.joycourse.www.prod.dto.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@Controller
@ControllerAdvice
@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler
    public Response IoExceptionHandler(CustomException e){
        String errorDescription = e.getCustomError().getError();
        String error = e.toString();
        if(e.getMessage() != ""){
            errorDescription = e.getMessage();
        }
        return new Response(error, errorDescription);
    }
}
