package pl.java.scalatech.flatimportfile;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.java.scalatech.common.CommonJobTest;
import pl.java.scalatech.config.job.FlatFileJob;
import static org.junit.Assert.assertEquals;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@SpringApplicationConfiguration(classes = { FlatFileJob.class })
public class FlatImportFileTest extends CommonJobTest {

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
        JobExecution execution = startJobAndGetExecutionId();
        assertEquals(ExitStatus.COMPLETED.getExitCode(), execution.getExitStatus().getExitCode());

    }

}
