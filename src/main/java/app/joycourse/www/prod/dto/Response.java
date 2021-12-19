package app.joycourse.www.prod.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.metadata.HanaCallMetaDataProvider;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
//@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Response<T> {
    private String error;
    private String errorDescription;
    private T data;

    public Response(T data){
        this.data = data;
        this.error = null;
        this.errorDescription = null;
    }

    public Response (String error, String errorDescription){
        this.error = error;
        this.errorDescription = errorDescription;
        this.data = null;
    }

}
