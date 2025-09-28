package com.info_anime.anime_fullPrograms.service;

import com.info_anime.anime_fullPrograms.model.animeModel;
import com.info_anime.anime_fullPrograms.repository.animeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class animeServiceImplements implements animeService {

    @Autowired
    private animeRepo animeRepo;

    //Todos los animes
    @Override
    public List<animeModel> getAllAnimes() {
        return animeRepo.findAll();
    }

    // anime por id
    @Override
    public Optional<animeModel> getAnimeById(String id) {
        return animeRepo.findById(id);
    }

    //anime por título
    @Override
    public List<animeModel> getAnimesByTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        return animeRepo.findByTituloContainingIgnoreCase(titulo);
    }

    //crear un anime
    @Override
    public animeModel createAnime(animeModel anime) {
        validateAnime(anime);

        List<animeModel> existingAnimes = animeRepo.findByTituloContainingIgnoreCase(anime.getTitulo());
        if (!existingAnimes.isEmpty()) {

            boolean exactMatch = existingAnimes.stream()
                    .anyMatch(existing -> existing.getTitulo().equalsIgnoreCase(anime.getTitulo()));
            if (exactMatch) {
                throw new IllegalArgumentException("Ya existe un anime con este título");
            }
        }

        return animeRepo.save(anime);
    }

    //Actualizar anime
    @Override
    public Optional<animeModel> updateAnime(String id, animeModel animeDetails) {
        validateAnime(animeDetails);

        return animeRepo.findById(id)
                .map(anime -> {
                    anime.setTitulo(animeDetails.getTitulo());
                    anime.setEscritor(animeDetails.getEscritor());
                    anime.setDirector(animeDetails.getDirector());
                    anime.setAnoLanzamiento(animeDetails.getAnoLanzamiento());
                    anime.setNumeroCapitulos(animeDetails.getNumeroCapitulos());
                    anime.setGenero(animeDetails.getGenero());
                    return animeRepo.save(anime);
                });
    }

    //Eliminar por id
    @Override
    public boolean deleteAnimeById(String id) {
        if (animeRepo.existsById(id)) {
            animeRepo.deleteById(id);
            return true;
        }
        return false;
    }

    //Eliminar por título
    @Override
    public int deleteAnimesByTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }

        List<animeModel> animesToDelete = animeRepo.findByTituloContainingIgnoreCase(titulo);
        int count = animesToDelete.size();
        if (!animesToDelete.isEmpty()) {
            animeRepo.deleteAll(animesToDelete);
        }
        return count;
    }

    private void validateAnime(animeModel anime) {
        if (anime == null) {
            throw new IllegalArgumentException("El anime no puede ser nulo");
        }

        if (anime.getTitulo() == null || anime.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título es obligatorio");
        }

        if (anime.getAnoLanzamiento() < 1900 || anime.getAnoLanzamiento() > 2030) {
            throw new IllegalArgumentException("El año de lanzamiento debe estar entre 1900 y 2030");
        }

        if (anime.getNumeroCapitulos() <= 0) {
            throw new IllegalArgumentException("El número de capítulos debe ser mayor a 0");
        }

        if (anime.getGenero() == null || anime.getGenero().isEmpty()) {
            throw new IllegalArgumentException("Debe especificar al menos un género");
        }

    }
}
