package app.joycourse.www.prod.repository;

import app.joycourse.www.prod.domain.Comment;
import app.joycourse.www.prod.domain.Course;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment saveComment(Comment comment);

    Optional<List<Comment>> pagingByCourse(Course course, int page, int pageLength);

    void deleteComment(Comment comment);
}
