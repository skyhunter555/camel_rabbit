package ru.syntez.camel.rabbit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.rabbitmq.jms.admin.RMQConnectionFactory;
import com.rabbitmq.jms.admin.RMQDestination;
import com.rabbitmq.jms.admin.RMQObjectFactory;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

@Configuration
public class JmsConfig {

    @Value("${camel.component.rabbitmq.host}")
    private String brokerHost = "localhost";

    @Value("${camel.component.rabbitmq.port}")
    private Integer brokerPort = 5672;

    @Value("${camel.component.rabbitmq.vhost}")
    private String virtualHost = "user";

    @Value("${camel.component.rabbitmq.username}")
    private String username = "user";

    @Value("${camel.component.rabbitmq.password}")
    private String password = "user";

    @Bean
    public Destination jmsInputDestination() {
        RMQDestination jmsDestination = new RMQDestination();
        jmsDestination.setAmqp(false);
        jmsDestination.setQueue(true);
        jmsDestination.setAmqpRoutingKey("inputqueue");
        jmsDestination.setAmqpExchangeName("documentExchange");
        jmsDestination.setDestinationName("inputqueue");
        jmsDestination.setAmqpQueueName("inputqueue");
        return jmsDestination;
    }

    @Bean
    public Destination jmsOutputDestination() {
        RMQDestination jmsDestination = new RMQDestination();
        jmsDestination.setAmqp(false);
        jmsDestination.setQueue(true);
        jmsDestination.setAmqpRoutingKey("outputorder");
        jmsDestination.setAmqpExchangeName("documentOrderExchange");
        jmsDestination.setDestinationName("outputorder");
        jmsDestination.setAmqpQueueName("outputorder");
        return jmsDestination;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        RMQConnectionFactory connectionFactory = new RMQConnectionFactory();
        connectionFactory.setHost(brokerHost);
        connectionFactory.setPort(brokerPort);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JmsTransactionManager transactionManager = new JmsTransactionManager();
        transactionManager.setConnectionFactory(connectionFactory());
        return transactionManager;
    }
}
