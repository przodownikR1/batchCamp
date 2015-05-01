package pl.java.scalatech;

import java.util.HashMap;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import lombok.extern.slf4j.Slf4j;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.messaging.MessageHeaders;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.java.scalatech.config.JmsConfig;
import pl.java.scalatech.config.ReceiveApplication;
import pl.java.scalatech.jms.QueueSender;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { JmsConfig.class,ReceiveApplication.class })
@Slf4j
public class JmsConfigTest {

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private JmsMessagingTemplate jmsMessageTemplate;
    @Autowired
    private QueueSender queueSender;
    @AfterClass
    public static void afterClass() throws Exception {
        Thread.sleep(1000);
    }
    @Test
    public void shouldJmsMessagingTemplateWork(){
        jmsMessageTemplate.send(new org.springframework.messaging.Message<String>() {

            @Override
            public String getPayload() {
                return "slawek";
            }

            @Override
            public MessageHeaders getHeaders() {
                return new MessageHeaders(new HashMap<String, Object>());
            }
        });
    }
    
    @Test
    public void shouldConfigBootstrap() {
        MessageCreator messageCreator = new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("Hello World!");
            }
        };

        log.info("Sending a message...");
        jmsTemplate.send("queue.A", messageCreator);
    }

    @Test
    public void shouldQueueSenderWork(){
        queueSender.send("Hello przodownik..");
    }
    
}
