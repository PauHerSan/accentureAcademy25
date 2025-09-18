package com.polymorphism.novel.service;

import com.polymorphism.novel.model.publication;

import java.util.List;
import java.util.Optional;

public interface publicationService {

    List<publication> getAllPublications();
    List<publication> getPublicationsByTitle(String title);
    List<publication> getPublicationsByWriter(String writer);
    List<publication> getPublicationsByGenre(String genre);
    publication createPublication(publication publication);
    void deletePublicationByTitle(String title);

}
