package app.joycourse.www.prod.repository;


import app.joycourse.www.prod.domain.User;

import javax.persistence.EntityManager;
import java.util.Optional;

public interface AccountRepository {
    EntityManager getEm();
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    Optional<User> findByNickname(String nickname);
    User newUser(User user);
    void deleteUser(User user);
    void updateUser(User user, User userInfo);
}
