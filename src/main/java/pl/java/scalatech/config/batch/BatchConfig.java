package pl.java.scalatech.config.batch;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;

import pl.java.scalatech.config.jpa.JpaConfig;

@Configuration
@EnableBatchProcessing
@Import(JpaConfig.class)
// @EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@Slf4j
@ComponentScan(basePackages = "pl.java.scalatech.listeners")
public class BatchConfig {
    @Value("${batch.jdbc.driver}")
    private String driverDB;
    @Value("${batch.jdbc.url}")
    private String urlDB;
    @Value("${batch.jdbc.user}")
    private String userDB;
    @Value("${batch.jdbc.password}")
    private String passwdDB;
    @Value("${batch.schema.script.drop}")
    private String dropScript;
    @Value("${batch.schema.script}")
    private String createScript;

    @Bean
    public ResourceDatabasePopulator resourceDatabasePopulator() {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new ClassPathResource(dropScript));
        resourceDatabasePopulator.addScript(new ClassPathResource(createScript));
        log.debug("+++ resourceDbPopulator drop.... {}", dropScript);
        log.debug("+++ resourceDbPopulator init.... {}", createScript);

        return resourceDatabasePopulator;
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource);
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator());
        return dataSourceInitializer;
    }

    @PostConstruct
    public void init() {
        log.debug("+++ init - > driverDb {}", driverDB);
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
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

    @Bean
    public JobRegistry jobRegistry() {
        return new MapJobRegistry();
    }

    @Bean
    public JobRepository jobRepository(PlatformTransactionManager transactionManager, DataSource dataSource) throws Exception {
        JobRepositoryFactoryBean fb = new JobRepositoryFactoryBean();
        log.info("+++  jobRepository Store :  {}", dataSource.getConnection().getMetaData().getDriverName());
        fb.setDataSource(dataSource);
        fb.setTransactionManager(transactionManager);
        fb.setTablePrefix("BATCH_");
        return fb.getObject();
    }

    @Bean
    public JobLauncher jobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        return launcher;
    }

    @Bean
    // beanPostProcessor that registers Job beans with a JobRegistry.
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }
}
/*
 
 */
/*
 * @Bean
 * public JobRepository jobRepository(PlatformTransactionManager transactionManager) throws Exception {
 * MapJobRepositoryFactoryBean fb = new MapJobRepositoryFactoryBean(transactionManager);
 * return fb.getObject();
 * }
 */

