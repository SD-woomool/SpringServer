package app.joycourse.www.prod.dto;

import app.joycourse.www.prod.entity.Place;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Document(indexName = "place")
public class PlaceInfoDto {

    private String addressName;

    private String categoryGroupCode;

    private String categoryGroupName;

    private String categoryName;

    private Float distance;

    private Long id;

    private String phone;

    private String placeName;

    private String placeUrl;

    private String roadAddressName;

    private Float x;

    private Float y;

    public PlaceInfoDto() {
    }

    public PlaceInfoDto(Place place) {
        if (place != null) {
            this.addressName = place.getAddressName();
            this.categoryGroupCode = place.getCategoryGroupCode();
            this.categoryGroupName = place.getCategoryGroupName();
            this.categoryName = place.getCategoryName();
            this.distance = place.getDistance();
            this.id = place.getId();
            this.phone = place.getPhone();
            this.placeName = place.getPlaceName();
            this.placeUrl = place.getPlaceUrl();
            this.roadAddressName = place.getRoadAddressName();
            this.x = place.getX();
            this.y = place.getY();
        }

    }

    @Override
    public String toString() {
        return "{" +
                "addressName='" + addressName + '\'' +
                ", categoryGroupCode='" + categoryGroupCode + '\'' +
                ", categoryGroupName='" + categoryGroupName + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", distance=" + distance +
                ", id=" + id +
                ", phone='" + phone + '\'' +
                ", placeName='" + placeName + '\'' +
                ", placeUrl='" + placeUrl + '\'' +
                ", roadAddressName='" + roadAddressName + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
