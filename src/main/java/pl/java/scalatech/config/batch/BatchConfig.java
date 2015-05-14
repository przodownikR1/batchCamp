package pl.java.scalatech.config.batch;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;

import pl.java.scalatech.tasklet.HelloTasklet;

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@PropertySource("classpath:batch_mysql.properties")
@Slf4j
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

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory stepBuilders;

    @Autowired
    private DataSource dataSource;

    @Bean
    public ResourceDatabasePopulator resourceDatabasePopulator() {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new ClassPathResource(dropScript));
        resourceDatabasePopulator.addScript(new ClassPathResource(createScript));
        log.info("+++ resourceDbPopulator .... {},{}", dropScript, createScript);
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
        log.info("+++ init - > driverDb {}", driverDB);
    }

    @Bean
    public Job job() {
        return jobs.get("HelloJob").start(step()).build();
    }

    @Bean
    public Step step() {
        return stepBuilders.get("step").tasklet(helloTasklet()).build();
    }

    @Bean
    public Tasklet helloTasklet() {
        return new HelloTasklet();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        System.err.println(driverDB);
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(driverDB);

        ds.setUrl(urlDB);
        ds.setUsername(userDB);
        ds.setPassword(passwdDB);
        return ds;
    }

    @Bean
    @DependsOn("jobExplorer")
    public JobOperator jobOperator(JobLauncher jobLauncher, JobRepository jobRepository, JobExplorer jobExplorer, JobRegistry jobRegistry) throws Exception {
        SimpleJobOperator jobOperator = new SimpleJobOperator();
        jobOperator.setJobLauncher(jobLauncher);
        jobOperator.setJobRepository(jobRepository);
        jobOperator.setJobExplorer(jobExplorer);
        jobOperator.setJobRegistry(jobRegistry);

        jobOperator.afterPropertiesSet();
        return jobOperator;
    }

    @Bean
    public JobRegistry jobRegistry() {
        return new MapJobRegistry();
    }

    @Bean
    public JobRepository jobRepository(PlatformTransactionManager transactionManager, DataSource dataSource) throws Exception {
        JobRepositoryFactoryBean fb = new JobRepositoryFactoryBean();
        log.info("+++  {}", dataSource.getConnection().getMetaData().getDriverName());
        fb.setDataSource(dataSource);
        fb.setTransactionManager(transactionManager);
        fb.setTablePrefix("BATCH_");
        return fb.getObject();
    }

    @Bean
    @DependsOn("dataSource")
    public JobExplorer jobExplorer(DataSource dataSource) throws Exception {
        JobExplorerFactoryBean factory = new JobExplorerFactoryBean();
        factory.setDataSource(dataSource);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    public JobLauncher jobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        return launcher;
    }

}

/*
 * @Bean
 * public JobRepository jobRepository(PlatformTransactionManager transactionManager) throws Exception {
 * MapJobRepositoryFactoryBean fb = new MapJobRepositoryFactoryBean(transactionManager);
 * return fb.getObject();
 * }
 */

