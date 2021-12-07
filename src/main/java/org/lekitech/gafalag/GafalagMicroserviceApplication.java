package org.lekitech.gafalag;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@SpringBootApplication
@EnableJpaRepositories
public class GafalagMicroserviceApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(GafalagMicroserviceApplication.class);
    }

    public static void main(String[] args) {
        log.info("Start Gafalag Microservice");
        SpringApplication.run(GafalagMicroserviceApplication.class, args);
    }

    public @Bean
    OpenAPI checkAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Gafalag Microservice")
                                .description("Gafalag - микросервис словаря")
                                .version("0.1.1-MVP")
                );
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // TODO Allowed frontend requests (CORS)
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }
}
