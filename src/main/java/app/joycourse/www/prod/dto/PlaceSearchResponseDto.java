package app.joycourse.www.prod.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class PlaceSearchResponseDto {

    private List<PlaceInfoDto> documents;
    private Meta meta;

}

@Getter
@Setter
class Meta {
    private Boolean isEnd;
    private Integer totalCount;
    private Integer pageableCount;
    private SameName sameName;
}

@Getter
@Setter
class SameName {
    private List<String> region;
    private String keyword;
    private String selectedRegion;
}

