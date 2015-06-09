package pl.java.scalatech.reader;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Maps;

import pl.java.scalatech.config.job.SimpleGenericReaderJob;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@SpringApplicationConfiguration(classes = {SimpleGenericReaderJob.class})
public class GenericReaderTest {
    @Autowired
    private JobRegistry jobRegistry;

    @Autowired
    private JobLauncher jobLauncher;
    @Test
    public void shouldBootStrapWork() {
        
    }
    @Test
    public void shouldCsvProcessing() throws NoSuchJobException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
            Job job = jobRegistry.getJob("simpleCustomers");
            Assertions.assertThat(jobRegistry).isNotNull();
            Assertions.assertThat(jobLauncher).isNotNull();
            log.info("jobs :  {}", jobRegistry.getJobNames());
            //
            Map<String,JobParameter> params = Maps.newHashMap();
            params.put("time", new JobParameter(System.currentTimeMillis()));
            Assertions.assertThat(job).isNotNull();
            Assertions.assertThat(jobLauncher.run(job, new JobParameters(params)).getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        }
}
