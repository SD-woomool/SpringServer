package app.joycourse.www.prod.dto;


import app.joycourse.www.prod.domain.Course;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CourseListDto {
    Boolean isEnd;
    Integer pageLength;
    Integer page;
    List<CourseInfoDto> courseList;

    public CourseListDto(
            Boolean isEnd,
            Integer pageLength,
            Integer page,
            List<CourseInfoDto> courseList
    ) {
        this.isEnd = isEnd;
        this.pageLength = pageLength;
        this.page = page;
        this.courseList = courseList;
    }

    public CourseListDto(
            Boolean isEnd,
            Integer pageLength,
            Integer page
    ) {
        this.isEnd = isEnd;
        this.pageLength = pageLength;
        this.page = page;
    }

    public void setCourseList(List<Course> courseList) {
        if (courseList == null) {
            this.courseList = null;
            return;
        }
        this.courseList = new ArrayList<>();
        courseList.forEach((course) -> {
            this.courseList.add(new CourseInfoDto(
                    course.getId(),
                    course.getUser().getNickname(),
                    course.getTitle(),
                    course.getContent(),
                    course.getLocation(),
                    course.getThumbnailUrl(),
                    course.getLikeCnt(),
                    course.getTotalPrice(),
                    course.getMemo(),
                    course.getCourseDetail()
            ));
        });
    }

}
