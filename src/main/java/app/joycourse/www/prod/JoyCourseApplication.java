package app.joycourse.www.prod;

import app.joycourse.www.prod.domain.User;
import app.joycourse.www.prod.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.transaction.Transactional;

@ServletComponentScan
@SpringBootApplication
@EnableWebMvc
public class JoyCourseApplication {

    public static void main(String[] args) {
        SpringApplication.run(JoyCourseApplication.class, args);
    }

    @Bean
    DispatcherServlet dispatcherServlet() {  // 404에러는 컨트롤러 까지 오지 않고 dispatchServlet에서 해결됨, 그걸 해결하기 위해
        DispatcherServlet ds = new DispatcherServlet();
        ds.setThrowExceptionIfNoHandlerFound(true);
        return ds;
    }

    @Bean
    @Transactional
    CommandLineRunner insertTestData(AccountRepository accountRepository) {
        return args -> {
            User user1 = new User();
            user1.setNickname("ykh");
            user1.setEmail("ykh@email.com");
            user1.setAgeRange(20);
            user1.setGender(1);
            user1.setCreateAt();
            accountRepository.newUser(user1);

            User user2 = new User();
            user2.setNickname("hsh");
            user2.setEmail("hsh@email.com");
            user2.setAgeRange(20);
            user2.setGender(1);
            user2.setCreateAt();
            accountRepository.newUser(user2);

            System.out.println("Test user(ykh, hsh) is created");
        };
    }
}
