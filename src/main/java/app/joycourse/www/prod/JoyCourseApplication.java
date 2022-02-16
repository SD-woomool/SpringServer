package app.joycourse.www.prod;

import app.joycourse.www.prod.domain.User;
import app.joycourse.www.prod.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@ServletComponentScan
@SpringBootApplication
@EnableWebMvc
@EnableFeignClients
public class JoyCourseApplication {

    public static void main(String[] args) {
        SpringApplication.run(JoyCourseApplication.class, args);
    }

    @Bean
    CommandLineRunner insertTestData(AccountRepository accountRepository) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) {
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
            }
        };
    }
}
