package ru.syntez.camel.rabbit.component;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.converter.jaxb.JaxbDataFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.syntez.camel.rabbit.entities.RoutingDocument;
import ru.syntez.camel.rabbit.exceptions.RouterException;

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

    private final CamelErrorProcessor errorProcessor;
    public CamelRouteBuilder(CamelErrorProcessor errorProcessor) {
        this.errorProcessor = errorProcessor;
    }

    @Override
    public void configure() throws Exception {

        JAXBContext context = JAXBContext.newInstance(RoutingDocument.class);
        xmlDataFormat.setContext(context);

        onException(Exception.class).process(errorProcessor).log("******** ERROR ON ROUTING ")
                .handled(true)
                .redeliveryDelay(10000)  // 10 секунд
                .maximumRedeliveries(5); // 5 попыток, пересылки

        from(queueInputEndpoint)
            .log("******** ROUTING FROM INPUT QUEUE TO OUTPUT")
            .to(queueOutputOrderEndpoint);

        from(queueOutputOrderEndpoint)
                .transacted()
            .doTry().unmarshal(xmlDataFormat)
            .log("******** PROCESS MESSAGE FROM OUTPUT QUEUE")
            //.to("bean:camelConsumer?method=execute(${body})");
            .doTry()
                .bean("camelConsumer", "execute(${body})")
            .doCatch(RouterException.class)
                .rollback()
            .end();
    }
}
