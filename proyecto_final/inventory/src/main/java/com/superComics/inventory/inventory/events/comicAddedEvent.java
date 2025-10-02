package com.superComics.inventory.inventory.events;


import lombok.Builder;
import lombok.Value;

import java.time.Instant;

//Se publica cuando un nuevo cómic ha sido agregado al inventario.
@Value
@Builder
public class comicAddedEvent {

    Long comicId;
    String sku;
    String title;
    String publisher;
    int currentStock;

    @Builder.Default
    Instant timestamp = Instant.now();
}
