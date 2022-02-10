package app.joycourse.www.prod.domain;


import app.joycourse.www.prod.dto.CommentInfoDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.text.SimpleDateFormat;

@Entity
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column
    private int likeCnt;

    @Column
    private String createdAt;

    @Column
    private Long parentComment;

    public Comment() {

    }

    public Comment(CommentInfoDto commentInfo) {
        this.content = commentInfo.getContent();
        this.likeCnt = commentInfo.getLikeCnt();
        this.parentComment = commentInfo.getParentComment();
        this.createdAt = commentInfo.getCreatedAt();
    }

    public void setCreateAt() {
        long millis = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.createdAt = format.format(millis);
    }

    public void setCreateAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
