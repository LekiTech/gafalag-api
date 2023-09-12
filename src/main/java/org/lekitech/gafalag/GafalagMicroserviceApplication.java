package org.lekitech.gafalag;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.http.HttpMethod.*;

@Slf4j
@SpringBootApplication
//@EnableJpaRepositories
public class GafalagMicroserviceApplication extends SpringBootServletInitializer {

    private static final long MAXAGESECS = 3600;

    public static void main(String[] args) {
        SpringApplication.run(GafalagMicroserviceApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(GafalagMicroserviceApplication.class);
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
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods(GET.name(), POST.name(), PUT.name(), PATCH.name(), DELETE.name(), OPTIONS.name())
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(MAXAGESECS);
            }
        };
    }
}
