package pl.java.scalatech.config.jpa;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "pl.java.scalatech.repository")
@EntityScan(basePackages = "pl.java.scalatech.domain")
@EnableTransactionManagement
@EnableAutoConfiguration
@ComponentScan(basePackages = { "pl.java.scalatech.service", "pl.java.scalatech.repository" })
@Slf4j
public class JpaConfig {
    @Value("${batch.jdbc.driver}")
    private String driverDB;
    @Value("${batch.jdbc.url}")
    private String urlDB;
    @Value("${batch.jdbc.user}")
    private String userDB;
    @Value("${batch.jdbc.password}")
    private String passwdDB;

    @PostConstruct
    public void init() {
        log.debug("+++ init - > driverDb {}", driverDB);
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        log.debug("+++ primary DataSource -> Batch config <- {} : {}", driverDB, urlDB);
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(driverDB);
        ds.setUrl(urlDB);
        ds.setUsername(userDB);
        ds.setPassword(passwdDB);
        return ds;
    }

    @Configuration
    @PropertySource("classpath:batch-dev.properties")
    @Slf4j
    @Profile("dev")
    public static class PropertiesLoaderForMysql {

        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            log.debug("+++               propertySource Mysql -> prop profile launch");
            return new PropertySourcesPlaceholderConfigurer();
        }
    }

    @Configuration
    @PropertySource("classpath:batch-test.properties")
    @Slf4j
    @Profile("test")
    public static class PropertiesLoaderForH2 {

        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            log.debug("+++         propertySource H2-> prop profile launch");
            return new PropertySourcesPlaceholderConfigurer();
        }
    }

}
