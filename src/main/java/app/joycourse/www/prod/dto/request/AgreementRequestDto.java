package app.joycourse.www.prod.dto.request;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class AgreementRequestDto {
    @NotEmpty
    private List<AgreementRequest> agreements;

    @Getter
    @Setter
    public static class AgreementRequest {
        private Long agreementSeq;
        private Boolean isAgree;
    }
}
