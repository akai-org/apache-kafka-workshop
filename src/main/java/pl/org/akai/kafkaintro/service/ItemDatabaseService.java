package pl.org.akai.kafkaintro.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.org.akai.kafkaintro.model.Item;
import pl.org.akai.kafkaintro.repository.ItemRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ItemDatabaseService {

    private final ItemRepository itemRepository;
    private final Logger logger = LoggerFactory.getLogger(ItemDatabaseService.class);

    public ItemDatabaseService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Flux<Item> getItems() {
        return itemRepository.findAll();
    }

    @Transactional
    public Mono<Item> createIfNotExists(Item item) {
        return itemRepository.findByUuid(item.uuid())
                .doOnNext(existingItem -> logger.info("Item already exists: {}", existingItem.uuid()))
                .switchIfEmpty(saveItem(item));
    }

    @Transactional
    public Mono<Item> upsertItem(Item item) {
        return itemRepository.findByUuid(item.uuid())
                .flatMap(oldItem -> itemRepository.updateStatusAndResultByUuid(item.status(), item.result(), item.uuid()))
                .doOnNext(count -> logger.info("Updated item in database: {}", count))
                .doOnError(error -> logger.error("Failed to update item to database: {}", item.uuid()))
                .thenReturn(item)
                .switchIfEmpty(saveItem(item));
    }

    private Mono<Item> saveItem(Item item) {
        return itemRepository.save(item)
                .doOnSuccess(savedItem -> logger.info("Saved item to database: {}", savedItem.uuid()))
                .doOnError(error -> logger.error("Failed to save item to database: {}", item.uuid()));
    }
}
