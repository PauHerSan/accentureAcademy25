package com.superComics.inventory.comicsOnSite;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/inventary")
public class inventaryController {

    private final inventoryService inventoryService;

    public inventaryController(inventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    //Obtener todos los comics
    @GetMapping("/all-comics")
    public ResponseEntity<List<comic>> getAllComics(){
        List<comic> comics = inventoryService.getAllComics();
        return new ResponseEntity<>(comics, HttpStatus.OK);
    }

    //Ver si están disponibles
    @GetMapping("/{comicId}/avialable")
    public ResponseEntity<Boolean> verifyStock(
            @PathVariable Long comicId,
            @RequestParam int number){
        boolean available = inventoryService.verifyStock(comicId, number);
        return new ResponseEntity<>(available, HttpStatus.OK);
    }

    //Ver si tienen stock bajo
    @GetMapping("/lowStock")
    public ResponseEntity<List<comic>> getComicsWithLowStock(){
        List<comic> comics = inventoryService.getComicsWithLowStock();
        return new ResponseEntity<>(comics, HttpStatus.OK);
    }

    //Obtener comics por titulo
    @GetMapping("/by-title/{title}")
    public ResponseEntity<List<comic>> getComicsByTitle(@PathVariable String title){
        List<comic> comics = inventoryService.getComicsByTitle(title);
        if(title == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(comics);
    }

    //Obtener por publisher
    @GetMapping("/publisher/{publisher}")
    public ResponseEntity<List<comic>> getComicsByPublisher(@PathVariable String publisher){
        List<comic> comics = inventoryService.getComicsByPublisher(publisher);
        if(publisher == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(comics);
    }

    //Obtener por Id
    @GetMapping("/id/{id}")
    public ResponseEntity<List<comic>> getComicsById(@PathVariable Long id){
        List<comic> comics = Collections.singletonList(inventoryService.getComicsById(id));
        if(id == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(comics);
    }

    //Crear un nuevo comic
    @PostMapping("/new-comic")
    public ResponseEntity<comic>  newComic(
            @RequestParam Long id,
            @RequestParam String sku,
            @RequestParam String title,
            @RequestParam Integer issueNumber,
            @RequestParam String publisher,
            @RequestParam double estimatedValue,
            @RequestParam Integer currentStock,
            @RequestParam Integer minimalStock,
            @RequestParam String grading,
            @RequestParam Long traderId){
        comic newComic = inventoryService.newComic(id, sku, title, issueNumber, publisher,
                estimatedValue, currentStock,
                minimalStock, grading, traderId);
        return ResponseEntity.ok(newComic);
    }

    //Reducir Stock
    @PostMapping("/{id}/lowStock")
    public ResponseEntity<comic>  lowStock(
            @PathVariable Long id,
            @RequestParam int number){
        inventoryService.byeStocks(id, number);
        return ResponseEntity.ok().build();
    }

    //Aumentar Stock
    @PostMapping("/{id}/plusStock")
    public ResponseEntity<comic>  plusStock(
            @PathVariable Long id,
            @RequestParam int number){
        inventoryService.plusStocks(id, number);
        return ResponseEntity.ok().build();
    }

}
