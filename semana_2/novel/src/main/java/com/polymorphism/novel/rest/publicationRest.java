package com.polymorphism.novel.rest;

import com.polymorphism.novel.model.publication;
import com.polymorphism.novel.repository.novelRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/publications")
public class publicationRest {

    @Autowired
    private novelRepo novelRepo;

    // GET: Obtener todas las publicaciones
    @GetMapping
    public ResponseEntity<List<publication>> getAllPublications() {
        List<publication> publications = novelRepo.findAll();
        if (publications.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(publications, HttpStatus.OK);
    }

    // GET: Obtener publicaciones por título o escritor
    @GetMapping("/search")
    public ResponseEntity<List<publication>> getPublicationsByCriteria(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String writer) {

        List<publication> publications = List.of();

        if (title != null) {
            publications = novelRepo.findByTitleContainingIgnoreCase(title);
        } else if (writer != null) {
            publications = novelRepo.findByWriterContainingIgnoreCase(writer);
        }

        if (publications.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(publications, HttpStatus.OK);
    }

    // GET: Obtener publicaciones por género
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<publication>> getPublicationsByGenre(@PathVariable String genre) {
        List<publication> publications = novelRepo.findAll().stream()
                .filter(p -> p.getClass().getSimpleName().equalsIgnoreCase(genre))
                .collect(Collectors.toList());

        if (publications.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(publications, HttpStatus.OK);
    }

    // POST: Crear una nueva publicación
    @PostMapping
    public ResponseEntity<publication> createPublication(@RequestBody publication publication) {
        publication savedPublication = novelRepo.save(publication);
        return new ResponseEntity<>(savedPublication, HttpStatus.CREATED);
    }

    // DELETE: Eliminar una publicación por título
    @DeleteMapping("/{title}")
    public ResponseEntity<Void> deletePublicationByTitle(@PathVariable String title) {
        try {
            novelRepo.deleteByTitle(title);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }


}//Rest
