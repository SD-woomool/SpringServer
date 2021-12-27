package app.joycourse.www.prod.repository;

import app.joycourse.www.prod.domain.User;

import java.util.Optional;

public interface AccountRepository {
    Optional<User> findByEmail(String email);
    Optional<User> findById(String id);
    Optional<User> findByNickname(String nickname);
}
