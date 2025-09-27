package com.superComics.inventory.inventory.events;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class gradingUpdatedEvent {

    private Long id;
    private String title;
    private String sku;
    private String previousGrading;
    private String newGrading;


}
