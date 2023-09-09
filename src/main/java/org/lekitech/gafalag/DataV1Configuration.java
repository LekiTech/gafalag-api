package org.lekitech.gafalag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Slf4j
@Configuration
//@PropertySource({ "classpath:application-dev.properties" })
@EnableJpaRepositories(
        basePackages = "org.lekitech.gafalag.repository.v1",
        entityManagerFactoryRef = "entityManagerFactoryV1",
        transactionManagerRef = "transactionManagerV1"
)
public class DataV1Configuration {
    @Autowired
    private Environment env;
    @Value("${gafalag.db.v1.url}")
    private String dbUrlV1;
    @Value("${gafalag.db.v1.username}")
    private String dbUsernameV1;
    @Value("${gafalag.db.v1.password}")
    private String dbPasswordV1;


//    @Primary
    @Bean(name = "entityManagerFactoryV1")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryV1() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(getDataSourceV1());
        em.setPackagesToScan("org.lekitech.gafalag.entity.v1");

        HibernateJpaVendorAdapter vendorAdapter
                = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto",
                env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect",
                env.getProperty("hibernate.dialect"));
        em.setJpaPropertyMap(properties);

        return em;
    }

    /**
     * Loading db connection programmatically for secure connection string passing in production
     * @return
     */
//    @Primary
    @Bean(name="v1")
    public DataSource getDataSourceV1() {
        log.info("\n===== PASSED V1 DB PARAMS =====\n'" + dbUrlV1 + "'\n'" + dbUsernameV1 + "'\n'" + dbPasswordV1 + "'");
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(dbUrlV1);
        dataSourceBuilder.username(dbUsernameV1);
        dataSourceBuilder.password(dbPasswordV1);
        return dataSourceBuilder.build();
    }

//    @Primary
    @Bean(name = "transactionManagerV1")
    public PlatformTransactionManager userTransactionManager() {

        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                entityManagerFactoryV1().getObject());
        return transactionManager;
    }
}
