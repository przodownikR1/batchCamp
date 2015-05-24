package pl.java.scalatech.scheduler;

import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.java.scalatech.config.batch.BatchConfig;
import pl.java.scalatech.config.batch.firstTasklet.FirstScheduleTask;
import pl.java.scalatech.config.batch.firstTasklet.FirstTaskletConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { FirstScheduleTask.class, BatchConfig.class })
@ActiveProfiles(value="dev")
@Slf4j
public class TaskletSchedulerTest {    
    @Autowired
    private Job job;

    @Test
    public void testJob() throws Exception {
       
        Thread.sleep(2000);

    }
  
}