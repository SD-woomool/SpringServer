package app.joycourse.www.prod;

import app.joycourse.www.prod.entity.Agreement;
import app.joycourse.www.prod.entity.AgreementLog;
import app.joycourse.www.prod.entity.auth.Auth;
import app.joycourse.www.prod.entity.auth.Provider;
import app.joycourse.www.prod.entity.user.User;
import app.joycourse.www.prod.entity.user.UserRoleEnum;
import app.joycourse.www.prod.repository.AgreementLogRepository;
import app.joycourse.www.prod.repository.AgreementRepository;
import app.joycourse.www.prod.repository.AuthRepository;
import app.joycourse.www.prod.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.transaction.Transactional;

@EnableWebMvc
@EnableWebSecurity
@EnableFeignClients
@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
@Slf4j
public class JoyCourseApplication {

    public static void main(String[] args) {
        SpringApplication.run(JoyCourseApplication.class, args);
    }

    @Bean
    CommandLineRunner insertTestData(UserRepository userRepository, AuthRepository authRepository, AgreementRepository agreementRepository, AgreementLogRepository agreementLogRepository) {
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
                tony.setIsAgreed(true);
                tony.setRole(UserRoleEnum.ADMIN);
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
                ironman.setIsAgreed(true);
                ironman.setRole(UserRoleEnum.NORMAL);
                userRepository.save(ironman);

                log.info("Test user(Tony, Ironman) is created");

                Agreement agreement = new Agreement();
                agreement.setTitle("개인정보 약관");
                agreement.setContent("제 1조");
                agreement.setIsRequired(true);
                agreementRepository.save(agreement);


                AgreementLog tonyAgreementLog = new AgreementLog();
                tonyAgreementLog.setAgreementSeq(agreement.getSeq());
                tonyAgreementLog.setUserSeq(tony.getSeq());
                agreementLogRepository.save(tonyAgreementLog);

                AgreementLog ironmanAgreementLog = new AgreementLog();
                ironmanAgreementLog.setAgreementSeq(agreement.getSeq());
                ironmanAgreementLog.setUserSeq(ironman.getSeq());
                agreementLogRepository.save(ironmanAgreementLog);
            }
        };
    }
}
