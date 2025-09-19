package com.polymorphism.novel.service;

import com.polymorphism.novel.model.novels;
import com.polymorphism.novel.model.webToon;
import com.polymorphism.novel.model.publication;
import com.polymorphism.novel.repository.novelRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class publicationServicelmpl implements publicationService{

    @Autowired
    private final novelRepo novelRepo;

    public publicationServicelmpl(novelRepo novelRepo) {
        this.novelRepo = novelRepo;
    }

    // 1. CONSULTA GENERAL
    @Override
    public List<publication> getAllPublications() {
        return novelRepo.findAll();
    }

    @Override
    public List<novels> getAllNovels() {
        return novelRepo.findAll().stream()
                .filter(p -> p instanceof novels)
                .map(p -> (novels) p)
                .collect(Collectors.toList());
    }

    @Override
    public List<webToon> getAllWebToons() {
        return novelRepo.findAll().stream()
                .filter(p -> p instanceof webToon)
                .map(p -> (webToon) p)
                .collect(Collectors.toList());
    }

    // 2. CONSULTAR POR ID
    @Override
    public publication getPublicationById(Long id) {
        return novelRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Publication not found with id: " + id));
    }

    // 3. CONSULTAR POR TÍTULO
    @Override
    public List<publication> getPublicationsByTitle(String title) {
        return novelRepo.findByTitleIgnoreCase(title);
    }

    // 4. CONSULTAR POR ESCRITOR/AUTOR
    @Override
    public List<publication> getPublicationsByAuthor(String author) {
        return novelRepo.findByWriterIgnoreCase(author);
    }

    // 5. CONSULTAR POR GÉNERO (solo para novels)
    @Override
    public List<novels> getNovelsByGenre(String genre) {
        return novelRepo.findAll().stream()
                .filter(p -> p instanceof novels && p.getGenre().equalsIgnoreCase(genre))
                .map(p -> (novels) p)
                .collect(Collectors.toList());
    }

    // 6. CREAR NUEVA PUBLICACIÓN
    @Override
    public publication createPublication(publication publication) {
        return novelRepo.save(publication);
    }

    // 7. ACTUALIZAR PUBLICACIÓN
    @Override
    public publication updatePublication(Long id, publication publicationDetails) {
        // Asegúrate de que el ID es válido antes de actualizar
        publication existingPublication = getPublicationById(id);

        // Actualizar los campos comunes
        existingPublication.setTitle(publicationDetails.getTitle());
        existingPublication.setWriter(publicationDetails.getWriter());
        existingPublication.setPublisher(publicationDetails.getPublisher());
        existingPublication.setYearOfRelease(publicationDetails.getYearOfRelease());
        existingPublication.setGenre(publicationDetails.getGenre());
        existingPublication.setStatus(publicationDetails.getStatus());

        // Actualizar campos específicos con lógica de polimorfismo
        if (existingPublication instanceof novels && publicationDetails instanceof novels) {
            novels existingNovel = (novels) existingPublication;
            novels novelDetails = (novels) publicationDetails;
            existingNovel.setEditor(novelDetails.getEditor());
            existingNovel.setChapters(novelDetails.getChapters());
        } else if (existingPublication instanceof webToon && publicationDetails instanceof webToon) {
            webToon existingWebToon = (webToon) existingPublication;
            webToon webToonDetails = (webToon) publicationDetails;
            existingWebToon.setArtist(webToonDetails.getArtist());
            existingWebToon.setFullColor(webToonDetails.isFullColor());
        } else {
            // Manejar un caso en el que los tipos no coincidan, si es necesario.
            throw new RuntimeException("Tipo de publicación no coincide para la actualización.");
        }

        return novelRepo.save(existingPublication);
    }

    // 8. ELIMINAR POR TÍTULO
    @Override
    public boolean deletePublicationByTitle(String title) {
        //Llama al metodo del repositorio para eliminar y devuelve si se eliminó algo.
        novelRepo.deleteByTitle(title);
        // Si no hay errores, se asume que se intentó eliminar.
        return true;
    }

    // Métodos para crear publicaciones específicas (ya que el controlador los usa)
    @Override
    public novels createNovel(novels novel) {
        return novelRepo.save(novel);
    }

    @Override
    public webToon createWebToon(webToon webToon) {
        return novelRepo.save(webToon);
    }

}
