package pl.java.scalatech.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class QueueSender {
    @Autowired
    private JmsTemplate jmsTemplate;
    
   

    public void send(final String message) {
        log.debug("start sending : {}", message);
        jmsTemplate.convertAndSend(message);
        log.debug("end sending : {}", message);
    }
}