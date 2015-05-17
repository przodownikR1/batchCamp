package pl.java.scalatech.retry;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:retry-test.xml" })
@Slf4j
public class RetryAOPTest_IT {

    @Resource
    private ExampleService service;

    @Test
    public void givenAServiceMethodWhenTryingToInvokeItThenRetry5Times() {

        try {

            service.sendMail();

            assertEquals(service.getTimes(), 4);

        } catch (Exception e) {
            log.error("Error trying to send email!");
        }

    }

}