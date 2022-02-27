package app.joycourse.www.prod.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class CustomException extends RuntimeException {
    @AllArgsConstructor
    @Getter
    public enum CustomError {
        INVALID_PROVIDER(400, "INVALID_PROVIDER"),
        INVALID_PARAMETER(400, "INVALID_PARAMETER"),
        MISSING_PARAMETERS(400, "MISSING_PARAMETERS"),
        UNSIGNED(400, "UNSIGNED"),
        SHOULD_AGREE(400, "SHOULD_AGREE"),
        BAD_REQUEST(400, "BAD_REQUEST"),
        UNAUTHORIZED(401, "UNAUTHORIZED"),
        GET_TOKEN_ERROR(403, "FORBIDDEN"),
        ACCESS_TOKEN_EXPIRED(403, "ACCESS_TOKEN_EXPIRED"),
        REFRESH_TOKEN_EXPIRED(403, "REFRESH_TOKEN_EXPIRED"),
        PAGE_NOT_FOUND(404, "PAGE_NOT_FOUND"),
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
