package app.joycourse.www.prod.service;


import app.joycourse.www.prod.dto.CommentInfoDto;
import app.joycourse.www.prod.entity.Comment;
import app.joycourse.www.prod.entity.user.AgeRangeEnum;
import app.joycourse.www.prod.entity.user.GenderEnum;
import app.joycourse.www.prod.entity.user.User;
import app.joycourse.www.prod.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
public class CommentServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager em;

    @Test
    public void cascadeWithoutColumnTest() {
        //given
        System.out.println("test start!!");
        User user1 = new User();
        user1.setNickname("ykh1");
        user1.setAgeRangeEnum(AgeRangeEnum.TWENTIES);
        user1.setGenderEnum(GenderEnum.MALE);
        userRepository.save(user1);
        String userId = user1.getUid();
        //when
        CommentInfoDto commentInfo = new CommentInfoDto();
        Comment comment = new Comment(commentInfo);
        comment.setUser(user1);
        em.persist(comment);
        em.flush();
        em.clear();
        Long commentId = comment.getId();
        // then
        User findUser = em.find(User.class, userId);
        em.remove(findUser);
        em.flush();
        Assertions.assertThat(em.find(Comment.class, commentId)).isNull();
    }
}
