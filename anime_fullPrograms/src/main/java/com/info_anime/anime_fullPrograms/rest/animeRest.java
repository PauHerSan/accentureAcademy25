package com.info_anime.anime_fullPrograms.rest;

import com.info_anime.anime_fullPrograms.model.animeModel;
import com.info_anime.anime_fullPrograms.repository.animeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/animeModel")
public class animeRest {

    @Autowired
    private animeRepo animeRepo;

    @GetMapping
    public List<animeModel> getanimeModels() {
        return animeRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<animeModel> getanimeModelById(@PathVariable String id) {
        return animeRepo.findById(id)
                .map(animeModel -> ResponseEntity.ok().body(animeModel))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<animeModel>> getanimeModelsByTitulo(@PathVariable String titulo) {
        List<animeModel> list = animeRepo.findByTituloContainingIgnoreCase(titulo);
        if (list.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok().body(list);
        }
    }


    @PostMapping
    public ResponseEntity<animeModel> createanimeModel(@RequestBody animeModel anime) {
        animeModel nuevoAnime = animeRepo.save(anime);
        return new ResponseEntity<>(nuevoAnime, HttpStatus.CREATED);
    }

    @PutMapping ("/{id}")
    public ResponseEntity<animeModel> updateAnime(@PathVariable String id, @RequestBody animeModel animeDetails) {
        return animeRepo.findById(id)
                .map(anime -> {
                    anime.setTitulo(animeDetails.getTitulo());
                    anime.setEscritor(animeDetails.getEscritor());
                    anime.setDirector(animeDetails.getDirector());
                    anime.setAnoLanzamiento(animeDetails.getAnoLanzamiento());
                    anime.setNumeroCapitulos(animeDetails.getNumeroCapitulos());
                    anime.setGenero(animeDetails.getGenero());
                    animeModel animeActualizado = animeRepo.save(anime);
                    return ResponseEntity.ok().body(animeActualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/titulo/{titulo}")
    public ResponseEntity<Void> deleteByTitulo(@PathVariable String titulo) {
        List<animeModel> animesToDelete = animeRepo.findByTituloContainingIgnoreCase(titulo);
        if(animesToDelete.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        animeRepo.deleteAll(animesToDelete);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnime(@PathVariable String id) {
        if (animeRepo.existsById(id)) {
            animeRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
