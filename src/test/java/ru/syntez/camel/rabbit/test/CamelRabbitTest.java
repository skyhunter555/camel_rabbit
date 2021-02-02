package ru.syntez.camel.rabbit.test;

import com.rabbitmq.jms.admin.RMQConnectionFactory;
import com.rabbitmq.jms.admin.RMQDestination;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
public class CamelRabbitTest {

    @Test
    public void sendMessageToInputQueueTest() {
        RMQConnectionFactory connectionFactory = new RMQConnectionFactory();
        connectionFactory.setUsername("user");
        connectionFactory.setPassword("user");
        connectionFactory.setVirtualHost("user");
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);

        RMQDestination jmsDestination = new RMQDestination();
        jmsDestination.setAmqp(false);
        jmsDestination.setQueue(true);
        jmsDestination.setAmqpExchangeName("documentExchange");
        jmsDestination.setAmqpRoutingKey("inputqueue");
        jmsDestination.setDestinationName("inputqueue");
        jmsDestination.setAmqpQueueName("inputqueue");

        Connection connection = null;

        try {
            connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(true, Session.SESSION_TRANSACTED);
            MessageProducer producer = session.createProducer(jmsDestination);

            String messageXml = new String(Files.readAllBytes(Paths.get(getClass().getResource("/router_doc_1.xml").toURI())));
            TextMessage textMessage = session.createTextMessage("Test message");
            textMessage.setJMSType("TextMessage");
            textMessage.setText(messageXml);
            producer.send(textMessage);
            session.commit();
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
