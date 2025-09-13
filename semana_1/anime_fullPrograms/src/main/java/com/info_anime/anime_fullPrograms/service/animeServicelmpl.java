package com.info_anime.anime_fullPrograms.service;

import com.info_anime.anime_fullPrograms.model.animeModel;
import com.info_anime.anime_fullPrograms.repository.animeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class animeServicelmpl implements animeService {

    @Autowired
    private animeRepo animeRepo;

    @Override
    public List<animeModel> getAllAnimes() {
        return animeRepo.findAll();
    }

    @Override
    public Optional<animeModel> getAnimeById(String id) {
        return animeRepo.findById(id);
    }

    @Override
    public animeModel createAnime(animeModel anime) {
        return animeRepo.save(anime);
    }

    @Override
    public animeModel updateAnime(String id, animeModel animeDetails) {
        Optional<animeModel> animeOptional = animeRepo.findById(id);
        if (animeOptional.isPresent()) {
            animeModel animeToUpdate = animeOptional.get();
            animeToUpdate.setTitulo(animeDetails.getTitulo());
            animeToUpdate.setEscritor(animeDetails.getEscritor());
            animeToUpdate.setDirector(animeDetails.getDirector());
            animeToUpdate.setAnoLanzamiento(animeDetails.getAnoLanzamiento());
            animeToUpdate.setGenero(animeDetails.getGenero());
            return animeRepo.save(animeToUpdate);
        }
        return null;
    }

    @Override
    public boolean deleteAnimeById(String id) {
        if (animeRepo.existsById(id)) {
            animeRepo.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<animeModel> getAnimesByTitulo(String titulo) {
        return animeRepo.findByTituloContainingIgnoreCase(titulo);
    }

    @Override
    public boolean deleteAnimesByTitulo(String titulo) {
        List<animeModel> animesToDelete = animeRepo.findByTituloContainingIgnoreCase(titulo);
        if (animesToDelete.isEmpty()) {
            return false;
        }
        animeRepo.deleteAll(animesToDelete);
        return true;
    }



}
