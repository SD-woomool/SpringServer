package app.joycourse.www.prod.dto.response;

import app.joycourse.www.prod.entity.user.AgeRange;
import app.joycourse.www.prod.entity.user.Agreement;
import app.joycourse.www.prod.entity.user.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserInfoDto {
    private String nickname;
    private String profileImageUrl;
    private AgeRange ageRange;
    private Gender gender;
    private Agreement agreement;
}
