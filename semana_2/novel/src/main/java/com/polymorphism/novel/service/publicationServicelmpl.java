package com.polymorphism.novel.service;

import com.polymorphism.novel.model.publication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class publicationServicelmpl implements publicationService{

    @Override
    public List<publication> getAllPublications() {
        return List.of();
    }

    @Override
    public List<publication> getPublicationsByTitle(String title) {
        return List.of();
    }

    @Override
    public List<publication> getPublicationsByWriter(String writer) {
        return List.of();
    }

    @Override
    public List<publication> getPublicationsByGenre(String genre) {
        return List.of();
    }

    @Override
    public publication createPublication(publication publication) {
        return null;
    }

    @Override
    public void deletePublicationByTitle(String title) {

    }
}
