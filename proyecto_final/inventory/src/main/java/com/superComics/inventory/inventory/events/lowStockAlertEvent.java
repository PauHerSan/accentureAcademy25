package com.superComics.inventory.inventory.events;

import lombok.*;

import java.time.Instant;


@Value
@Builder
public class lowStockAlertEvent {

    Long comicId;
    String title;
    int currentStock;
    int minimalStock;

    @Builder.Default
    Instant timestamp = Instant.now();


}
