package app.joycourse.www.prod.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class CustomException extends RuntimeException {
    @AllArgsConstructor
    @Getter
    public enum CustomError{
        INVALID_PROVIDER(400, "BAD_REQUEST"),
        GET_TOKEN_ERROR(500, "SERVER_ERROR"),
        PAGE_NOT_FOUND(404, "PAGE_NOT_FOUND"),
        UNAUTHORIZED(401, "UNAUTHORIZED"),
        MISSING_PARAMETERS(400, "PARAMETER IS MISSING"),
        BAD_REQUEST(400, "BAD_REQUEST"),
        SERVER_ERROR(500, "SERVER_ERROR");

        private int status;
        private String error;
    }

    private final CustomError customError;

    public CustomException(){
        super("SERVER_ERROR");
        this.customError = CustomError.SERVER_ERROR;
    }
    public CustomException(String message){
        super(message);
        this.customError = CustomError.SERVER_ERROR;
    }
    public CustomException(String message, CustomError customError){
        super(message);
        this.customError = customError;
    }
    public CustomException(CustomError customError){
        super(customError.error);
        this.customError = customError;
    }

    public CustomError getCustomError(){
        return this.customError;
    }
}
