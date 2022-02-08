package app.joycourse.www.prod.repository;

import app.joycourse.www.prod.domain.Comment;
import app.joycourse.www.prod.domain.Course;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaCommentRepository implements CommentRepository {
    private final EntityManager em;

    @Override
    public Comment saveComment(Comment comment) {
        em.persist(comment);
        return comment;
    }

    @Override
    public Optional<List<Comment>> pagingByCourse(Course course, int page, int pageLength) {
        List<Comment> result = em.createQuery("select c from Comment c where c.course = :course", Comment.class)
                .setParameter("course", course)
                .setFirstResult((page - 1) * pageLength)
                .setMaxResults(pageLength)
                .getResultList();
        return Optional.ofNullable(result);
    }

    @Override
    public void deleteComment(Comment comment) {
        em.remove(comment);
    }
}
