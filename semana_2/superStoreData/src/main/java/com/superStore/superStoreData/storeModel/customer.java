package com.superStore.superStoreData.storeModel;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "COSTUMER_DATA")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class customer {

    @Id
    @Column(name = "ID")
    private int Id;

    @Column(name = "YEAR_BIRTH")
    private int Year_Birth;

    @Column(name = "EDUCATION")
    private String Education;

    @Column(name = "MARITAL_STATUS")
    private String Marital_Status;

    @Column(name = "INCOME")
    private double Income;

    @Column(name = "MNT_FRUITS")
    private int MntFruits;

    @Column(name = "MNT_MEAT_PRODUCTS")
    private int MntMeatProducts;

    @Column(name = "MNT_FISH_PRODUCTS")
    private int MntFishProducts;

    @Column(name = "MNT_SWEET_PRODUCTS")
    private int MntSweetProducts;


}
