package app.joycourse.www.prod.dto.request;

import app.joycourse.www.prod.entity.user.AgeRange;
import app.joycourse.www.prod.entity.user.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto {
    private AgeRange ageRange;
    private Gender gender;
}
