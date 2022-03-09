package app.joycourse.www.prod.dto.request;

import app.joycourse.www.prod.entity.user.AgeRange;
import app.joycourse.www.prod.entity.user.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserUpdateDto {
    @NotNull
    private AgeRange ageRange;
    @NotNull
    private Gender gender;
}
