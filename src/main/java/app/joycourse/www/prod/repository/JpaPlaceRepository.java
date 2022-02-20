package app.joycourse.www.prod.repository;

import app.joycourse.www.prod.entity.Place;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaPlaceRepository implements PlaceRepository {

    private final EntityManager em;

    @Override
    public Place save(Place place) {
        em.persist(place);
        return place;
    }

    @Override
    public Optional<Place> findById(Long id) {
        return Optional.ofNullable(em.find(Place.class, id));
    }
}
