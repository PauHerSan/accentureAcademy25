package com.polymorphism.novel.service;

import com.polymorphism.novel.model.novels;
import com.polymorphism.novel.model.publication;
import com.polymorphism.novel.model.webToon;

import java.util.List;

public interface publicationService {

    //Metodos para todas las publicaciones
    List<publication> getAllPublications();
    publication getPublicationById(Long id);
    List<publication> getPublicationsByTitle(String title);
    List<publication> getPublicationsByAuthor(String author);
    List<publication> getPublicationsByGenre(String genre);
    publication createPublication(publication publication);
    publication updatePublication(Long id, publication publicationDetails);
    boolean deletePublicationByTitle(String title);

    //Métodos específicos para novelas
    List<novels> getAllNovels();
    List<novels> getNovelsByGenre(String genre);
    novels createNovel(novels novel);

    //Métodos específicos para webtoons
    List<webToon> getAllWebToons();
    webToon createWebToon(webToon webToon);


}
