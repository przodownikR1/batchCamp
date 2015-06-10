package pl.java.scalatech.simple_string_job;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.java.scalatech.common.CommonJobTest;
import pl.java.scalatech.config.job.SimpleStringStepsJob;
import static org.junit.Assert.assertEquals;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
@ContextConfiguration(classes = { SimpleStringStepsJob.class })
public class SimpleStringStepsTest extends CommonJobTest {

    @Test
    public void shouldSimpleJobTest() throws NoSuchJobException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
            JobParametersInvalidException, JobInstanceAlreadyExistsException {
        Job job = jobRegistry.getJob("simpleStepsProcessing");
        Assertions.assertThat(jobRegistry).isNotNull();
        Assertions.assertThat(jobLauncher).isNotNull();
        log.info("jobs :  {}", jobRegistry.getJobNames());
        JobExecution execution = startJobAndGetExecutionId();
        assertEquals(ExitStatus.COMPLETED.getExitCode(), execution.getExitStatus().getExitCode());
    }

}
