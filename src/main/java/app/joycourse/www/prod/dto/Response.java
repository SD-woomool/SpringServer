package app.joycourse.www.prod.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class Response<T> {
    private final T data;
    private final String errorMessage;
    private final Integer status;

    public Response(T data) {
        this.data = data;
        this.errorMessage = null;
        this.status = 200;
    }

    public Response(String errorMessage, Integer status) {
        this.data = null;
        this.errorMessage = errorMessage;
        this.status = status;
    }
}
