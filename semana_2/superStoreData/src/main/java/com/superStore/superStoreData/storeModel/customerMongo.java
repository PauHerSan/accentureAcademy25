package com.superStore.superStoreData.storeModel;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "COSTUMERS")
public class customerMongo {

    @Id
    private int Id;
    private int Year_Birth;
    private String Education;
    private String Marital_Status;
    private double Income;
    private int MntFruits;
    private int MntMeatProducts;
    private int MntFishProducts;
    private int MntSweetProducts;


}
