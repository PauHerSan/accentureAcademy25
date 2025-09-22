package com.superStore.superStoreData.storeRepo;


import com.superStore.superStoreData.storeModel.customerMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface storeMongoRepository extends MongoRepository<customerMongo, String> {

}
