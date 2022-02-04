package app.joycourse.www.prod.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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

