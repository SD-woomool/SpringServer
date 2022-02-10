package app.joycourse.www.prod.dto;

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
}
