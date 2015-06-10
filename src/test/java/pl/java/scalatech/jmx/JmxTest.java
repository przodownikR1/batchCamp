package pl.java.scalatech.jmx;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import lombok.extern.slf4j.Slf4j;

import org.fest.assertions.Assertions;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.java.scalatech.common.CommonJobTest;
import pl.java.scalatech.config.job.SimpleStringJob;

import com.google.common.collect.Maps;

import static com.jayway.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
@ContextConfiguration(classes = { SimpleStringJob.class })
public class JmxTest extends CommonJobTest {

    private AtomicInteger ai = new AtomicInteger();

    private String jobName;

    @Test
    public void shouldSimpleJobTest() throws NoSuchJobException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
            JobParametersInvalidException, InterruptedException {
        Job job = jobRegistry.getJob("simpleStringProcessing");
        Assertions.assertThat(jobRegistry).isNotNull();
        Assertions.assertThat(jobLauncher).isNotNull();
        log.info("jobs :  {}", jobRegistry.getJobNames());
        //
        Assertions.assertThat(job).isNotNull();
        Map<String, JobParameter> params = Maps.newHashMap();
        params.put("time", new JobParameter(System.currentTimeMillis()));
        Assertions.assertThat(jobLauncher.run(job, new JobParameters(params)).getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }

    public void shouldJmxWork() throws IOException, MalformedObjectNameException {
        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://localhost/jndi/rmi://localhost:1099/myconnector");
        JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
        MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
        ObjectName mbeanName;
        try {
            mbeanName = new ObjectName("spring:service=batch,bean=jobOperator");
            JobOperator jobOperator = JMX.newMBeanProxy(mbsc, mbeanName, JobOperator.class, true);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void jobExecutionTest() throws NoSuchJobException {
        List<JobExecution> runningJobInstances = new ArrayList<JobExecution>();
        List<String> jobNames = jobExplorer.getJobNames();
        for (String jobName : jobNames) {
            Set<JobExecution> jobExecutions = jobExplorer.findRunningJobExecutions(jobName);
            runningJobInstances.addAll(jobExecutions);
        }
    }

    @Test
    public void jobExecution2Test() throws NoSuchJobException, JobInstanceAlreadyExistsException, JobParametersInvalidException {
        // TODO hardcoding
        String jobName = jobExplorer.getJobNames().get(0);
        int count = jobExplorer.getJobInstanceCount(jobName);

        log.info(" jobname : {} , count : {}", jobName, count);
        // TODO
        long executionId = jobOperator.start("simpleStringProcessing", "" + System.currentTimeMillis());
        await().until(finished(executionId));
        JobExecution execution = jobExplorer.getJobExecution(executionId);
        assertEquals(ExitStatus.COMPLETED.getExitCode(), execution.getExitStatus().getExitCode());
    }

    private List<Throwable> getFailureExceptions(JobExecution jobExecution) {
        List<Throwable> failureExceptions = new ArrayList<>();
        if (!jobExecution.getExitStatus().equals(ExitStatus.FAILED)) { return failureExceptions; }

        List<Throwable> jobFailureExceptions = jobExecution.getFailureExceptions();
        failureExceptions.addAll(jobFailureExceptions);

        for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
            List<Throwable> stepFailureExceptions = stepExecution.getFailureExceptions();
            failureExceptions.addAll(stepFailureExceptions);
        }

        return failureExceptions;
    }

}
