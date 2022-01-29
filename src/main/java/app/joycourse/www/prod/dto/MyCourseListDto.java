package app.joycourse.www.prod.dto;


import app.joycourse.www.prod.domain.Course;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MyCourseListDto {
    Boolean isEnd;
    Integer pageLength;
    Integer page;
    List<CourseInfoDto> courseList;

    public MyCourseListDto(
            Boolean isEnd,
            Integer pageLength,
            Integer page
    ){
        this.isEnd = isEnd;
        this.pageLength = pageLength;
        this.page = page;
    }

    public void setCourseList(List<Course> courseList){
        if(courseList == null){
            System.out.println("****** no course***********");
            this.courseList = null;
            return;
        }
        this.courseList = new ArrayList<>();
        courseList.forEach((course)->{
            this.courseList.add(new CourseInfoDto(
                    course.getTitle(),
                    course.getContent(),
                    course.getThumbnailUrl(),
                    course.getLikeCnt(),
                    course.getCourseDetail()
            ));
        });
    }

}
