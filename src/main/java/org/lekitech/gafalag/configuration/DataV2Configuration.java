package org.lekitech.gafalag.configuration;

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
@EnableJpaRepositories(
        basePackages = "org.lekitech.gafalag.repository.v2",
        entityManagerFactoryRef = "entityManagerFactoryV2",
        transactionManagerRef = "transactionManagerV2"
)
public class DataV2Configuration {
    @Autowired
    private Environment env;
    @Value("${gafalag.db.v2.url}")
    private String dbUrlV2;
    @Value("${gafalag.db.v2.username}")
    private String dbUsernameV2;
    @Value("${gafalag.db.v2.password}")
    private String dbPasswordV2;


    @Primary
    @Bean(name = "entityManagerFactoryV2")
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryV2() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(getDataSourceV2());
        em.setPackagesToScan("org.lekitech.gafalag.entity.v2");

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
     *
     * @return
     */
    @Primary
    @Bean(name = "v2")
    public DataSource getDataSourceV2() {
        log.info("\n===== PASSED V2 DB PARAMS =====\n'" + dbUrlV2 + "'\n'" + dbUsernameV2 + "'\n'" + dbPasswordV2 + "'");
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(dbUrlV2);
        dataSourceBuilder.username(dbUsernameV2);
        dataSourceBuilder.password(dbPasswordV2);
        return dataSourceBuilder.build();
    }

    @Primary
    @Bean(name = "transactionManagerV2")
    public PlatformTransactionManager userTransactionManager() {

        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                entityManagerFactoryV2().getObject());
        return transactionManager;
    }
}
