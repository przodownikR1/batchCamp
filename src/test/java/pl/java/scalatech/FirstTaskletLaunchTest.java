package pl.java.scalatech;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Maps;

import pl.java.scalatech.config.batch.BatchConfig;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@SpringApplicationConfiguration(classes = {BatchCampApplication.class,BatchConfig.class})
public class FirstTaskletLaunchTest {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;

    @Test
    public void shouldTaskletPrintSomething() {

        try {
            Map<String,JobParameter> params = Maps.newHashMap();
            params.put("test", new JobParameter("przodownik"));
            params.put("time", new JobParameter(new Date()));
            JobExecution execution = jobLauncher.run(job, new JobParameters(params));
            log.info("Exit Status :  {}", execution.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}