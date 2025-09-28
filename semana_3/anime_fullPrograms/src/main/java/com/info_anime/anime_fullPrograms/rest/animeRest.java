package com.info_anime.anime_fullPrograms.rest;

import com.info_anime.anime_fullPrograms.model.animeModel;
import com.info_anime.anime_fullPrograms.service.animeServiceImplements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/anime")
public class animeRest {

    @Autowired
    private animeServiceImplements animeService;

    // Obtener todos los animes
    @GetMapping
    public ResponseEntity<List<animeModel>> getAllAnimes() {
        try {
            List<animeModel> animes = animeService.getAllAnimes();
            return ResponseEntity.ok(animes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Obtener anime por ID
    @GetMapping("/{id}")
    public ResponseEntity<animeModel> getAnimeById(@PathVariable String id) {
        try {
            return animeService.getAnimeById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Obtener animes por título
    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<animeModel>> getAnimesByTitulo(@PathVariable String titulo) {
        try {
            List<animeModel> animes = animeService.getAnimesByTitulo(titulo);
            if (animes.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(animes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Crear un nuevo anime
    @PostMapping
    public ResponseEntity<?> createAnime(@RequestBody animeModel anime) {
        try {
            animeModel nuevoAnime = animeService.createAnime(anime);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoAnime);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error interno del servidor");
        }
    }

    // Actualizar anime por ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAnime(@PathVariable String id, @RequestBody animeModel animeDetails) {
        try {
            return animeService.updateAnime(id, animeDetails)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error interno del servidor");
        }
    }

    // Eliminar anime por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAnime(@PathVariable String id) {
        try {
            if (animeService.deleteAnimeById(id)) {
                return ResponseEntity.ok("Anime eliminado exitosamente");
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor");
        }
    }

    // Eliminar animes por título
    @DeleteMapping("/titulo/{titulo}")
    public ResponseEntity<String> deleteAnimesByTitulo(@PathVariable String titulo) {
        try {
            int deletedCount = animeService.deleteAnimesByTitulo(titulo);
            if (deletedCount == 0) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok("Se eliminaron " + deletedCount + " anime(s)");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error interno del servidor");
        }
    }

}
