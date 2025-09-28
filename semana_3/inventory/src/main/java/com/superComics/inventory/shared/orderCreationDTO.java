package com.superComics.inventory.shared;

import lombok.Data;

import java.util.List;

@Data
public class orderCreationDTO {
    private String type;
    private Long traderId;
    private List<OrderItemRequest> items;

    /**
     * DTO interno para el detalle de cada item de la orden.
     */
    @Data
    public static class OrderItemRequest {
        private Long comicItemId;
        private Integer quantity;
        private Double unitPrice;
    }

}
