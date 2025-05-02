package pl.org.akai.kafkaintro.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pl.org.akai.kafkaintro.model.Item;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ItemRepository extends ReactiveCrudRepository<Item, Long> {

    Mono<Item> findByUuid(UUID uuid);

    @Modifying
    @Query("UPDATE item SET status = :status, result = :result WHERE uuid = :uuid")
    Mono<Integer> updateStatusAndResultByUuid(String status, Long result, UUID uuid);
}

