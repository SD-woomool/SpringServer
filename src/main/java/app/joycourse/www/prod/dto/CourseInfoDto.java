package app.joycourse.www.prod.dto;

import app.joycourse.www.prod.entity.Course;
import app.joycourse.www.prod.entity.CourseDetail;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Getter
@Setter
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

    public CourseInfoDto() {

    }

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
        this.courseDetail = Optional.ofNullable(courseDetailList).stream().flatMap(Collection::stream)
                .map((detail) ->
                        new CourseDetailDto(detail.getCourse().getId(), detail.getPrice(), detail.getContent(), new PhotoInfoDto(detail.getPhoto(), null, false), new PlaceInfoDto(detail.getPlace())))
                .collect(Collectors.toList());
    }

    public CourseInfoDto(Course course) {
        this.id = course.getId();
        this.userNickname = course.getUser().getNickname();
        this.title = course.getTitle();
        this.content = course.getContent();
        this.thumbnailUrl = course.getThumbnailUrl();
        this.location = course.getLocation();
        this.totalPrice = course.getTotalPrice();
        this.memo = course.getMemo();
        this.likeCnt = course.getLikeCnt();
        this.courseDetail = Optional.ofNullable(course.getCourseDetail()).stream().flatMap(Collection::stream)
                .map((detail) ->
                        new CourseDetailDto(detail.getCourse().getId(), detail.getPrice(), detail.getContent(), new PhotoInfoDto(detail.getPhoto(), null, false), new PlaceInfoDto(detail.getPlace())))
                .collect(Collectors.toList());
    }


}
