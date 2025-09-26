package com.superComics.inventory.orders;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="orderItem")
public class orderItem {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "orderId")
    private Long orderId;

    @Column(name = "comicId")
    private Long comicId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unitPrice")
    private Double unitPrice;

    //GemMint (GM), NearMintMint (NMM), NearMint (NM)
    //VeryFine (VF), Fine (FN), VeryGood(VG),
    //Good(GD) y FairRegular(FR)
    @Column(name = "itemGrading")
    private String itemGrading;




}
