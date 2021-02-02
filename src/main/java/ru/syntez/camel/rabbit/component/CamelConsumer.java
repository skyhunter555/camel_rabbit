package ru.syntez.camel.rabbit.component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.syntez.camel.rabbit.entities.RoutingDocument;
import ru.syntez.camel.rabbit.exceptions.RouterException;

/**
 * Configuration custom CamelRouteBuilder
 *
 * @author Skyhunter
 * @date 26.01.2021
 */
@Component
public class CamelConsumer {

    private static Logger LOG = LogManager.getLogger(CamelConsumer.class);

    @Value("${server.work-time}")
    private Integer delayMillis = 100;
    private Integer consumedDocumentCount = 0;

    public void execute(RoutingDocument document) {

        LOG.info("START CONSUME MESSAGE, docId: {} docType: {}", document.getDocId(), document.getDocType());
        if (consumedDocumentCount > 10) {
            throw new RouterException("ConsumedDocumentCount > 10. Rollback");
        }
        try {
            Thread.sleep(delayMillis);
            consumedDocumentCount++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOG.info("FINISH CONSUME MESSAGE, docId: {} docType: {}. Total consumed: {}", document.getDocId(), document.getDocType(), consumedDocumentCount);
    }
}
