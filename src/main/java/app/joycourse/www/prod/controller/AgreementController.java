package app.joycourse.www.prod.controller;

import app.joycourse.www.prod.annotation.AuthorizationUser;
import app.joycourse.www.prod.dto.Response;
import app.joycourse.www.prod.dto.request.AgreementRequestDto;
import app.joycourse.www.prod.dto.response.AgreementResponseDto;
import app.joycourse.www.prod.entity.user.User;
import app.joycourse.www.prod.service.AgreementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "약관")
@RestController
@RequestMapping("/agreement")
@RequiredArgsConstructor
public class AgreementController {
    private final AgreementService agreementService;

    @ApiOperation(
            value = "해당 유저가 동의 해야하는 약관들을 내려준다.",
            notes = "해당 유저가 동의 해야하는 약관들을 내려준다.\n" +
                    "- 아직 아무것도 동의하지 않은 경우 약관들을 내려준다.\n" +
                    "- 유저가 현재 동의한 약관이 최신이라면 빈배열이 내려간다.\n" +
                    "- 약관 버전이 바뀌면(약관이 새로 생기게 되면), 약관이 같이 내려간다. 이때 동의하지 않은 선택 약관도 내려간다.",
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 정보가 없는 경우"),
            @ApiResponse(code = 403, message = "권한이 없거나, 유효하지 않는 인증 정보인 경우")
    })
    @GetMapping
    public Response<List<AgreementResponseDto>> getAgreements(@AuthorizationUser User user) {
        return new Response<>(agreementService.getAgreements(user));
    }

    @ApiOperation(
            value = "약관에 동의한다.",
            notes = "동의할 약관들을 목록으로 받아와, 약관 동의 여부를 체크한다.",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 400, message = "잘못된 파라미터, 필수 약관을 동의하지 않은 경우"),
            @ApiResponse(code = 401, message = "인증 정보가 없는 경우"),
            @ApiResponse(code = 403, message = "권한이 없거나, 유효하지 않는 인증 정보인 경우")
    })
    @PostMapping
    public Response<Void> agreeAgreements(@AuthorizationUser User user, @Valid @RequestBody AgreementRequestDto agreementRequestDto) {
        agreementService.agreeAgreements(user, agreementRequestDto);
        return new Response<>();
    }
}
