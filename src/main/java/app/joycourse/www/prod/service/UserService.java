package app.joycourse.www.prod.service;

import app.joycourse.www.prod.dto.request.UserSignDto;
import app.joycourse.www.prod.dto.request.UserUpdateDto;
import app.joycourse.www.prod.entity.ImageFile;
import app.joycourse.www.prod.entity.ImageFileType;
import app.joycourse.www.prod.entity.user.User;
import app.joycourse.www.prod.entity.user.UserRole;
import app.joycourse.www.prod.exception.CustomException;
import app.joycourse.www.prod.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
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
            throw new CustomException(CustomException.CustomError.ALREADY_SIGNED);
        }

        ImageFile imageFile = Optional.ofNullable(profileImageFile)
                .map(file -> fileService.uploadFile(ImageFileType.PROFILE, file))
                .orElse(null);
        user.setImageFile(imageFile);
        user.setNickname(userSignDto.getNickname());
        user.setAgeRange(userSignDto.getAgeRange());
        user.setGender(userSignDto.getGender());
        user.setIsSigned(true);
        userRepository.save(user);
    }

    public void updateUser(User user, UserUpdateDto userUpdateDto, MultipartFile profileImageFile) {
        ImageFile imageFile = Optional.ofNullable(profileImageFile)
                .map(file -> {
                    if(Objects.nonNull(user.getImageFile())) {
                        fileService.deleteFile(ImageFileType.PROFILE, user.getImageFile().getHashedName());
                    }

                    return fileService.uploadFile(ImageFileType.PROFILE, file);
                })
                .orElse(null);
        user.setImageFile(imageFile);
        user.setAgeRange(userUpdateDto.getAgeRange());
        user.setGender(userUpdateDto.getGender());
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

        return user.getRole().equals(UserRole.BLOCK);
    }
}
