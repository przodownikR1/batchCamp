package pl.java.scalatech.flatimportfile;

import static com.jayway.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.concurrent.Callable;

import lombok.extern.slf4j.Slf4j;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.java.scalatech.config.batch.BatchConfig;
import pl.java.scalatech.config.job.FlatFileJob;
import pl.java.scalatech.config.metrics.JmxConfig;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@DirtiesContext
@SpringApplicationConfiguration(classes = { BatchConfig.class, FlatFileJob.class, JmxConfig.class })
public class FlatImportFileTest {
    @Autowired
    private JobRegistry jobRegistry;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobOperator jobOperator;

    @Autowired
    private JobExplorer jobExplorer;
    @Test
    public void shouldBootStrapWork() {

    }

    @Test
    public void shouldCsvProcessing() throws NoSuchJobException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
            JobParametersInvalidException, IOException, InterruptedException, JobInstanceAlreadyExistsException {
        Job job = jobRegistry.getJob("importCustomers");
        Assertions.assertThat(jobRegistry).isNotNull();
        Assertions.assertThat(jobLauncher).isNotNull();
        log.info("jobs :  {}", jobRegistry.getJobNames());
        //
        Assertions.assertThat(job).isNotNull();
        long executionId = jobOperator.start(jobRegistry.getJobNames().stream().findFirst().get(), "" + System.currentTimeMillis());
        await().until(finished(executionId));
        JobExecution execution = jobExplorer.getJobExecution(executionId);
        assertEquals(ExitStatus.COMPLETED.getExitCode(), execution.getExitStatus().getExitCode());
        
    }
    private Callable<Boolean> finished(final long executionId) {
        return () -> jobExplorer.getJobExecution(executionId).isRunning() == false;
    }
}
