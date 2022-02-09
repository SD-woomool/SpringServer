package app.joycourse.www.prod.repository;

import app.joycourse.www.prod.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaUserRepository implements UserRepository {
    private final EntityManager em;

    public JpaUserRepository(EntityManager em) {
        this.em = em;
    }

    public EntityManager getEm() {
        return this.em;
    }

    @Override
    public Optional<User> findByNickname(String nickname) {
        List<User> result = em.createQuery("select m from User m where m.nickname = :nickname", User.class).
                setParameter("nickname", nickname).getResultList();
        return result.stream().findAny();
    }

    @Override
    public Optional<User> findBySeq(Long seq) {
        return Optional.ofNullable(em.find(User.class, seq));
    }

    @Override
    public Optional<User> findByUid(String uid) {
        List<User> result = em.createQuery("select m from User m where m.uid = :uid", User.class).
                setParameter("uid", uid).getResultList();
        return result.stream().findAny();
    }

    @Override
    public User newUser(User user) {
        em.persist(user); // 한번 찾아보자
        return user;
    }

    @Override
    public void deleteUser(User user) {
//        Long id = user.getId();
//        int deleteCount = em.createQuery("delete from User u where u.id = :id").
//                setParameter("id", id).executeUpdate();
//        System.out.println("삭제한 갯수: " + deleteCount);
    }

    @Override
    public void updateUser(User user, User userInfo) {
//        int resultCount = em.createQuery("update User u set u.profileImageUrl = :profileImageUrl, u.gender = :gender, u.ageRange = :ageRange " +
//                        "where u.id = :id").
//                setParameter("profileImageUrl", userInfo.getProfileImageUrl()).
//                setParameter("gender", userInfo.getGender()).
//                setParameter("ageRange", userInfo.getAgeRange()).
//                setParameter("id", user.getId()).
//                executeUpdate();
//        System.out.println("업데이트 결과 수: " + resultCount);
    }
}


