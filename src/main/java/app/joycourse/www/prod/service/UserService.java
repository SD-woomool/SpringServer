package app.joycourse.www.prod.service;

import app.joycourse.www.prod.entity.user.User;
import app.joycourse.www.prod.entity.user.UserRole;
import app.joycourse.www.prod.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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