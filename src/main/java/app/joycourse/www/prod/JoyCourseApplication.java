package app.joycourse.www.prod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class JoyCourseApplication {

    public static void main(String[] args) {
        SpringApplication.run(JoyCourseApplication.class, args);
    }

}
