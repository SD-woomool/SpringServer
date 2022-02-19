package app.joycourse.www.prod.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class CustomException extends RuntimeException {
    @AllArgsConstructor
    @Getter
    public enum CustomError {
        INVALID_PROVIDER(400, "BAD_REQUEST"),
        GET_TOKEN_ERROR(403, "FORBIDDEN"),
        PAGE_NOT_FOUND(404, "PAGE_NOT_FOUND"),
        UNAUTHORIZED(401, "UNAUTHORIZED"),
        MISSING_PARAMETERS(400, "PARAMETER IS MISSING"),
        BAD_REQUEST(400, "BAD_REQUEST"),
        SERVER_ERROR(500, "SERVER_ERROR");

        private final int status;
        private final String errorDescription;
    }

    private final CustomError customError;

    public CustomException(CustomError customError) {
        this.customError = customError;
    }

    public int getStatus() {
        return customError.getStatus();
    }

    public String getErrorDescription() {
        return customError.getErrorDescription();
    }
}
