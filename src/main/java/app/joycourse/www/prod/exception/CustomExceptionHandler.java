package app.joycourse.www.prod.exception;


import app.joycourse.www.prod.dto.Response;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Map;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public Response<Void> requestBodyIsWrong(HttpMessageNotReadableException e) {
        log.error("[requestBodyIsWrong]: {}", e.getMessage());
        Throwable throwable = e.getMostSpecificCause();
        CustomException customException;
        if (throwable instanceof InvalidFormatException) {
            customException = new CustomException(CustomException.CustomError.INVALID_PARAMETER);
        } else {
            customException = new CustomException(CustomException.CustomError.MISSING_PARAMETERS);
        }
        return new Response<>(customException.getStatus(), customException.getErrorDescription());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, MultipartException.class})
    public Response<Void> invalidParameter(Exception e) {
        log.error("[invalidParameter]: {}", e.getMessage());
        CustomException customException = new CustomException(CustomException.CustomError.INVALID_PARAMETER);
        return new Response<>(customException.getStatus(), customException.getErrorDescription());
    }

    @ExceptionHandler({CustomException.class})
    public Response<Void> customExceptionHandler(CustomException customException) {
        log.error("[customExceptionHandler]: {}({})", customException.getErrorDescription(), customException.getStatus());
        log.error(customException.getErrorDescription(), customException);
        return new Response<>(customException.getStatus(), customException.getErrorDescription());
    }

    @ExceptionHandler({NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public Response<Void> noHandlerFoundException(Exception e) {
        log.error("[noHandlerFoundException]: {}", e.getMessage());
        CustomException customException = new CustomException(CustomException.CustomError.PAGE_NOT_FOUND);
        return new Response<>(customException.getStatus(), customException.getErrorDescription());
    }

    @ExceptionHandler({AuthenticationException.class, AccessDeniedException.class})
    public Response<Void> wrongAccessExceptionHandler(Exception e) {
        log.error("[wrongAccessExceptionHandler]: {}", e.getMessage());
        CustomException customException = new CustomException(CustomException.CustomError.UNAUTHORIZED);
        return new Response<>(customException.getStatus(), customException.getErrorDescription());
    }

    @ExceptionHandler({Exception.class})
    public Response<Map<String, Integer>> exceptionHandler(Exception e) {
        log.error("[exceptionHandler], {}", e.getMessage());
        log.error(e.getMessage(), e);
        CustomException customException = new CustomException(CustomException.CustomError.SERVER_ERROR);
        return new Response<>(customException.getStatus(), customException.getErrorDescription());
    }
}
