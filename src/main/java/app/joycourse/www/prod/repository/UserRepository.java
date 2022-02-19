package app.joycourse.www.prod.repository;


import app.joycourse.www.prod.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUid(String uid);

    Optional<User> findByNickname(String nickname);
}
