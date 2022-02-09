package app.joycourse.www.prod.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommentSaveRequestBodyDto {

    private CommentInfoDto commentInfo;
    private Long courseId;
}
