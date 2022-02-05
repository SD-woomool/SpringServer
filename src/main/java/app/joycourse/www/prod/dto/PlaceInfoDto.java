package app.joycourse.www.prod.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
