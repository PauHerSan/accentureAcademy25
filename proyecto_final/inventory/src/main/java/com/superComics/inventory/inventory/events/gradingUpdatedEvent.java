package com.superComics.inventory.inventory.events;

import lombok.*;

import java.time.Instant;

@Value
@Builder
public class gradingUpdatedEvent {

    Long comicId;
    String sku;
    String oldGradingCode;
    String newGradingCode;

    @Builder.Default
    Instant timestamp = Instant.now();


}
