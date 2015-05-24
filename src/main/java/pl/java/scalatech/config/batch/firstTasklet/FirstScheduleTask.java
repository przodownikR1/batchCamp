package pl.java.scalatech.config.batch.firstTasklet;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import pl.java.scalatech.config.batch.BatchConfig;
import pl.java.scalatech.tasklet.HelloTasklet;
@Configuration
@Import(BatchConfig.class)
@EnableScheduling
@Slf4j
public class FirstScheduleTask {
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

    @Scheduled(fixedRate = 1000)
    void runTask() throws Exception {
        JobParameters params = new JobParametersBuilder().addDate("date", new Date()).toJobParameters();
        JobExecution jobExecution = jobLauncher.run(job(), params);
        BatchStatus batchStatus = jobExecution.getStatus();
        while (batchStatus.isRunning()) {
            log.info("+++  Still running...");
            Thread.sleep(500);
        }

        System.out.println(String.format("Exit status: %s", jobExecution.getExitStatus().getExitCode()));
        JobInstance jobInstance = jobExecution.getJobInstance();
        System.out.println(String.format("job instance Id: %d", jobInstance.getId()));

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
