package app.joycourse.www.prod.repository;

import app.joycourse.www.prod.domain.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaAccountRepository implements AccountRepository{
    private final EntityManager em;

    public JpaAccountRepository(EntityManager em){
        this.em = em;
    }

    @Override
    public Optional<User> findByEmail(String email){
        List<User> result = em.createQuery("select m from User m where m.email = :email", User.class).
                setParameter("email", email).getResultList();
        return result.stream().findAny();
    }
}
