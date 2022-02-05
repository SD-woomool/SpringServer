package app.joycourse.www.prod.repository;

import java.util.Optional;

public interface PlaceCacheRepository {
    void save(String key, String value);

    Optional<String> findByKeyword(String key);
}
