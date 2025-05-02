package pl.org.akai.kafkaintro.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("item")
public record Item(
        @Id Long id,
        UUID uuid,
        String input,
        String status,
        Long result
) {
    public Item(UUID uuid, String input, String status) {
        this(null, uuid, input, status, null);
    }

    public Item(UUID uuid, String status, long result) {
        this(null, uuid, null, status, result);
    }
}

