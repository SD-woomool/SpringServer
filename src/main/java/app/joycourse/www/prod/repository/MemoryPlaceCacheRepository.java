package app.joycourse.www.prod.repository;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Repository
public class MemoryPlaceCacheRepository implements PlaceCacheRepository {

    private static final Map<String, String> store = new HashMap<>();

    @Override
    public void save(String key, String value) {
        store.put(key, value);
    }

    @Override
    public Optional<String> findByKeyword(String key) {
        return Optional.ofNullable(store.get(key));
    }
}
