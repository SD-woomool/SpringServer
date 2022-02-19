package app.joycourse.www.prod.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response<T> {
    private String error;
    private String errorDescription;
    private T data;

    public Response(T data) {
        this.data = data;
        this.error = null;
        this.errorDescription = null;
    }

    public Response(String error, String errorDescription) {
        this.error = error;
        this.errorDescription = errorDescription;
        this.data = null;
    }

    public Response(String error, String errorDescription, T data) {
        this.error = error;
        this.errorDescription = errorDescription;
        this.data = data;
    }

}
