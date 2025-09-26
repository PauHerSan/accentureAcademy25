package com.info_anime.anime_fullPrograms.repository;

import com.info_anime.anime_fullPrograms.model.animeModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface animeRepo extends MongoRepository<animeModel,String> {

    List<animeModel> findByTituloContainingIgnoreCase(String titulo);
}
