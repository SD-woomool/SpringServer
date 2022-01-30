package app.joycourse.www.prod.repository;

import app.joycourse.www.prod.domain.Place;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class JpaPlaceRepository implements PlaceRepository {

    private final EntityManager em;

    @Override
    public Place save(Place place) {
        em.persist(place);
        return place;
    }
}
