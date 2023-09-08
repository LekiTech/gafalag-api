package org.lekitech.gafalag;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${gafalag.db.v2.url}")
    private String dbUrlV2;
    @Value("${gafalag.db.v2.username}")
    private String dbUsernameV2;
    @Value("${gafalag.db.v2.password}")
    private String dbPasswordV2;

    private static final long MAXAGESECS = 3600;

    /**
     * Loading db connection programmatically for secure connection string passing in production
     * @return
     */
//    @Primary
//    @Bean(name="v1")
//    public DataSource getDataSourceV1() {
//        log.info("\n===== PASSED V1 DB PARAMS =====\n'" + dbUrlV1 + "'\n'" + dbUsernameV1 + "'\n'" + dbPasswordV1 + "'");
//        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//        dataSourceBuilder.url(dbUrlV1);
//        dataSourceBuilder.username(dbUsernameV1);
//        dataSourceBuilder.password(dbPasswordV1);
//        return dataSourceBuilder.build();
//    }
//    @Primary
//    @Bean(name = "entityManagerFactoryV1")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactoryV1(EntityManagerFactoryBuilder builder, @Qualifier("v1") DataSource dataSource) {
//        return builder
//                .dataSource(dataSource)
//                .packages("org.lekitech.gafalag.entity.v1") // base package of your v1 entities
//                .persistenceUnit("v1")
//                .build();
//    }
//
//    @Primary
//    @Bean(name = "transactionManagerV1")
//    public PlatformTransactionManager transactionManagerV1(@Qualifier("entityManagerFactoryV1") EntityManagerFactory entityManagerFactory) {
//        return new JpaTransactionManager(entityManagerFactory);
//    }

//    @Bean(name="v2")
//    public DataSource getDataSourceV2() {
//        log.info("\n===== PASSED V2 DB PARAMS =====\n'" + dbUrlV2 + "'\n'" + dbUsernameV2 + "'\n'" + dbPasswordV2 + "'");
//        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
//        dataSourceBuilder.url(dbUrlV2);
//        dataSourceBuilder.username(dbUsernameV2);
//        dataSourceBuilder.password(dbPasswordV2);
//        return dataSourceBuilder.build();
//    }
//
//    @Bean(name = "entityManagerFactoryV2")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactoryV2(EntityManagerFactoryBuilder builder, @Qualifier("v2") DataSource dataSource) {
//        return builder
//                .dataSource(dataSource)
//                .packages("org.lekitech.gafalag.entity.v2") // base package of your v2 entities
//                .persistenceUnit("v2")
//                .build();
//    }
//
//    @Bean(name = "transactionManagerV2")
//    public PlatformTransactionManager transactionManagerV2(@Qualifier("entityManagerFactoryV2") EntityManagerFactory entityManagerFactory) {
//        return new JpaTransactionManager(entityManagerFactory);
//    }

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
