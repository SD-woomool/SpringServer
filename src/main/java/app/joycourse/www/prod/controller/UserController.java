package app.joycourse.www.prod.controller;


import app.joycourse.www.prod.annotation.AuthorizationUser;
import app.joycourse.www.prod.dto.Response;
import app.joycourse.www.prod.dto.request.UserSignDto;
import app.joycourse.www.prod.dto.request.UserUpdateDto;
import app.joycourse.www.prod.dto.response.UserInfoDto;
import app.joycourse.www.prod.entity.user.User;
import app.joycourse.www.prod.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Api(tags = "유저")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @ApiOperation(
            value = "닉네임 중복 체크",
            notes = "이미 등록되어 있는 닉네임인지 체크한다.\n",
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 정보가 없는 경우"),
            @ApiResponse(code = 403, message = "권한이 없거나, 유효하지 않는 인증 정보인 경우")
    })
    @GetMapping("/nickname")
    public Response<Boolean> checkNickname(@RequestParam(value = "query") String nickname) {
        return new Response<>(!userService.alreadyExistNickname(nickname));
    }

    @ApiOperation(
            value = "회원가입을 진행한다.",
            notes = "연령, 성별, 약관 등 회원가입을 진행한다.\n",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 정보가 없는 경우"),
            @ApiResponse(code = 403, message = "권한이 없거나, 유효하지 않는 인증 정보인 경우")
    })
    @PostMapping
    public Response<Boolean> sign(@AuthorizationUser User user, @Valid @RequestPart("body") UserSignDto userSignDto, @RequestPart(name = "file", required = false) MultipartFile profileImageFile) {
        userService.signUser(user, userSignDto, profileImageFile);
        return new Response<>(true);
    }

    @ApiOperation(
            value = "회원 정보를 수정한다.",
            notes = "연령, 성별, 약관 등 회원 정보를 수정한다.\n",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 정보가 없는 경우"),
            @ApiResponse(code = 403, message = "권한이 없거나, 유효하지 않는 인증 정보인 경우")
    })
    @PutMapping
    public Response<Boolean> update(@AuthorizationUser User user, @Valid @RequestPart("body") UserUpdateDto userUpdateDto, @RequestPart(name = "file", required = false) MultipartFile profileImageFile) {
        userService.updateUser(user, userUpdateDto, profileImageFile);
        return new Response<>(true);
    }

    @ApiOperation(
            value = "내 정보를 가져온다.",
            notes = "연령, 성별, 약관 등 내 계정 정보를 가져온다.\n",
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 정보가 없는 경우"),
            @ApiResponse(code = 403, message = "권한이 없거나, 유효하지 않는 인증 정보인 경우")
    })
    @GetMapping
    public Response<UserInfoDto> me(@AuthorizationUser User user) {
        return new Response<>(
                UserInfoDto.builder()
                        .ageRangeEnum(user.getAgeRangeEnum())
                        .genderEnum(user.getGenderEnum())
                        .nickname(user.getNickname())
                        .profileImageUrl(user.getProfileImageUrl())
                        .build());
    }
}
