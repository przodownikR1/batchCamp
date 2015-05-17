package pl.java.scalatech.retry;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
//https://github.com/bijukunjummen/test-spring-retry.git
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= {SpringConfig.class})
public class SpringDirectRetryTemplateTests {

    @Autowired
    private RemoteCallService remoteCallService;

    @Autowired
    private RetryTemplate retryTemplate;

    @Test
    @Ignore
    //TODO check autowired retryTemplate
    public void testRetry() throws Exception {
        String message = this.retryTemplate.execute(context -> this.remoteCallService.call());
        verify(remoteCallService, times(3)).call();
        assertThat(message, is("Completed"));
    }

  
}