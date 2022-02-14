package app.joycourse.www.prod.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class PlaceSearchResponseDto {

    private List<PlaceInfoDto> documents;
    private Meta meta;

    public PlaceSearchResponseDto() {
    }

    public PlaceSearchResponseDto(Boolean isEnd) {
        this.documents = new ArrayList<>();
        this.setMeta(new Meta(isEnd));
    }
}

@Getter
@Setter
class Meta {
    private Boolean isEnd;
    private Integer totalCount;
    private Integer pageableCount;
    private SameName sameName;

    public Meta() {
    }

    public Meta(Boolean isEnd) {
        this.isEnd = isEnd;
    }
}

@Getter
@Setter
class SameName {
    private List<String> region;
    private String keyword;
    private String selectedRegion;
}

