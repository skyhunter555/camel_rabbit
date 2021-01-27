package ru.syntez.camel.rabbit.component;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.syntez.camel.rabbit.entities.RoutingDocument;
import javax.xml.bind.JAXBContext;

/**
 * Configuration custom CamelRouteBuilder
 *
 * @author Skyhunter
 * @date 26.01.2021
 */
@Component
public class CamelRouteBuilder extends RouteBuilder {

    @Value("${server.rabbitmq.queues.input-endpoint}")
    private String queueInputEndpoint = "inputqueue";

    @Value("${server.rabbitmq.queues.output-order-endpoint}")
    private String queueOutputOrderEndpoint = "outputorder";

    private JaxbDataFormat xmlDataFormat = new JaxbDataFormat();

    @Override
    public void configure() throws Exception {

        JAXBContext context = JAXBContext.newInstance(RoutingDocument.class);
        xmlDataFormat.setContext(context);

        from(queueInputEndpoint)
            .log("******** ROUTING FROM INPUT QUEUE TO OUTPUT")
            .to(queueOutputOrderEndpoint);

        from(queueOutputOrderEndpoint)
            .doTry().unmarshal(xmlDataFormat)
            .log("******** PROCESS MESSAGE FROM OUTPUT QUEUE")
            .to("bean:camelConsumer?method=execute(${body})");
    }
}
