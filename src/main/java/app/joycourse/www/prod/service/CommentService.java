package app.joycourse.www.prod.service;

import app.joycourse.www.prod.domain.Comment;
import app.joycourse.www.prod.domain.Course;
import app.joycourse.www.prod.domain.User;
import app.joycourse.www.prod.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Comment saveComment(Comment comment, User user, Course course) {
        comment.setUser(user);
        comment.setCourse(course);
        comment.setCreateAt();
        return commentRepository.saveComment(comment);
    }

    public Optional<List<Comment>> pagingComment(Course course, int page, int pageLength) {

        return commentRepository.pagingByCourse(course, page, pageLength);
    }
}
