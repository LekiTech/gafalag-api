package org.lekitech.gafalag;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpMethod.OPTIONS;

@Slf4j
@SpringBootApplication
@EnableJpaRepositories
public class GafalagMicroserviceApplication extends SpringBootServletInitializer {
    @Value("${gafalag.db.url}")
    private String dbUrl;
    @Value("${gafalag.db.username}")
    private String dbUsername;
    @Value("${gafalag.db.password}")
    private String dbPassword;

    private static final long MAXAGESECS = 3600;

    /**
     * Loading db connection programmatically for secure connection string passing in production
     * @return
     */
    @Bean
    public DataSource getDataSource() {
        log.info("\n===== PASSED DB PARAMS =====\n'" + dbUrl + "'\n'" + dbUsername + "'\n'" + dbPassword + "'");
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(dbUrl);
        dataSourceBuilder.username(dbUsername);
        dataSourceBuilder.password(dbPassword);
        return dataSourceBuilder.build();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(GafalagMicroserviceApplication.class);
    }

    public static void main(String[] args) {
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
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                // TODO Allowed frontend requests (CORS)
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
