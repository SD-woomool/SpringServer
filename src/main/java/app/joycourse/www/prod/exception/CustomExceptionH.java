package app.joycourse.www.prod.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class CustomExceptionH {
    @Getter
    @AllArgsConstructor
    public static enum CustomError {
        PROVIDER_WRONG(400, "PROVIDER IS NOT VALID"),
        PAGE_NOT_FOUND(404, "INVALID ROUTE OR METHOD"),
        SERVER_ERROR(500, "SERVER ERROR"),
        BAD_REQUEST(400, "BAD REQUEST"),
        MISSING_PARAMETERS(400, "MISSING PARAMETERS");

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
