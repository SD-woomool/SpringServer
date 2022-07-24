package app.joycourse.www.prod.dto;

import app.joycourse.www.prod.entity.CourseDetail;
import app.joycourse.www.prod.entity.Place;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
//@Document(indexName = "course_detail")
public class CourseDetailDto {

    private Long id;
    private Long courseId;
    private float price;
    private String content;
    private PhotoInfoDto photo;
    private PlaceInfoDto place;

    public CourseDetailDto() {
    }

    public static CourseDetailDto of(CourseDetail courseDetail, PhotoInfoDto photoInfoDto, PlaceInfoDto placeInfoDto) {
        return CourseDetailDto.builder()
                .id(courseDetail.getId())
                .courseId(courseDetail.getCourse() != null ? courseDetail.getCourse().getId() : null)
                .price(courseDetail.getPrice())
                .content(courseDetail.getContent())
                .photo(photoInfoDto)
                .place(placeInfoDto)
                .build();
    }

    public void setPlace(Place place) {
        if (place != null) {
            this.place = new PlaceInfoDto(place);
        } else {
            this.place = null;
        }
    }

    public CourseDetail convertToEntity() {
        return CourseDetail.of(this, null, null);
    }
}
