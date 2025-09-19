package com.polymorphism.novel.rest;

import com.polymorphism.novel.model.novels;
import com.polymorphism.novel.model.publication;
import com.polymorphism.novel.model.webToon;
import com.polymorphism.novel.repository.novelRepo;
import com.polymorphism.novel.service.publicationService;
import com.polymorphism.novel.service.publicationServicelmpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/publications")
@CrossOrigin(origins = "*")
public class publicationRest {

    @Autowired
    private novelRepo novelRepo;
    @Autowired
    private publicationServicelmpl publicationServicelmpl;


    // 1. CONSULTAR DE MANERA GENERAL
    @GetMapping("/all")
    public ResponseEntity<List<publication>> getAllPublications() {
        List<publication> publications = publicationServicelmpl.getAllPublications();
        return ResponseEntity.ok(publications);
    }

    // Obtener solo novels
    @GetMapping("/novels")
    public ResponseEntity<List<novels>> getAllNovels() {
        List<novels> novels = publicationServicelmpl.getAllNovels();
        return ResponseEntity.ok(novels);
    }

    // Obtener solo webtoons
    @GetMapping("/webtoons")
    public ResponseEntity<List<webToon>> getAllWebToons() {
        List<webToon> weebToon = publicationServicelmpl.getAllWebToons();
        return ResponseEntity.ok(weebToon);
    }

    // 2. CONSULTAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<publication> getPublicationById(@PathVariable Long id) {
        try {
            publication publication = publicationServicelmpl.getPublicationById(id);
            return ResponseEntity.ok(publication);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 3. CONSULTAR POR TÍTULO
    @GetMapping("/title/{title}")
    public ResponseEntity<List<publication>> getPublicationsByTitle(@PathVariable String title) {
        List<publication> publications = publicationServicelmpl.getPublicationsByTitle(title);
        if (publications.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(publications);
    }

    // 4. CONSULTAR POR ESCRITOR/AUTOR
    @GetMapping("/author/{author}")
    public ResponseEntity<List<publication>> getPublicationsByAuthor(@PathVariable String author) {
        List<publication> publications = publicationServicelmpl.getPublicationsByAuthor(author);
        if (publications.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(publications);
    }

    // 5. CONSULTAR POR GÉNERO (solo para novels)
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<novels>> getNovelsByGenre(@PathVariable String genre) {
        List<novels> novels = publicationServicelmpl.getNovelsByGenre(genre);
        if (novels.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(novels);
    }

    // 6. CREAR NUEVA PUBLICACIÓN (novel o webtoon)
    @PostMapping
    public ResponseEntity<publication> createPublication(@RequestBody publication publication) {
        try {
            publication createdPublication = publicationServicelmpl.createPublication(publication);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPublication);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Endpoint específico para crear novel
    @PostMapping("/new-Novel")
    public ResponseEntity<publication> createNovel(@RequestBody novels novel) {
        try {
            publication createdNovel = publicationServicelmpl.createPublication(novel);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNovel);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Endpoint específico para crear webtoon
    @PostMapping("/new-Webtoon")
    public ResponseEntity<publication> createWeebToon(@RequestBody webToon weebToon) {
        try {
            publication createdWebToon = publicationServicelmpl.createPublication(weebToon);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdWebToon);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 7. ACTUALIZAR PUBLICACIÓN
    @PutMapping("/actu/{id}")
    public ResponseEntity<publication> updatePublication(@PathVariable Long id,
                                                          @RequestBody publication publicationDetails) {
        try {
            publication updatedPublication = publicationServicelmpl.updatePublication(id, publicationDetails);
            return ResponseEntity.ok(updatedPublication);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 8. ELIMINAR POR TÍTULO
    @DeleteMapping("/byeTitle/{title}")
    public ResponseEntity<String> deletePublicationByTitle(@PathVariable String title) {
        boolean deleted = publicationServicelmpl.deletePublicationByTitle(title);
        if (deleted) {
            return ResponseEntity.ok("Publication with title '" + title + "' deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }

    }
}//Rest
