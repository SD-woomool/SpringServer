package app.joycourse.www.prod.dto.request;

import app.joycourse.www.prod.entity.user.AgeRangeEnum;
import app.joycourse.www.prod.entity.user.GenderEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserSignDto {
    @NotBlank
    @Size(min = 3, max = 10)
    private String nickname;
    private AgeRangeEnum ageRange;
    private GenderEnum gender;
}
