package pl.org.akai.kafkaintro.web;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pl.org.akai.kafkaintro.model.Item;
import pl.org.akai.kafkaintro.model.ItemRequest;
import pl.org.akai.kafkaintro.service.ItemCreateService;
import pl.org.akai.kafkaintro.service.ItemDatabaseService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/api/items")
public class RestApiController {
    private final ItemCreateService itemCreateService;
    private final ItemDatabaseService itemDatabaseService;

    public RestApiController(ItemCreateService itemCreateService, ItemDatabaseService itemDatabaseService) {
        this.itemCreateService = itemCreateService;
        this.itemDatabaseService = itemDatabaseService;
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Item> streamItems() {
        return Flux.concat(
                itemDatabaseService.getItems(),
                Flux.interval(Duration.ofSeconds(5))
                        .flatMap(tick -> itemDatabaseService.getItems())
                        .repeat()
        );
    }

    @PostMapping
    public Mono<Item> addItem(@RequestBody ItemRequest itemRequest) {
        return itemCreateService.addItem(itemRequest);
    }
}
