package com.superComics.inventory.comicsOnSite;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{comicId}/avialable")
    public ResponseEntity<Boolean> verifyStock(
            @PathVariable Long comicId,
            @RequestParam int number){
        boolean available = inventoryService.verifyStock(comicId, number);
        return new ResponseEntity<>(available, HttpStatus.OK);
    }

    @GetMapping("/lowStock")
    public ResponseEntity<List<comic>> getComicsWithLowStock(){
        List<comic> comics = inventoryService.getComicsWithLowStock();
        return new ResponseEntity<>(comics, HttpStatus.OK);
    }

    


}
