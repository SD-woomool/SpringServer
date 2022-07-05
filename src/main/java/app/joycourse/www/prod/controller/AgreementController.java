package app.joycourse.www.prod.controller;

import app.joycourse.www.prod.annotation.AuthorizationUser;
import app.joycourse.www.prod.dto.Response;
import app.joycourse.www.prod.dto.request.AgreementRequestDto;
import app.joycourse.www.prod.dto.response.AgreementResponseDto;
import app.joycourse.www.prod.entity.user.User;
import app.joycourse.www.prod.service.AgreementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("agreement")
@RequiredArgsConstructor
public class AgreementController {
    private final AgreementService agreementService;

    @GetMapping
    public Response<List<AgreementResponseDto>> getAgreements(@AuthorizationUser User user) {
        return new Response<>(agreementService.getAgreements(user));
    }

    @PostMapping
    public Response<Void> agreeAgreements(@AuthorizationUser User user, @Valid @RequestBody AgreementRequestDto agreementRequestDto) {
        agreementService.agreeAgreements(agreementRequestDto);
        return new Response<>();
    }
}
