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
    private Long id;
    private String userNickname;
    private String title;
    private String content;
    private String location;
    private String thumbnailUrl;
    private Integer likeCnt;
    private Float totalPrice;
    private String memo;
    private List<CourseDetailDto> courseDetail;

    public CourseInfoDto(
            Long id,
            String userNickname,
            String title,
            String content,
            String location,
            String thumbnailUrl,
            Integer likeCnt,
            Float totalPrice,
            String memo,
            List<CourseDetail> courseDetailList
    ) {
        this.id = id;
        this.userNickname = userNickname;
        this.title = title;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
        this.location = location;
        this.totalPrice = totalPrice;
        this.memo = memo;
        this.likeCnt = likeCnt;
        this.courseDetail = new ArrayList<>();
        courseDetailList.forEach((detail) -> {
            this.courseDetail.add(new CourseDetailDto(detail.getPrice(), detail.getContent(), detail.getPhoto()));
        });

    }


}
