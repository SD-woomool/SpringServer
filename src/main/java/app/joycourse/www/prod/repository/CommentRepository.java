package app.joycourse.www.prod.repository;


import app.joycourse.www.prod.entity.Comment;
import app.joycourse.www.prod.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment saveComment(Comment comment);

    Optional<List<Comment>> pagingByCourse(Course course, int page, int pageLength);

    void deleteComment(Comment comment);

    int deleteCommentByParentId(Long parentId);


    Optional<Comment> findById(Long commentId);

    Optional<List<Comment>> findByParentId(Long parentId);

    Optional<Comment> mergeComment(Comment newComment);
}
