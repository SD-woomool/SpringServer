package app.joycourse.www.prod.dto;

import app.joycourse.www.prod.domain.Comment;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
public class CommentSaveRequestBodyDto {

    private Comment comment;
    private Long courseId;
}
