package app.joycourse.www.prod.dto;

import app.joycourse.www.prod.domain.Comment;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
public class CommentInfoDto {

    private Long id;
    private String nickname;
    private String content;
    private Long CourseId;
    private int likeCnt;
    private String createdAt;
    private Long parentComment;

    public CommentInfoDto(Comment comment) {
        this.id = comment.getId();
        this.nickname = comment.getUser().getNickname();
        this.content = comment.getContent();
        this.CourseId = comment.getCourse().getId();
        this.likeCnt = comment.getLikeCnt();
        this.createdAt = comment.getCreatedAt();
        this.parentComment = comment.getParentComment();
    }
}
