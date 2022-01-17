package app.joycourse.www.prod.dto;

import app.joycourse.www.prod.domain.CourseDetail;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CourseSaveDto {
    private Boolean save;
    private String title;
    private String content;
    private Integer like_cnt;
    private Float totalPrice;
    private List<CourseDetail> courseDetail;
}
