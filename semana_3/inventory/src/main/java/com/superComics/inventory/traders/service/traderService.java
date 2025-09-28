package com.superComics.inventory.traders.service;

import com.superComics.inventory.traders.model.traders;
import jakarta.transaction.Transactional;

import java.util.List;

public interface traderService {

    traders createNewTrader(String name, String email, String speciality, double usualPrice, String address);
    traders getTraderById(Long id);

    @Transactional
    void deactivateTrader(Long id);

    List<traders> getAllActiveTraders();
}
