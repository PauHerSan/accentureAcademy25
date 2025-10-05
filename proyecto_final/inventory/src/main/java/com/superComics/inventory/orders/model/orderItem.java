package com.superComics.inventory.orders.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="orderItem")
public class orderItem {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ID del cómic en el módulo inventory
    @Column(name = "comicItemId", nullable = false)
    private Long comicItemId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unitPrice", nullable = false)
    private Double unitPrice;

    // Relación ManyToOne a la Order principal
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private order order;

    /**
     * Constructor para crear un item de orden antes de asignarlo a una orden persistente.
     */
    public orderItem(Long comicItemId, Integer quantity, Double unitPrice) {
        this.comicItemId = comicItemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    /**
     * Calcula el subtotal para este item.
     * @return El costo total (cantidad * precio unitario).
     */
    public double getSubtotal() {
        return this.quantity * this.unitPrice;
    }

    //GemMint (GM), NearMintMint (NMM), NearMint (NM)
    //VeryFine (VF), Fine (FN), VeryGood(VG),
    //Good(GD) y FairRegular(FR)
}
