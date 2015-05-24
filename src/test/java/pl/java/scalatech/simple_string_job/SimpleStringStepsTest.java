package pl.java.scalatech.simple_string_job;

import lombok.extern.slf4j.Slf4j;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.java.scalatech.config.batch.BatchConfig;
import pl.java.scalatech.config.job.SimpleStringJob;
import pl.java.scalatech.config.job.SimpleStringStepsJob;
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
@ContextConfiguration(classes = { BatchConfig.class, SimpleStringStepsJob.class })

public class SimpleStringStepsTest {

    @Autowired
    private JobRegistry jobRegistry;

    @Autowired
    private JobLauncher jobLauncher;

    @Test
    public void shouldSimpleJobTest() throws NoSuchJobException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
            JobParametersInvalidException {
        Job job = jobRegistry.getJob("simpleStepsProcessing");
        Assertions.assertThat(jobRegistry).isNotNull();
        Assertions.assertThat(jobLauncher).isNotNull();
        log.info("jobs :  {}", jobRegistry.getJobNames());
        //
        Assertions.assertThat(job).isNotNull();
        Assertions.assertThat(jobLauncher.run(job, new JobParameters()).getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }

}
