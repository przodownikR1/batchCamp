package pl.java.scalatech.listeners;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobLoggerListener implements JobExecutionListener{
    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        log.info("beginning exec : {} ", jobExecution.getJobInstance().getJobName());

    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        log.info("has completed job {} -> stastus {}", jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());
    }
}