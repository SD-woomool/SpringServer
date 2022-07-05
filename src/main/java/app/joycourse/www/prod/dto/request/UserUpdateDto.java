package app.joycourse.www.prod.dto.request;

import app.joycourse.www.prod.entity.user.AgeRangeEnum;
import app.joycourse.www.prod.entity.user.GenderEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto {
    private AgeRangeEnum ageRange;
    private GenderEnum gender;
}
