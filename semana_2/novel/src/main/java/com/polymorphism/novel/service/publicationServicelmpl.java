package com.polymorphism.novel.service;

import com.polymorphism.novel.model.novels;
import com.polymorphism.novel.model.webToon;
import com.polymorphism.novel.model.publication;
import com.polymorphism.novel.repository.publicationRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class publicationServicelmpl implements publicationService{

    @Autowired
    private final publicationRepo publicationRepo;

    public publicationServicelmpl(publicationRepo publicationRepo) {
        this.publicationRepo = publicationRepo;
    }

    // 1. CONSULTA GENERAL
    @Override
    public List<publication> getAllPublications() {
        return publicationRepo.findAll();
    }

    @Override
    public List<novels> getAllNovels() {
        return publicationRepo.findAll().stream()
                .filter(p -> p instanceof novels)
                .map(p -> (novels) p)
                .collect(Collectors.toList());
    }

    @Override
    public List<webToon> getAllWebToons() {
        return publicationRepo.findAll().stream()
                .filter(p -> p instanceof webToon)
                .map(p -> (webToon) p)
                .collect(Collectors.toList());
    }

    //2.CONSULTAR POR ID
    @Override
    public publication getPublicationById(Long id) {
        return publicationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Publication not found with id: " + id));
    }

    //3.CONSULTAR POR TÍTULO
    @Override
    public List<publication> getPublicationsByTitle(String title) {
        return publicationRepo.findByTitleIgnoreCase(title);
    }

    // 4. CONSULTAR POR ESCRITOR/AUTOR
    @Override
    public List<publication> getPublicationsByAuthor(String author) {
        return publicationRepo.findByWriterIgnoreCase(author);
    }

    // 5. CONSULTAR POR GÉNERO (solo para novels)
    @Override
    public List<novels> getNovelsByGenre(String genre) {
        return publicationRepo.findAll().stream()
                .filter(p -> p instanceof novels && p.getGenre().equalsIgnoreCase(genre))
                .map(p -> (novels) p)
                .collect(Collectors.toList());
    }

    // 6. CREAR NUEVA PUBLICACIÓN
    @Override
    public publication createPublication(publication publication) {
        return publicationRepo.save(publication);
    }

    // 7. ACTUALIZAR PUBLICACIÓN
    @Override
    public publication updatePublication(Long id, publication publicationDetails) {

        publication existingPublication = getPublicationById(id);
        existingPublication.setTitle(publicationDetails.getTitle());
        existingPublication.setWriter(publicationDetails.getWriter());
        existingPublication.setPublisher(publicationDetails.getPublisher());
        existingPublication.setYearOfRelease(publicationDetails.getYearOfRelease());
        existingPublication.setGenre(publicationDetails.getGenre());
        existingPublication.setStatus(publicationDetails.getStatus());

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
            throw new RuntimeException("Tipo de publicación no coincide para la actualización.");
        }

        return publicationRepo.save(existingPublication);
    }

    // 8. ELIMINAR POR TÍTULO
    @Override
    public boolean deletePublicationByTitle(String title) {
        //Llama al metodo del repositorio para eliminar y devuelve si se eliminó algo.
        publicationRepo.deleteByTitle(title);
        // Si no hay errores, se asume que se intentó eliminar.
        return true;
    }

    // Métodos para crear publicaciones específicas (ya que el controlador los usa)
    @Override
    public novels createNovel(novels novel) {
        return publicationRepo.save(novel);
    }

    @Override
    public webToon createWebToon(webToon webToon) {
        return publicationRepo.save(webToon);
    }

}
