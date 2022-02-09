package app.joycourse.www.prod.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CommentListDto {

    private Boolean idEnd;
    private int page;
    private int pageLength;
    private List<CommentInfoDto> commentList;
}
