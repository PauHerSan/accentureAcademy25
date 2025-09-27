package com.superComics.inventory.inventory.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="comics")
public class comic {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sku")
    private String sku;

    @Column(name ="title")
    private String title;

    @Column(name = "issueNumber") //numero de publicacion
    private int issueNumber;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "estimatedValue")
    private double estimatedValue;

    @Column(name = "currentStock", nullable = false)
    private Integer currentStock;

    @Column(name = "minimalStock", nullable = false)
    private Integer minimalStock;

    //GemMint (GM), NearMintMint (NMM), NearMint (NM)
    //VeryFine (VF), Fine (FN), VeryGood(VG),
    //Good(GD) y FairRegular(FR)
    @Embedded
    @Column(name = "grading") // Estado del comic
    private grading grading;

    @Column(name = "traderId") //id del proveedor
    private long traderId;

    public void byeStock(int number){
        if (number <= 0){
            throw new IllegalArgumentException("Agregue al menos una pieza");
        }
        if (number < currentStock){
            throw new IllegalArgumentException("Hay pocas piezas disponibles. Disponibles: "+ currentStock + ", usted solicitó: " + number );
        }
        this.currentStock -= number;
    }

    public void plusStock(int number){
        if (number <= 0){
            throw new IllegalArgumentException("Revise el número de piezas");
        }
        this.currentStock += number;
    }

    public boolean needRestock(){
        return currentStock <= minimalStock;
    }

    public boolean yesStock(int number){
        return currentStock >= number;
    }

}
