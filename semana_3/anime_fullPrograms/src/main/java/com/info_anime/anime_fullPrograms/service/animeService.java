package com.info_anime.anime_fullPrograms.service;

import com.info_anime.anime_fullPrograms.model.animeModel;

import java.util.List;
import java.util.Optional;

public interface animeService {


    List<animeModel> getAllAnimes();
    Optional<animeModel> getAnimeById(String id);
    animeModel createAnime(animeModel anime);
    Optional<animeModel> updateAnime(String id, animeModel animeDetails);
    boolean deleteAnimeById(String id);

    List<animeModel> getAnimesByTitulo(String titulo);
    int deleteAnimesByTitulo(String titulo);

}
