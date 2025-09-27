package com.superComics.inventory.inventory.repository;

import com.superComics.inventory.inventory.model.comicItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface comicRepo extends JpaRepository<comicItem, Long>{


    List<comicItem> findAllByPublisher(String publisher);
    List<comicItem> findByTitle(String title);

}
