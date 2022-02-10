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

    @Override
    public int deleteCommentByParentId(Long parentId) {
        return em.createQuery("delete from Comment c where c.parentComment = :parentComment")
                .setParameter("parentComment", parentId)
                .executeUpdate();
    }


    @Override
    public Optional<Comment> findById(Long commentId) {
        return Optional.ofNullable(em.find(Comment.class, commentId));
    }

    @Override
    public Optional<List<Comment>> findByParentId(Long parentId) {
        return Optional.ofNullable(
                em.createQuery("select from Comment c where c.parentId = :parentId", Comment.class)
                        .setParameter("parentId", parentId)
                        .getResultList()
        );
    }

    @Override
    public Optional<Comment> mergeComment(Comment newComment) {
        return Optional.ofNullable(em.merge(newComment));
    }
}
