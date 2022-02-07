package app.joycourse.www.prod.repository;

import app.joycourse.www.prod.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

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
    public void deleteComment(Comment comment) {
        em.remove(comment);
    }
}
