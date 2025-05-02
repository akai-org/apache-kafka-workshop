package pl.org.akai.kafkaintro.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import pl.org.akai.kafkaintro.model.Item;
import pl.org.akai.kafkaintro.model.KafkaMessage.ItemToSaveMessage;
import pl.org.akai.kafkaintro.model.Status;

import java.util.UUID;


@Service
public class ItemCompleteService {

    private final ItemDatabaseService itemDatabaseService;
    private final Logger logger = LoggerFactory.getLogger(ItemCompleteService.class);

    public ItemCompleteService(ItemDatabaseService itemDatabaseService) {
        this.itemDatabaseService = itemDatabaseService;
    }

    // TODO
    public void listen(ConsumerRecord<String, ItemToSaveMessage> record) {
        logger.info("Received ItemToSaveMessage: {}", /* TODO */);
        var inMessage = /* TODO */
        var item = new Item(
                UUID.fromString(inMessage.getUuid()),
                Status.SUCCESS.name(),
                inMessage.getResult()
        );
        itemDatabaseService.upsertItem(item).subscribe();
    }
}

