package app.joycourse.www.prod.config;

import app.joycourse.www.prod.annotation.AuthorizationUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.lang.annotation.Annotation;
import java.util.Collections;

@Configuration
@Profile("dev")
public class SwaggerConfig implements WebMvcConfigurer {
    @Bean
    public Docket swagger() {
        return new Docket(DocumentationType.OAS_30)
                .ignoredParameterTypes(AuthorizationUser.class)
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey()))
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Collections.singletonList(defaultAuth()))
                .operationSelector(operationContext -> {
                    for (ResolvedMethodParameter parameter : operationContext.getParameters()) {
                        for (Annotation annotation : parameter.getAnnotations()) {
                            if (annotation instanceof AuthorizationUser) {
                                return true;
                            }
                        }
                    }
                    return false;
                })
                .build();
    }

    private SecurityReference defaultAuth() {
        AuthorizationScope scope = new AuthorizationScope("global", "authorization");
        return new SecurityReference("Authorization", new AuthorizationScope[]{scope});
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Joycourse Swagger")
                .contact(new Contact("Joycourse", "https://www.joycourse.app", "joycourse.kr@gmail.com"))
                .description("<p>조이코스 api 문서 입니다! :)</p><p>인증은 Authorize 초록색 버튼 또는 자물쇠 버튼에 <pre>tony</pre> 또는 <pre>ironman</pre> 입력하면됩니다.</p>")
                .version("0.1")
                .build();
    }
}