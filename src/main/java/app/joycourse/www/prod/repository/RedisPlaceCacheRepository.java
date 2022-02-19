package app.joycourse.www.prod.repository;

import app.joycourse.www.prod.dto.PlaceSearchResponseDto;
import org.springframework.data.repository.CrudRepository;

public interface RedisPlaceCacheRepository extends CrudRepository<PlaceSearchResponseDto, String> {

}
