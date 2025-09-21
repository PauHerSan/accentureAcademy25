package com.polymorphism.novel.rest;

import com.polymorphism.novel.model.novels;
import com.polymorphism.novel.model.publication;
import com.polymorphism.novel.model.webToon;
import com.polymorphism.novel.repository.publicationRepo;
import com.polymorphism.novel.service.publicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/publications")
@CrossOrigin(origins = "*")
public class publicationRest {

    @Autowired
    private publicationService publicationService;

    // 1. CONSULTAR DE MANERA GENERAL
    @GetMapping("/all")
    public ResponseEntity<List<publication>> getAllPublications() {
        List<publication> publications = publicationService.getAllPublications();
        return ResponseEntity.ok(publications);
    }

    // Obtener solo novels
    @GetMapping("/novels")
    public ResponseEntity<List<novels>> getAllNovels() {
        List<novels> novels = publicationService.getAllNovels();
        return ResponseEntity.ok(novels);
    }

    // Obtener solo webtoons
    @GetMapping("/webtoons")
    public ResponseEntity<List<webToon>> getAllWebToons() {
        List<webToon> weebToon = publicationService.getAllWebToons();
        return ResponseEntity.ok(weebToon);
    }

    // 2. CONSULTAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<publication> getPublicationById(@PathVariable Long id) {
        try {
            publication publication = publicationService.getPublicationById(id);
            return ResponseEntity.ok(publication);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 3. CONSULTAR POR TÍTULO
    @GetMapping("/by-title/{title}")
    public ResponseEntity<List<publication>> getPublicationsByTitle(@PathVariable String title) {
        List<publication> publications = publicationService.getPublicationsByTitle(title);
        if (publications.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(publications);
    }

    // 4. CONSULTAR POR ESCRITOR/AUTOR
    @GetMapping("/by-writer/{writer}")
    public ResponseEntity<List<publication>> getPublicationsByWriter(@PathVariable String writer) {
        List<publication> publications = publicationService.getPublicationsByAuthor(writer);
        if (publications.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(publications);
    }

    // 5. CONSULTAR POR GÉNERO (para cualquier tipo de publicación)
    @GetMapping("/by-genre/{genre}")
    public ResponseEntity<List<publication>> getPublicationsByGenre(@PathVariable String genre) {
        List<publication> publications = publicationService.getPublicationsByGenre(genre);
        if (publications.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(publications);
    }

    // 6. CREAR PUBLICACIÓN (se mantiene la lógica específica)
    @PostMapping("/new-novel")
    public ResponseEntity<publication> createNovel(@RequestBody novels novel) {
        try {
            publication createdNovel = publicationService.createPublication(novel);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNovel);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/new-webtoon")
    public ResponseEntity<publication> createWeebToon(@RequestBody webToon weebToon) {
        try {
            publication createdWebToon = publicationService.createPublication(weebToon);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdWebToon);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 7. ACTUALIZAR PUBLICACIÓN
    @PutMapping("/update/{id}")
    public ResponseEntity<publication> updatePublication(@PathVariable Long id,
                                                         @RequestBody publication publicationDetails) {
        try {
            publication updatedPublication = publicationService.updatePublication(id, publicationDetails);
            return ResponseEntity.ok(updatedPublication);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // 8. ELIMINAR POR TÍTULO
    @DeleteMapping("/by-title/{title}")
    public ResponseEntity<String> deletePublicationByTitle(@PathVariable String title) {
        boolean deleted = publicationService.deletePublicationByTitle(title);
        if (deleted) {
            return ResponseEntity.ok("Publication with title '" + title + "' deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Publication with title '" + title + "' not found.");
        }
    }
}//Rest
