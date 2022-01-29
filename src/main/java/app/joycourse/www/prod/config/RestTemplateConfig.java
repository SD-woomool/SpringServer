package app.joycourse.www.prod.config;


import ch.qos.logback.classic.pattern.MessageConverter;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Arrays;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(){
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(3000);
        // Apache HttpComponents HttpClient
        HttpClient httpClient = HttpClientBuilder.create().setMaxConnTotal(50).setMaxConnPerRoute(20).build();

        factory.setHttpClient(httpClient);
        RestTemplate restTemplate = new RestTemplate(factory);
        return restTemplate;

    }
}
