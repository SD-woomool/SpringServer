package app.joycourse.www.prod.dto;

import app.joycourse.www.prod.domain.CourseDetail;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CourseInfoDto {
    String title;
    String content;
    String thumbnailUrl;
    Integer likeCnt;
    List<CourseDetailDto> courseDetail;

    public CourseInfoDto(
            String title,
            String content,
            String thumbnailUrl,
            Integer likeCnt,
            List<CourseDetail> courseDetailList
    ){

        this.title = title;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
        this.likeCnt = likeCnt;
        this.courseDetail = new ArrayList<>();
        courseDetailList.forEach((detail) -> {
            this.courseDetail.add(new CourseDetailDto(detail.getPrice(), detail.getContent(), detail.getPhoto()));
        });

    }




}
