package app.joycourse.www.prod.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommentRequestBodyDto {

    private CommentInfoDto commentInfo;
    private Long courseId;
}
