package app.joycourse.www.prod.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PlaceSearchResponseDto {

    private List<PlaceInfoDto> documents;
    private Meta meta;

    @Override
    public String toString() {

        String placeInfoList = "";
        
        return "PlaceSearchResponseDto{" +
                "documents=" + documents +
                ", meta=" + meta +
                '}';
    }
}

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
class Meta {
    private Boolean isEnd;
    private Integer totalCount;
    private Integer pageableCount;
    private SameName sameName;
}

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
class SameName {
    private List<String> region;
    private String keyword;
    private String selectedRegion;
}

