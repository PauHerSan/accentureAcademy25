package com.superComics.inventory.shared;

import lombok.Data;

@Data
public class traderCreationDTO {

    private String name;
    private String email;
    private String speciality;
    private double usualPrice;
    private String address;
}
