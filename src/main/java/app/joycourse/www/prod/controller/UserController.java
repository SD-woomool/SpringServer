package app.joycourse.www.prod.controller;


import app.joycourse.www.prod.annotation.AuthorizationUser;
import app.joycourse.www.prod.dto.Response;
import app.joycourse.www.prod.dto.request.UserSignDto;
import app.joycourse.www.prod.dto.request.UserUpdateDto;
import app.joycourse.www.prod.dto.response.UserInfoDto;
import app.joycourse.www.prod.entity.ImageFile;
import app.joycourse.www.prod.entity.user.User;
import app.joycourse.www.prod.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/nickname")
    public Response<Boolean> checkNickname(@RequestParam(value = "query")
                                           @Valid @NotBlank @Size(min = 3, max = 10) String nickname) {
        return new Response<>(!userService.alreadyExistNickname(nickname));
    }

    @PostMapping
    public Response<Boolean> sign(@AuthorizationUser User user,
                                  @Valid @RequestPart("body") UserSignDto userSignDto,
                                  @RequestPart(name = "file", required = false) MultipartFile profileImageFile) {
        userService.signUser(user, userSignDto, profileImageFile);
        return new Response<>(true);
    }

    @PutMapping
    public Response<Boolean> update(@AuthorizationUser User user,
                                    @Valid @RequestPart("body") UserUpdateDto userUpdateDto,
                                    @RequestPart(name = "file", required = false) MultipartFile profileImageFile) {
        userService.updateUser(user, userUpdateDto, profileImageFile);
        return new Response<>(true);
    }

    @GetMapping
    public Response<UserInfoDto> me(@AuthorizationUser User user) {
        return new Response<>(
                UserInfoDto.builder()
                        .ageRange(user.getAgeRange())
                        .gender(user.getGender())
                        .nickname(user.getNickname())
                        .profileImageUrl(Optional.ofNullable(user.getImageFile()).map(ImageFile::getFileUrl).orElse(null))
                        .build());
    }
}
