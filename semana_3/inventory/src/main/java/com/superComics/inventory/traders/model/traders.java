package com.superComics.inventory.traders.model;

import jakarta.persistence.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="orders")
public class traders {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "speciality")
    private String speciality;

    @Column(name = "usualPrice+-")
    private double usualPrice;

    @Column(name = "address")
    private String address;


}
