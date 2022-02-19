package app.joycourse.www.prod.repository;

import app.joycourse.www.prod.entity.Place;

import java.util.Optional;

public interface PlaceRepository {
    Place save(Place place);

    Optional<Place> findById(Long id);
}
