package app.joycourse.www.prod.config;

import app.joycourse.www.prod.filter.ExceptionHandlerFilter;
import app.joycourse.www.prod.service.auth.AuthenticationFailureHandler;
import app.joycourse.www.prod.service.auth.AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final CorsConfig corsConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .logout().disable()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> handlerExceptionResolver.resolveException(request, response, null, authException))
                .accessDeniedHandler((request, response, accessDeniedException) -> handlerExceptionResolver.resolveException(request, response, null, accessDeniedException))
                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/auth/authorization")
                .and()
                .redirectionEndpoint()
                .baseUri("/auth/callback/*")
                .and()
                .userInfoEndpoint()
                .and()
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler);

        http.addFilterBefore(exceptionHandlerFilter, SecurityContextPersistenceFilter.class);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedHeaders(corsConfig.getAllowedHeaders());
        config.setAllowedMethods(corsConfig.getAllowedMethods());
        config.setAllowedOrigins(corsConfig.getAllowedOrigins());
        config.setMaxAge(corsConfig.getMaxAge()); // preflight max age
        config.setAllowCredentials(true);

        corsConfigurationSource.registerCorsConfiguration("/**", config);
        return corsConfigurationSource;
    }
}
