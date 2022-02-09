package app.joycourse.www.prod.service;


import app.joycourse.www.prod.domain.Comment;
import app.joycourse.www.prod.domain.User;
import app.joycourse.www.prod.dto.CommentInfoDto;
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
    AccountService accountService;
    @Autowired
    EntityManager em;

    @Test
    public void cascadeWithoutColumnTest() {
        //given
        System.out.println("test start!!");
        User user1 = new User();
        user1.setNickname("ykh1");
        user1.setEmail("ykh2@email.com");
        user1.setAgeRange(20);
        user1.setGender(1);
        user1.setCreateAt();
        accountService.saveUser(user1);
        Long userId = user1.getId();
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
