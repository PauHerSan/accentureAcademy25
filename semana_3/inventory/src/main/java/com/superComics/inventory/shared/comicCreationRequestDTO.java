package com.superComics.inventory.shared;

import com.superComics.inventory.inventory.model.grading;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class comicCreationRequestDTO {

    private String sku;
    private String title;
    private Integer issueNumber;
    private String publisher;
    private double estimatedValue;
    private Integer currentStock;
    private Integer minimalStock;

    private grading grading;
    private Long traderId;
}
