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
    private final KafkaTemplate<String, ItemToSaveMessage> kafkaTemplate;
    private final Logger logger = LoggerFactory.getLogger(ItemProcessingService.class);

    public ItemProcessingService(ItemDatabaseService itemDatabaseService, KafkaTemplate<String, ItemToSaveMessage> kafkaTemplate) {
        this.itemDatabaseService = itemDatabaseService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "items-to-process", groupId = "my-group", containerFactory="itemToProcessKafkaListenerContainerFactory")
    public void listen(ConsumerRecord<String, ItemToProcessMessage> record) {
        logger.info("Received ItemToProcessMessage: {}", record.key());
        var inMessage = record.value();
        var input = inMessage.getInput();
        var item = new Item(UUID.fromString(inMessage.getUuid()), inMessage.getInput(), Status.PENDING.name());
        var letterCount = input.chars()
                .filter(Character::isLetter)
                .count();

        var outMessage = ItemToSaveMessage.newBuilder()
                .setUuid(inMessage.getUuid())
                .setResult((int) letterCount)
                .build();

        itemDatabaseService.upsertItem(item)
                .then(Mono.fromFuture(kafkaTemplate.send("processed-items", outMessage.getUuid(), outMessage)))
                .doOnSuccess(savedItem -> logger.info("Saved item to database: {}", outMessage.getUuid()))
                .doOnError(error -> logger.error("Failed to save item to database: {}", outMessage.getUuid()))
                .doOnError(Throwable::printStackTrace)
                .subscribe();
    }
}
