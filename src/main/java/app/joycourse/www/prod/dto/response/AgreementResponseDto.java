package app.joycourse.www.prod.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AgreementResponseDto {
    private final Long seq;
    private final String title;
    private final String content;
    private final Boolean isRequired;
}
