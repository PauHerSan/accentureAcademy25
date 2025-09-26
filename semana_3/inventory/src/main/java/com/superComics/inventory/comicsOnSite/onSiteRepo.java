package com.superComics.inventory.comicsOnSite;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface onSiteRepo extends JpaRepository<comic, Long>{

    List<comic> findByCurrentStockLessThanEqual(Integer minimalStock);
    List<comic> findAllByPublisher(String publisher);


}
