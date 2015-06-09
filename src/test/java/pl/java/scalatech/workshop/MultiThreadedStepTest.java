package pl.java.scalatech.workshop;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.java.scalatech.config.batch.BatchConfig;
import pl.java.scalatech.config.job.WorkshopMultiThreaded;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BatchConfig.class, WorkshopMultiThreaded.class })
@ActiveProfiles("dev")
@Slf4j
public class MultiThreadedStepTest {

    @Autowired
    JobLauncher jobLauncher;
    @Autowired
    private Job job;

    @Test
    public void trackingMultithreadedStepExecution() throws Exception {
        JobExecution exec = jobLauncher.run(job, new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters());
        assertEquals(BatchStatus.COMPLETED, exec.getStatus());
    }

}