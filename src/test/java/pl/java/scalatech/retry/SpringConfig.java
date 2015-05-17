package pl.java.scalatech.retry;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@EnableRetry
@Slf4j
public class SpringConfig {

    @Bean
    public RemoteCallService remoteCallService() throws Exception {
        log.info("remoteCallService init bean");
        RemoteCallService remoteService = mock(RemoteCallService.class);
        when(remoteService.call()).thenThrow(new RuntimeException("one exception")).thenThrow(new RuntimeException("second exception")).thenReturn("Completed");
        return remoteService;
    }

    @Bean
    public RetryTemplate retryTemplate() {
        log.info("retryTemplate init bean");
        RetryTemplate retryTemplate = new RetryTemplate();
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(2000l);
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);

        retryTemplate.setRetryPolicy(retryPolicy);
        return retryTemplate;
    }

}