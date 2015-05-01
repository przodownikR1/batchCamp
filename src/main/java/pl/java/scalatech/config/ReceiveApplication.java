package pl.java.scalatech.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;

import pl.java.scalatech.jms.Receiver;

@Configuration
@EnableJms
@Import(JmsConfig.class)
public class ReceiveApplication {

    public final static String DESC = "queue.A";

    @Bean
    public Receiver receiver() {
        return new Receiver();
    }

}