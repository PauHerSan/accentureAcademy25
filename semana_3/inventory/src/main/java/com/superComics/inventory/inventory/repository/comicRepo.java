package com.superComics.inventory.inventory.repository;

import com.superComics.inventory.inventory.model.comic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface comicRepo extends JpaRepository<comic, Long>{


    List<comic> findAllByPublisher(String publisher);
    List<comic> findByTitle(String title);




}
