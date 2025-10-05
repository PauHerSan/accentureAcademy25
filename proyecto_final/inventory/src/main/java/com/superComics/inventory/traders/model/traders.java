package com.superComics.inventory.traders.model;

import jakarta.persistence.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="traders")
public class traders {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name",  nullable = false)
    private String name;

    @Column(name = "email",  nullable = false)
    private String email;

    @Column(name = "speciality")
    private String speciality;

    @Column(name = "usual_price_variance")
    private double usualPrice;

    @Column(name = "address")
    private String address;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;
}
