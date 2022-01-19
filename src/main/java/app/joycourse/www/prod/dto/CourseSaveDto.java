package app.joycourse.www.prod.dto;

import app.joycourse.www.prod.domain.CourseDetail;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CourseSaveDto {
    private Boolean save;
    private String title;
    private String content;
    private Integer likeCnt;
    private Float totalPrice;
    private List<CourseDetailDto> courseDetail;


    public CourseSaveDto(
            Boolean save,
            String title,
            String content,
            Integer likeCnt,
            Float totalPrice,
            List<CourseDetail> courseDetailList
    ){

        this. save = save;
        this.title = title;
        this.content = content;
        this.likeCnt = likeCnt;
        this.totalPrice = totalPrice;
        this.courseDetail = new ArrayList<>();
        courseDetailList.forEach((detail) -> {
            this.courseDetail.add(new CourseDetailDto(detail.getPrice(), detail.getContent(), detail.getPhoto()));
        });

    }

    @Getter
    @Setter
    @AllArgsConstructor
    public class CourseDetailDto {

        private float price;
        private String content;
        private String photo;
    }




}

