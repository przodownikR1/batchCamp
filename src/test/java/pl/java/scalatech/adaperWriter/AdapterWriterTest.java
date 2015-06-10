package pl.java.scalatech.adaperWriter;

import lombok.extern.slf4j.Slf4j;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.java.scalatech.config.batch.BatchConfig;
import pl.java.scalatech.config.job.AdapterWriterJob;
import pl.java.scalatech.config.metrics.JmxConfig;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@DirtiesContext
@SpringApplicationConfiguration(classes = { BatchConfig.class, AdapterWriterJob.class, JmxConfig.class })
public class AdapterWriterTest {
    @Autowired
    private JobRegistry jobRegistry;

    @Autowired
    private JobLauncher jobLauncher;

    @Test
    @Ignore
    public void shouldBootStrapWork() {

    }
}
