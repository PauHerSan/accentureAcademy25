package com.superStore.superStoreData.storeRepo;

import com.superStore.superStoreData.storeModel.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface storeRepository extends JpaRepository<customer,Integer> {


}
