package com.superComics.inventory.traders.service;

import com.superComics.inventory.shared.BusinessException;
import com.superComics.inventory.traders.model.traders;
import com.superComics.inventory.traders.repository.tradersRepo;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class tradersServiceImpl implements traderService {

    private tradersRepo tradersRepo;
    private ApplicationEventPublisher eventPublisher;

    public tradersServiceImpl(tradersRepo tradersRepo, ApplicationEventPublisher eventPublisher) {
        this.tradersRepo = tradersRepo;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Override
    public traders createNewTrader(String name, String email, String speciality, double usualPrice, String address) {

        if (tradersRepo.findByContactEmail(email).isPresent()) {
            throw new BusinessException("Ya existe un Trader registrado con este email.");
       }
        traders newTrader = new traders(null, name, email, speciality, usualPrice, address, true);
        return tradersRepo.save(newTrader);
    }

    @Override
    public traders getTraderById(Long id) {
        // Lanza una excepción de negocio si no se encuentra
        return tradersRepo.findById(id)
                .orElseThrow(() -> new BusinessException("Trader con ID " + id + " no encontrado."));
    }

    @Transactional
    @Override
    public void deactivateTrader(Long id) {
        traders trader = getTraderById(id);
        if (!trader.isActive()) {
            throw new BusinessException("El Trader con ID " + id + " ya está inactivo.");
        }
        trader.setActive(false);
        tradersRepo.save(trader);
    }

    @Override
    public List<traders> getAllActiveTraders() {
        return tradersRepo.findAllByIsActiveTrue();
    }
}
