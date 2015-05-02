package pl.java.scalatech.config.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import pl.java.scalatech.tasklet.HelloTasklet;

@Configuration
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobs;
    
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

    @Bean
    public JobRepository jobRepository(PlatformTransactionManager transactionManager) throws Exception {
        MapJobRepositoryFactoryBean fb = new MapJobRepositoryFactoryBean(transactionManager);
        return fb.getObject();
    }

    @Bean
    public JobLauncher jobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        return launcher;
    }

}
