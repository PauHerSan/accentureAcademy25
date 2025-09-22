package com.superStore.superStoreData.storeConfiguration;

import com.superStore.superStoreData.storeModel.customer;
import org.springframework.batch.item.ItemProcessor;

public class educationProcessor implements ItemProcessor<customer, customer> {

    @Override
    public customer process(customer customer) throws Exception {
        if(customer.getEducation().equals("PhD") &&
                customer.getMarital_Status().equals("Single")) {
            return customer;
        }else{
            return null;
        }
    }
}
