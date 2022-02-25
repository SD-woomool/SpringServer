package app.joycourse.www.prod.dto;

import app.joycourse.www.prod.entity.CourseDetail;
import app.joycourse.www.prod.entity.Place;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CourseDetailDto {

    private Long courseId;
    private float price;
    private String content;
    private PhotoInfoDto photo;
    private PlaceInfoDto place;

    public CourseDetailDto() {
    }

    public void setPlace(Place place) {
        if (place != null) {
            this.place = new PlaceInfoDto(place);
        } else {
            this.place = null;
        }
    }

    public CourseDetail convertToEntity() {
        return new CourseDetail(this);
    }
}
