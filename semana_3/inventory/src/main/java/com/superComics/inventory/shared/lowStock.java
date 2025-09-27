package com.superComics.inventory.shared;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class lowStock {

        Long id;
        String title;
        Integer currentStock;
        Integer minimalStock;

}
