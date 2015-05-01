package pl.java.scalatech.config;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@ComponentScan(basePackages="pl.java.scalatech.jms")
public class JmsConfig {

   /* @Bean(initMethod = "start", destroyMethod = "stop")
    public BrokerService brokerService() throws Exception {
        BrokerService brokerService = new BrokerService();
        brokerService.setBrokerName("testBroker");
        brokerService.setPersistent(false);
        brokerService.setUseShutdownHook(false);
        brokerService.setEnableStatistics(true);
        brokerService.addConnector("tcp://localhost:61616");
        brokerService.getSystemUsage().getTempUsage().setLimit(1024L * 1024L);
        brokerService.getSystemUsage().getMemoryUsage().setLimit(1024L * 1024L * 64L);

        return brokerService;
    }*/

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrency("1-3");
        return factory;
    }

    @Bean
    public JmsMessagingTemplate jmsMessagingTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        JmsMessagingTemplate jmsMessagingTemplate = new JmsMessagingTemplate(jmsTemplate);
        jmsMessagingTemplate.setDefaultDestination(destination());
        return jmsMessagingTemplate;
    }

    @Bean
    public ActiveMQConnectionFactory amqConnectionFactory() {
        return new ActiveMQConnectionFactory("vm://embedded?broker.persistent=false");
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        final CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(amqConnectionFactory());
        cachingConnectionFactory.setSessionCacheSize(100);
        cachingConnectionFactory.setCacheProducers(true);
        cachingConnectionFactory.setReconnectOnException(true);
        return cachingConnectionFactory;
    }

    @Bean
    public ActiveMQQueue destination() {
        return new ActiveMQQueue("queue.A");
    }

    @Bean
    @Lazy
    public JmsTemplate jmsTemplate() {
        final JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
        jmsTemplate.setDefaultDestination(destination());
        jmsTemplate.setReceiveTimeout(1000);
        return jmsTemplate;
    }
}