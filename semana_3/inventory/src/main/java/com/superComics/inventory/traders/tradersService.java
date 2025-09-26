package com.superComics.inventory.traders;

import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class tradersService {

    private tradersRepo tradersRepo;
    private ApplicationEventPublisher eventPublisher;

    public tradersService(tradersRepo tradersRepo, ApplicationEventPublisher eventPublisher) {
        this.tradersRepo = tradersRepo;
        this.eventPublisher = eventPublisher;
    }

    //Encontrar por Id
    public traders findById(Long id) {
        return tradersRepo.findById(id).orElse(null);
    }

    //Encontrar todos los traders
    public List<traders> findAll(){
        return tradersRepo.findAll();
    }

    //Encontrar por Nombre
    public  List<traders> findByName(String name){
        return tradersRepo.findByName(name);
    }

    //Encontrar por rango de precios
    public  List<traders> findByUsualPrice(double price) {
        return tradersRepo.findByUsualPrice(price);
    }

    //Encontrar por email
    public List<traders> findByEmail(String email){
        return tradersRepo.findByEmail(email);
    }

    //Encontrar por Speciality
    public List<traders> findBySpeciality(String speciality){
        return tradersRepo.findBySpeciality(speciality);
    }

}
