package pl.org.akai.kafkaintro.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import pl.org.akai.kafkaintro.model.Item;
import pl.org.akai.kafkaintro.model.ItemRequest;
import pl.org.akai.kafkaintro.model.KafkaMessage.ItemToProcessMessage;
import pl.org.akai.kafkaintro.model.Status;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;


@Service
public class ItemCreateService {

    private final ItemDatabaseService itemDatabaseService;
    /* TODO */
    private final Logger logger = LoggerFactory.getLogger(ItemCreateService.class);

    public ItemCreateService(ItemDatabaseService itemDatabaseService /* TODO */) {
        this.itemDatabaseService = itemDatabaseService;
        /* TODO */
    }

    public Mono<Item> addItem(ItemRequest request) {
        var item = new Item(UUID.randomUUID(), request.input(), Status.WAITING_FOR_PROCESSING.name());
        return sendToKafka(item)
                .doOnNext(i -> itemDatabaseService.createIfNotExists(i)
                        .subscribeOn(Schedulers.boundedElastic())
                        .subscribe()
                );
    }

    private Mono<Item> sendToKafka(Item item) {
        var message = ItemToProcessMessage.newBuilder()
                .setUuid(item.uuid().toString())
                .setInput(item.input())
                .setStatus(item.status())
                .build();

        return Mono.fromFuture(/* TODO */)
                .doOnSuccess(sendResult -> logger.info("Sent item to Kafka: {}", message.getUuid()))
                .doOnError(err -> logger.error("Failed to save item to Kafka: {}", err.toString()))
                .thenReturn(item);
    }
}

