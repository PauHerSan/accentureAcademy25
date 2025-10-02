package com.superComics.inventory.inventory.api;


import com.superComics.inventory.inventory.model.comicItem;
import com.superComics.inventory.inventory.service.inventoryServices;
import com.superComics.inventory.shared.comicCreationRequestDTO;
import com.superComics.inventory.shared.gradingUpdateDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventary")
public class inventaryController {

    private final inventoryServices inventoryServices;

    public inventaryController(inventoryServices inventoryServices) {
        this.inventoryServices = inventoryServices;
    }

    //Obtener todos los comics
    @GetMapping("/all-comics")
    public ResponseEntity<List<comicItem>> getAllComics(){
        List<comicItem> comics = inventoryServices.getAllComics();
        return new ResponseEntity<>(comics, HttpStatus.OK);
    }

    //Obtener cómic por ID.
    @GetMapping("/{id}")
    public ResponseEntity<comicItem> getComicsById(@PathVariable Long id){
        comicItem comic = inventoryServices.getComicsById(id);
        return ResponseEntity.ok(comic);
    }

    //Ver si están disponibles
    @GetMapping("/{comicId}/avialable")
    public ResponseEntity<Boolean> verifyStock(
            @PathVariable Long comicId,
            @RequestParam int number){
        boolean available = inventoryServices.verifyStock(comicId, number);
        return new ResponseEntity<>(available, HttpStatus.OK);
    }

    //Ver si tienen stock bajo
    @GetMapping("/low-stock")
    public ResponseEntity<List<comicItem>> getComicsWithLowStock(){
        List<comicItem> comics = inventoryServices.getComicsWithLowStock();
        return new ResponseEntity<>(comics, HttpStatus.OK);
    }

    //Obtener comics por titulo
    @GetMapping("/by-title/{title}")
    public ResponseEntity<List<comicItem>> getComicsByTitle(@PathVariable String title){
        List<comicItem> comics = inventoryServices.getComicsByTitle(title);
        return ResponseEntity.ok(comics);
    }

    //Obtener por publisher
    @GetMapping("/publisher/{publisher}")
    public ResponseEntity<List<comicItem>> getComicsByPublisher(@PathVariable String publisher){
        List<comicItem> comics = inventoryServices.getComicsByPublisher(publisher);
        return ResponseEntity.ok(comics);
    }


    //Crear un nuevo comic utilizandoDTO
    @PostMapping("/new-comic")
    public ResponseEntity<comicItem>  newComic(@RequestBody comicCreationRequestDTO request) {

        // Mapeo simple del DTO a los parámetros del servicio
        comicItem newComic = inventoryServices.newComic(
                null, // Dejamos el ID como null para que JPA lo genere
                request.getSku(),
                request.getTitle(),
                request.getIssueNumber(),
                request.getPublisher(),
                request.getEstimatedValue(),
                request.getCurrentStock(),
                request.getMinimalStock(),
                request.getGrading(),
                request.getTraderId());

        return new ResponseEntity<>(newComic, HttpStatus.CREATED);
    }

    //Reducir Stock
    @PostMapping("/{id}/reduce-stock")
    public ResponseEntity<Void>  reduceStock(
            @PathVariable Long id,
            @RequestParam int number){
        inventoryServices.byeStocks(id, number);
        return ResponseEntity.ok().build();
    }

    //Aumentar Stock
    @PostMapping("/{id}/increase-Stock")
    public ResponseEntity<Void> increaseStock(
            @PathVariable Long id,
            @RequestParam int number){
        inventoryServices.plusStocks(id, number);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/updateGrading")
    public ResponseEntity<comicItem>  updateGrading(
            @PathVariable Long id,
            @RequestBody gradingUpdateDTO updateDTO){

        comicItem updatedComic = inventoryServices.updateGrading(
                id, updateDTO.getNewGradingCode());
        return new ResponseEntity<>(updatedComic, HttpStatus.OK);
    }

}
