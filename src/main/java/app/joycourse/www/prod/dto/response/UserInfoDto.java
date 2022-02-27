package app.joycourse.www.prod.dto.response;

import app.joycourse.www.prod.entity.user.AgeRangeEnum;
import app.joycourse.www.prod.entity.user.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserInfoDto {
    private String nickname;
    private String profileImageUrl;
    private AgeRangeEnum ageRangeEnum;
    private GenderEnum genderEnum;
}
