package com.superComics.inventory.batch;

import com.superComics.inventory.batch.jobLauncherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/batch")
public class batchController {

    private final jobLauncherService jobLauncherService;

    public batchController(jobLauncherService jobLauncherService) {
        this.jobLauncherService = jobLauncherService;
    }

    @PostMapping("/run-inventory-report")
    public ResponseEntity<String> runReportJob() {
        String result = jobLauncherService.runInventoryReportJob("inventoryReportJob");
        return ResponseEntity.ok(result);
    }

}
