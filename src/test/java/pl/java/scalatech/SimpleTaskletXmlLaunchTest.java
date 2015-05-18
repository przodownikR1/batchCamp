package pl.java.scalatech;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Maps;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:simpleTasklet.xml"})
@ActiveProfiles(value= {"dev","dev-prepare-db"})
@Slf4j
public class SimpleTaskletXmlLaunchTest {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;

    @Autowired
    private DataSource dataSource;
    @Test
    public void shouldTaskletPrintSomething() throws SQLException {
       log.info(" +++   db driver :  {}",dataSource.getConnection().getMetaData().getDriverName());

        try {
            Map<String,JobParameter> params = Maps.newHashMap();
            params.put("test", new JobParameter("przodownik"));
            params.put("time", new JobParameter(new Date()));
            JobExecution execution = jobLauncher.run(job, new JobParameters(params));
            log.info("Exit Status :  {}", execution.getExitStatus());
            Assert.assertEquals(ExitStatus.COMPLETED, execution.getExitStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
