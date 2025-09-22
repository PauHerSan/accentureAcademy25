package com.superStore.superStoreData.storeController;

import com.superStore.superStoreData.storeRepo.storeMongoRepository;
import com.superStore.superStoreData.storeRepo.storeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class dataVerificationController {

    @Autowired
    private storeRepository storeRepository;

    @Autowired
    private storeMongoRepository storeMongoRepository;

    @GetMapping("/verify-data")
    public Map<String, Object> verifyData() {
        Map<String, Object> result = new HashMap<>();

        long h2Count = storeRepository.count();
        long mongoCount = storeMongoRepository.count();

        result.put("h2_database_records", h2Count);
        result.put("mongodb_records", mongoCount);
        result.put("data_integrity_check", h2Count == mongoCount ? "PASSED" : "FAILED");
        result.put("batch_migration_status", h2Count > 0 && mongoCount > 0 ? "SUCCESS" : "INCOMPLETE");

        return result;
    }
}
