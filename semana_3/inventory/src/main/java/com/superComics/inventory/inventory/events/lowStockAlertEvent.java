package com.superComics.inventory.inventory.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class lowStockAlertEvent {

    private Long id;
    private String title;
    private Integer currentStock;
    private Integer minimalStock;


}
