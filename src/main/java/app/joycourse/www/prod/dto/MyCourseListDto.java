package app.joycourse.www.prod.dto;


import app.joycourse.www.prod.domain.Course;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MyCourseListDto {
    Boolean isEnd;
    Boolean noCourse;
    Integer pageLength;
    Integer page;
    List<Course> courseList;

}
