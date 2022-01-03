package app.joycourse.www.prod.repository;

import app.joycourse.www.prod.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.transaction.Transactional;
import java.util.Optional;

@SpringBootTest(properties = {"nickname=nickname", "email=email@email.com", "ageRange=0", "gender=0"})
@Transactional
public class JpaAccountRepositoryTests {
    @Autowired
    private AccountRepository repository;

    @Value("${nickname}")
    private String nickname;

    @Value("${email}")
    private String email;

    @Value("${ageRange}")
    private int ageRange;

    @Value("${gender}")
    private int gender;

    @Test
    public void 유저_저장() {
        // given
        User user = new User();
        user.setNickname(nickname);
        user.setEmail(email);
        user.setAgeRange(ageRange);
        user.setGender(gender);
        user.setCreateAt();
        repository.newUser(user);

        // when
        Optional<User> target = repository.findById(user.getId());

        // then
        Assertions.assertThat(target.isPresent()).isEqualTo(true);
        if (target.isPresent()) {
            User targetUser = target.get();
            Assertions.assertThat(targetUser.getNickname()).isEqualTo(nickname);
            Assertions.assertThat(targetUser.getEmail()).isEqualTo(email);
            Assertions.assertThat(targetUser.getAgeRange()).isEqualTo(ageRange);
            Assertions.assertThat(targetUser.getGender()).isEqualTo(gender);
        }
    }

    @Test
    public void 중복유저_저장() {
        // given
        User user = new User();
        user.setNickname(nickname);
        user.setEmail(email);
        user.setAgeRange(ageRange);
        user.setGender(gender);
        user.setCreateAt();
        repository.newUser(user);

        // when
        User duplicatedUser = new User();
        duplicatedUser.setNickname(nickname);
        duplicatedUser.setEmail(email);
        duplicatedUser.setAgeRange(ageRange);
        duplicatedUser.setGender(gender);
        duplicatedUser.setCreateAt();

        // then
        Assertions.assertThatThrownBy(() -> {
            repository.newUser(duplicatedUser);
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

}
