package com.polymorphism.novel.service;

import com.polymorphism.novel.model.novels;
import com.polymorphism.novel.model.webToon;
import com.polymorphism.novel.model.publication;
import com.polymorphism.novel.repository.publicationRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Transactional
public class publicationServicelmpl implements publicationService{

    private final publicationRepo publicationRepo;

    // Constructor de inyección de dependencias (se recomienda sin @Autowired en el campo)
    public publicationServicelmpl(publicationRepo publicationRepo) {
        this.publicationRepo = publicationRepo;
    }

    // 1. CONSULTA GENERAL
    @Override
    public List<publication> getAllPublications() {
        return publicationRepo.findAll();
    }

    // Usando la query del repositorio para un acceso más eficiente
    @Override
    public List<novels> getAllNovels() {
        return (List<novels>) (List<? extends publication>) publicationRepo.findAllNovels();
    }

    // Usando la query del repositorio para un acceso más eficiente
    @Override
    public List<webToon> getAllWebToons() {
        return (List<webToon>) (List<? extends publication>) publicationRepo.findAllWebToons();
    }

    // 2. CONSULTAR POR ID
    @Override
    public publication getPublicationById(Long id) {
        return publicationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Publication not found with id: " + id));
    }

    // 3. CONSULTAR POR TÍTULO
    @Override
    public List<publication> getPublicationsByTitle(String title) {
        return publicationRepo.findByTitleIgnoreCase(title);
    }

    // 4. CONSULTAR POR ESCRITOR/AUTOR
    @Override
    public List<publication> getPublicationsByAuthor(String author) {
        return publicationRepo.findByWriterIgnoreCase(author);
    }

    // 5. CONSULTAR POR GÉNERO
    @Override
    public List<publication> getPublicationsByGenre(String genre) {
        return publicationRepo.findByGenre(genre);
    }

    // Método obsoleto: eliminado ya que el método getPublicationsByGenre es más flexible y completo
    @Override
    public List<novels> getNovelsByGenre(String genre) {
        throw new UnsupportedOperationException("This method is deprecated. Please use getPublicationsByGenre instead.");
    }

    // 6. CREAR NUEVA PUBLICACIÓN
    @Override
    public publication createPublication(publication publication) {
        return publicationRepo.save(publication);
    }

    // Métodos para crear específicos (eliminados por ser redundantes)
    @Override
    public novels createNovel(novels novel) {
        throw new UnsupportedOperationException("Use createPublication method instead.");
    }

    @Override
    public webToon createWebToon(webToon webToon) {
        throw new UnsupportedOperationException("Use createPublication method instead.");
    }

    // 7. ACTUALIZAR PUBLICACIÓN
    @Override
    public publication updatePublication(Long id, publication publicationDetails) {
        publication existingPublication = getPublicationById(id);

        // Actualización de los campos comunes
        existingPublication.setTitle(publicationDetails.getTitle());
        existingPublication.setWriter(publicationDetails.getWriter());
        existingPublication.setPublisher(publicationDetails.getPublisher());
        existingPublication.setYearOfRelease(publicationDetails.getYearOfRelease());
        existingPublication.setGenre(publicationDetails.getGenre());
        existingPublication.setStatus(publicationDetails.getStatus());

        // Actualización de los campos específicos de la subclase
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
            throw new RuntimeException("Publication type does not match for update.");
        }

        return publicationRepo.save(existingPublication);
    }

    // 8. ELIMINAR POR TÍTULO
    @Override
    public boolean deletePublicationByTitle(String title) {
        if (publicationRepo.existsByTitle(title)) {
            publicationRepo.deleteByTitle(title);
            return true;
        }
        return false;
    }


}
