package app.joycourse.www.prod.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;

@Setter
@Getter
//@RedisHash(value = "places", timeToLive = 3000)
public class PlaceCache {
    @Id
    private String id;

}
