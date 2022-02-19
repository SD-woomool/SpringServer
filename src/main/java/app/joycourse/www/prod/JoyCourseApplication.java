package app.joycourse.www.prod;

import app.joycourse.www.prod.entity.auth.Auth;
import app.joycourse.www.prod.entity.auth.Provider;
import app.joycourse.www.prod.entity.user.Agreement;
import app.joycourse.www.prod.entity.user.User;
import app.joycourse.www.prod.entity.user.UserRole;
import app.joycourse.www.prod.repository.AuthRepository;
import app.joycourse.www.prod.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.transaction.Transactional;

@EnableWebMvc
@EnableWebSecurity
@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
@Slf4j
public class JoyCourseApplication {

    public static void main(String[] args) {
        SpringApplication.run(JoyCourseApplication.class, args);
    }

    @Bean
    CommandLineRunner insertTestData(UserRepository userRepository, AuthRepository authRepository) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) {
                Auth tonyAuth = new Auth();
                tonyAuth.setUid("tonyUid");
                tonyAuth.setEmail("tony@gmail.com");
                tonyAuth.setProvider(Provider.GOOGLE);
                authRepository.save(tonyAuth);

                User tony = new User();
                tony.setUid(tonyAuth.getUid());
                tony.setNickname("tony");
                tony.setIsSigned(true);
                tony.setAgreement(Agreement.BASIC);
                tony.setRole(UserRole.ADMIN);
                userRepository.save(tony);

                Auth ironmanAuth = new Auth();
                ironmanAuth.setUid("ironmanUid");
                ironmanAuth.setEmail("ironman@gmail.com");
                ironmanAuth.setProvider(Provider.GOOGLE);
                authRepository.save(ironmanAuth);

                User ironman = new User();
                ironman.setUid(ironmanAuth.getUid());
                ironman.setNickname("ironman");
                ironman.setIsSigned(true);
                ironman.setAgreement(Agreement.BASIC);
                ironman.setRole(UserRole.NORMAL);
                userRepository.save(ironman);

                log.info("Test user(Tony, Ironman) is created");
            }
        };
    }
}
