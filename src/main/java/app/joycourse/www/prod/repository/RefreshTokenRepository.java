package app.joycourse.www.prod.repository;

import app.joycourse.www.prod.entity.auth.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByUidAndDeviceId(String uid, String deviceId);
}
