package pl.org.akai.kafkaintro.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.org.akai.kafkaintro.model.Item;
import pl.org.akai.kafkaintro.model.KafkaMessage.ItemToProcessMessage;
import pl.org.akai.kafkaintro.model.KafkaMessage.ItemToSaveMessage;
import pl.org.akai.kafkaintro.model.Status;
import reactor.core.publisher.Mono;

import java.util.UUID;


@Service
public class ItemProcessingService {

    private final ItemDatabaseService itemDatabaseService;
    /* TODO */
    private final Logger logger = LoggerFactory.getLogger(ItemProcessingService.class);

    public ItemProcessingService(ItemDatabaseService itemDatabaseService, /* TODO */) {
        this.itemDatabaseService = itemDatabaseService;
        /* TODO */
    }

    // TODO
    public void listen(ConsumerRecord<String, ItemToProcessMessage> record) {
        logger.info("Received ItemToProcessMessage: {}", /* TODO */);
        /* TODO */

        itemDatabaseService.upsertItem(item)
                .then(/* TODO */)
                .doOnSuccess(savedItem -> logger.info("Saved item to database: {}", outMessage.getUuid()))
                .doOnError(error -> logger.error("Failed to save item to database: {}", outMessage.getUuid()))
                .doOnError(Throwable::printStackTrace)
                .subscribe();
    }
}

