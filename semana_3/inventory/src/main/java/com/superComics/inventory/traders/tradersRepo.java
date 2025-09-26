package com.superComics.inventory.traders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface tradersRepo extends JpaRepository<traders, Long> {

    List<traders> findByName(String name);
    List<traders> findByUsualPrice(double price);
    List<traders> findByEmail(String email);
    List<traders> findBySpeciality(String speciality);

}
