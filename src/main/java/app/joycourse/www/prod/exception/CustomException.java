package app.joycourse.www.prod.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class CustomException extends RuntimeException {
    @Getter
    @AllArgsConstructor
    public static enum CustomError {
        PROVIDER_WRONG(400, "PROVIDER IS NOT VALID"),
        PAGE_NOT_FOUND(404, "INVALID ROUTE OR METHOD"),
        SERVER_ERROR(500, "SERVER ERROR"),
        BAD_REQUEST(400, "BAD REQUEST");

        private int status;
        private String message;
    }

    private CustomError customError;

    public int getErrorStatus() {
        return customError.getStatus();
    }

    public String getErrorMessage() {
        return customError.getMessage();
    }
}
