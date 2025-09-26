package com.superComics.inventory.orders;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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

    //type: acquisition, sale_trade
    @Column(name = "type")
    private String type;

    //status: pending, transit, complete
    @Column(name = "status")
    private String status;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "cratedDate")
    private Date cratedDate;

    @Column(name = "traderId")
    private Long traderId;



}
