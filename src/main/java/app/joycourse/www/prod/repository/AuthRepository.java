package app.joycourse.www.prod.repository;

import app.joycourse.www.prod.entity.auth.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findByUid(String uid);

    Optional<Auth> findByEmail(String email);
}
