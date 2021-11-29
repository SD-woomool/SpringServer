package app.joycourse.www.prod.repository;


import app.joycourse.www.prod.domain.User;

import java.util.Optional;

public interface AccountRepository {
    void save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);
}
