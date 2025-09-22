package com.superStore.superStoreData.storeConfiguration;

import com.superStore.superStoreData.storeModel.customer;
import com.superStore.superStoreData.storeModel.customerMongo;
import org.springframework.batch.item.ItemProcessor;

public class maritalStatusProcessor implements ItemProcessor <customer, customerMongo>{

    @Override
    public customerMongo process(customer Customer) throws Exception {

        customerMongo customer = new customerMongo();

        customer.setId(Customer.getId());
        customer.setYear_Birth(Customer.getYear_Birth());
        customer.setEducation(Customer.getEducation());
        customer.setIncome(Customer.getIncome());
        customer.setMntFruits(Customer.getMntFruits());
        customer.setMntMeatProducts(Customer.getMntMeatProducts());
        customer.setMntFishProducts(Customer.getMntFishProducts());
        customer.setMntSweetProducts(Customer.getMntSweetProducts());


        if (Customer.getMarital_Status() == null) {
            String newStatus = "NO INFORMATION";
            customer.setMarital_Status(newStatus);
        }

        return customer;


    }
}
