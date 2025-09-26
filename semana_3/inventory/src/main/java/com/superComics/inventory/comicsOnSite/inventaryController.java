package com.superComics.inventory.comicsOnSite;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/inventary")
public class inventaryController {

    private final inventoryService inventoryService;

    public inventaryController(inventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/all-comics")
    public ResponseEntity<List<comic>> getAllComics(){
        List<comic> comics = inventoryService.getAllComics();
        return new ResponseEntity<>(comics, HttpStatus.OK);
    }

    @GetMapping("/withStock")
    public ResponseEntity<List<comic>> getComicsWithStock(){
        List<comic> comics = inventoryService.getComicsWithStock();
    }
}
