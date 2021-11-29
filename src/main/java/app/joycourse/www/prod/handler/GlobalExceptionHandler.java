package app.joycourse.www.prod.handler;

import app.joycourse.www.prod.dto.Response;
import app.joycourse.www.prod.exception.CustomException;
import jdk.jshell.spi.ExecutionControlProvider;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public Response<Object> handleCustomException(CustomException e) {
        return new Response<>(e.getErrorMessage(), e.getErrorStatus());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public Response<Object> handleNotFoundException(NoHandlerFoundException e) {
        CustomException.CustomError error = CustomException.CustomError.PAGE_NOT_FOUND;
        return new Response<>(error.getMessage(), error.getStatus());
    }

    @ExceptionHandler
    public Response<Object> handleException(Exception e) {
        CustomException.CustomError error = CustomException.CustomError.SERVER_ERROR;
        return new Response<>(error.getMessage(), error.getStatus());
    }
}
