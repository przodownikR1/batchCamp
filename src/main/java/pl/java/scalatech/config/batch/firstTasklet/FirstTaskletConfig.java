package pl.java.scalatech.config.batch.firstTasklet;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;

import pl.java.scalatech.config.batch.BatchConfig;
import pl.java.scalatech.tasklet.HelloTasklet;
@Configuration
@Import(BatchConfig.class)
@Slf4j
public class FirstTaskletConfig {
    @Autowired
    private JobBuilderFactory jobs;
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private StepBuilderFactory stepBuilders;
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
}
