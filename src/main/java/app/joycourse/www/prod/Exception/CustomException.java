package app.joycourse.www.prod.Exception;

import lombok.AllArgsConstructor;

public class CustomException extends RuntimeException{
    @AllArgsConstructor
    public enum CustomError{
        INVALID_PROVIDER(400, "BAD_REQUEST"),
        GET_TOKEN_ERROR(500, "SERVER_ERROR"),
        PAGE_NOT_FOUND(404, "PAGE_NOT_FOUND"),
        UNAUTHORIZED(401, "UNAUTHORIZED"),
        BAD_REQUEST(400, "BAD_REQUEST"),
        SERVER_ERROR(500, "SERVER_ERROR");

        private int status;
        private String error;

        public int getStatus(){
            return this.status;
        }
        public String getError(){
            return this.error;
        }
    }

    CustomError customError;

    public CustomException(){
        super("SERVER_ERROR");
    }
    public CustomException(String message){
        super(message);
    }
    public CustomException(String message, CustomError customError){
        super(message);
        this.customError = customError;
    }
    public CustomException(CustomError customError){
        super("SERVER_ERROR");
        this.customError = customError;
    }

    public CustomError getCustomError(){
        return this.customError;
    }

}
