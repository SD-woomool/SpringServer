package app.joycourse.www.prod.repository;

import app.joycourse.www.prod.domain.Comment;

public interface CommentRepository {
    Comment saveComment(Comment comment);

    void deleteComment(Comment comment);
}
