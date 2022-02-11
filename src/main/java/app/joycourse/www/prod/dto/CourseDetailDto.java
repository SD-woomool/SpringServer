package app.joycourse.www.prod.dto;

import app.joycourse.www.prod.domain.CourseDetail;
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
    private String photo;

    public CourseDetailDto() {
    }

    public CourseDetail convertToEntity() {
        return new CourseDetail(this);
    }

}
