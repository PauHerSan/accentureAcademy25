package com.superComics.inventory.orders.events;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
@Builder
public class orderCompleteEvent {

      Long orderId;
      Long traderId;
      String orderType; // acquisition o sale_trade
      Double totalAmount;
      Instant timestamp = Instant.now();

    /**
     * DTO interno para representar los items dentro del evento.
     */
    @Value
    @Builder
    public static class OrderItemDetails {
          Long comicItemId;
          Integer quantity;
          Double unitPrice;
    }

    List<OrderItemDetails> items;

}
