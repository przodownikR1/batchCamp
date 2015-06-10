package pl.java.scalatech.common;

import java.util.concurrent.Callable;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import static com.jayway.awaitility.Awaitility.await;
import pl.java.scalatech.config.batch.BatchConfig;
import pl.java.scalatech.config.metrics.JmxConfig;

@DirtiesContext
@Slf4j
@ContextConfiguration(classes = { BatchConfig.class, JmxConfig.class })
public abstract class CommonJobTest {

    @Autowired
    protected JobRegistry jobRegistry;
    @Autowired
    protected JobLauncher jobLauncher;
    @Autowired
    protected JobOperator jobOperator;
    @Autowired
    protected JobExplorer jobExplorer;

    public CommonJobTest() {
        super();
    }

    protected Callable<Boolean> finished(final long executionId) {
        return () -> jobExplorer.getJobExecution(executionId).isRunning() == false;
    }

    protected JobExecution startJobAndGetExecutionId() throws NoSuchJobException, JobInstanceAlreadyExistsException, JobParametersInvalidException {
        long executionId = jobOperator.start(jobRegistry.getJobNames().stream().findFirst().get(), "" + System.currentTimeMillis());
        await().until(finished(executionId));
        JobExecution execution = jobExplorer.getJobExecution(executionId);
        return execution;
    }

}