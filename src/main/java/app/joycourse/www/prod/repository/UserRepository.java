package app.joycourse.www.prod.repository;


import app.joycourse.www.prod.entity.User;

import javax.persistence.EntityManager;
import java.util.Optional;

public interface UserRepository {
    EntityManager getEm();

    Optional<User> findBySeq(Long seq);

    Optional<User> findByUid(String uid);

    Optional<User> findByNickname(String nickname);

    User newUser(User user);

    void deleteUser(User user);

    void updateUser(User user, User userInfo);
}
