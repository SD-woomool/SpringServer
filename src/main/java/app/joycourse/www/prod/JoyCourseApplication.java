package app.joycourse.www.prod;

import app.joycourse.www.prod.entity.Provider;
import app.joycourse.www.prod.entity.User;
import app.joycourse.www.prod.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.transaction.Transactional;

@ServletComponentScan
@SpringBootApplication
@EnableWebMvc
@EnableWebSecurity
@Slf4j
public class JoyCourseApplication {

    public static void main(String[] args) {
        SpringApplication.run(JoyCourseApplication.class, args);
    }

    @Bean
    CommandLineRunner insertTestData(UserRepository userRepository) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) {
                User tony = new User();
                tony.setUid("NAVER_1");
                tony.setNickname("tony");
                tony.setProvider(Provider.GOOGLE);
                userRepository.newUser(tony);

                User ironman = new User();
                ironman.setUid("NAVER_2");
                ironman.setNickname("ironman");
                ironman.setProvider(Provider.GOOGLE);
                userRepository.newUser(ironman);

                log.info("Test user(Tony, Ironman) is created");
            }
        };
    }
}
