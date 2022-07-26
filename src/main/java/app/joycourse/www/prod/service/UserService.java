package app.joycourse.www.prod.service;

import app.joycourse.www.prod.dto.request.UserSignDto;
import app.joycourse.www.prod.dto.request.UserUpdateDto;
import app.joycourse.www.prod.entity.user.User;
import app.joycourse.www.prod.entity.user.UserRoleEnum;
import app.joycourse.www.prod.exception.CustomException;
import app.joycourse.www.prod.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final FileService fileService;
    private final UserRepository userRepository;

    public Boolean alreadyExistNickname(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }

    public void signUser(User user, UserSignDto userSignDto, MultipartFile profileImageFile) {
        if (user.getIsSigned()) {
            log.info("[signUser] Fail to sign, cause already signed user: {}", user.getSeq());
            throw new CustomException(CustomException.CustomError.FORBIDDEN);
        }
        Map<String, String> fileMap = fileService.uploadFiles(List.of(profileImageFile), FileService.ImageFileType.PROFILE_IMAGE);
        user.setProfileImageUrl(fileMap.get(profileImageFile.getOriginalFilename()));
        user.setNickname(userSignDto.getNickname());
        user.setAgeRangeEnum(userSignDto.getAgeRange());
        user.setGenderEnum(userSignDto.getGender());
        user.setIsSigned(true);
        userRepository.save(user);
    }

    @CacheEvict(value = "AuthorizedUser", key = "'nickname_' + #user.nickname")// -> 테스트 사용자 없어지면 key = "#user.uid"로 바꾸자
    public void updateUser(User user, UserUpdateDto userUpdateDto, MultipartFile profileImageFile) {
        // TODO: 삭제 로직 바뀌면 수정해야함!
//        if (!fileService.deleteFile(기존 유저 떰네일 기반으로 삭제, FileService.ImageFileType.PROFILE_IMAGE)) {
//            log.error("[updateUser] Fail to delete profile. User: {}, ProfileImage: {}", user.getSeq(), profileImageFile.getOriginalFilename());
//            throw new CustomException(CustomException.CustomError.SERVER_ERROR);
//        }
        if (!profileImageFile.isEmpty()) {
            Map<String, String> fileMap = fileService.uploadFiles(List.of(profileImageFile), FileService.ImageFileType.PROFILE_IMAGE);
            user.setProfileImageUrl(fileMap.get(profileImageFile.getOriginalFilename()));
        }
        user.setAgeRangeEnum(userUpdateDto.getAgeRange());
        user.setGenderEnum(userUpdateDto.getGender());
        userRepository.save(user);
    }

    // MSA 환경일때 이부분은 유저에 대한 정보를 주게된다.
    // 유저가 없는경우 생성하고, 유저가 있는 경우 정보 업데이트한다.
    public Boolean syncUser(String uid) {
        User user = userRepository.findByUid(uid).orElseGet(() -> {
            // 없는 경우 새로 생성한다.
            User newUser = new User();
            newUser.setUid(uid);
            userRepository.save(newUser);

            return newUser;
        });

        return user.getRole().equals(UserRoleEnum.BLOCK);
    }

    @Cacheable(value = "AuthorizedUser", key = "'uid_' + #uid", cacheManager = "cacheManger")
    public Optional<User> getUserByUid(String uid) {
        return userRepository.findByUid(uid);
    }

    @Cacheable(value = "AuthorizedUser", key = "'nickname_' + #nickname", cacheManager = "cacheManager")
    // -> 테스트 사용자 없어지면 지우자
    public Optional<User> getUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname);
    }
}
