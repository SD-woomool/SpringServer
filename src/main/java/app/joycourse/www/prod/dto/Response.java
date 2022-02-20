package app.joycourse.www.prod.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response<T> {
    private Integer status;
    private String errorDescription;
    private T data;

    public Response() {
        this.status = 200;
    }

    public Response(T data) {
        this.data = data;
        this.status = 200;
    }

    public Response(Integer status, String errorDescription) {
        this.status = status;
        this.errorDescription = errorDescription;
    }
}
