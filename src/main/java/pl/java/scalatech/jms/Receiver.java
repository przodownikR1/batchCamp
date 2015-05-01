package pl.java.scalatech.jms;

import lombok.extern.slf4j.Slf4j;

import org.springframework.jms.annotation.JmsListener;

@Slf4j
public class Receiver {
    @JmsListener(destination = "queue.A")
    public void receiveMessage(String message) {
        log.info("+++     Received < " + message + " >");
    }
}