package app.joycourse.www.prod.handler;

import app.joycourse.www.prod.dto.Response;
import app.joycourse.www.prod.exception.CustomException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public Response<Object> handleCustomException(CustomException e) {
        return new Response<>(e.getErrorMessage(), e.getErrorStatus());
    }

    @ExceptionHandler({NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public Response<Object> handleNotFoundException(Exception e) {
        CustomException.CustomError error = CustomException.CustomError.PAGE_NOT_FOUND;
        return new Response<>(error.getMessage(), error.getStatus());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Response<Object> handleMissingParameters(MissingServletRequestParameterException e) {
        CustomException.CustomError error = CustomException.CustomError.MISSING_PARAMETERS;
        return new Response<>(error.getMessage(), error.getStatus());
    }

    @ExceptionHandler
    public Response<Object> handleException(Exception e) {
        e.printStackTrace();
        CustomException.CustomError error = CustomException.CustomError.SERVER_ERROR;
        return new Response<>(error.getMessage(), error.getStatus());
    }
}
