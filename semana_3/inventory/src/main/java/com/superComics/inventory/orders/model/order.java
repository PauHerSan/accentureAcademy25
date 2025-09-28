package com.superComics.inventory.orders.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="orders")
public class order {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // type: acquisition (compra a trader), sale_trade (venta a trader)
    @Column(name = "type")
    private String type;

    //status: pending, transit, complete, canceled
    @Column(name = "status")
    private String status;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "createdDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "traderId")
    private Long traderId;

    // Relación con los items de la orden (detalles)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<orderItem> items;

    // Constructor sin la lista de items (útil para la creación inicial)
    public order(Long id, String type, String status, Double totalAmount, Date createdDate, Long traderId) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdDate = createdDate;
        this.traderId = traderId;
    }

    /**
     * Marca la orden como completada, aplicando lógica de negocio si es necesario.
     */
    public void complete() {
        if (!"complete".equalsIgnoreCase(this.status)) {
            this.status = "complete";
        }
        // Lógica adicional, como actualizar la fecha de finalización, si fuera necesaria
    }


}
