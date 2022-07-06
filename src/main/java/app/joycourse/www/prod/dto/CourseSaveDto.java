package app.joycourse.www.prod.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseSaveDto {
    private Boolean save;
    private CourseInfoDto courseInfo;


    public CourseSaveDto(
            Boolean save,
            CourseInfoDto courseInfo
    ) {

        this.save = save;
        this.courseInfo = courseInfo;

    }


}

