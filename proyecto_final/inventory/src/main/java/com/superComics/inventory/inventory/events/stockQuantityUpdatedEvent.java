package com.superComics.inventory.inventory.events;


import lombok.Builder;
import lombok.Value;

import java.time.Instant;

//se publica cuando la cantidad de stock de un cómic se ha modificado.
@Value
@Builder
public class stockQuantityUpdatedEvent {

    Long comicId;
    String sku;
    int oldStock;
    int newStock;
    int changeQuantity;

    @Builder.Default
    Instant timestamp = Instant.now();


}
