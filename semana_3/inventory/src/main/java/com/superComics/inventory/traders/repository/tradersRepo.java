package com.superComics.inventory.traders.repository;

import com.superComics.inventory.traders.model.traders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface tradersRepo extends JpaRepository<traders, Long> {

    Optional<traders> findByContactEmail(String contactEmail);
    List<traders> findAllByIsActiveTrue();

}
