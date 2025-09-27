package com.superComics.inventory.inventory.service;

import com.superComics.inventory.inventory.events.gradingUpdatedEvent;
import com.superComics.inventory.inventory.events.lowStockAlertEvent;
import com.superComics.inventory.inventory.model.comic;
import com.superComics.inventory.inventory.model.grading;
import com.superComics.inventory.inventory.repository.comicRepo;
import com.superComics.inventory.notifications.notificationService;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class inventoryServiceImpl implements inventoryServices {

    private final comicRepo comicRepo;
    private final ApplicationEventPublisher eventPublisher;
    private final notificationService notificarionService;

    public inventoryServiceImpl(comicRepo comicRepo, ApplicationEventPublisher eventPublisher, notificationService notificarionService) {
        this.comicRepo = comicRepo;
        this.eventPublisher = eventPublisher;
        this.notificarionService = notificarionService;
    }

    //Crear un nuevo comic
    @Transactional
    @Override
    public comic newComic(Long id, String sku, String title, Integer issueNumber, String publisher, double estimatedValue, Integer currentStock, Integer minimalStock, grading grading, Long traderId){
        comic Comic = new comic(id, sku, title, issueNumber, publisher, estimatedValue, currentStock, minimalStock , grading, traderId);
        return comicRepo.save(Comic);
    }

    //Cambio de grading
    @Transactional
    @Override
    public comic updateGrading(Long comicId, String newGradingCode) {

        grading newGrading = new grading(newGradingCode); // Uso del Value Object con validación

        comic comic = comicRepo.findById(comicId)
                // Usando tu excepción compartida
                .orElseThrow(() -> new ComicNotFoundException("Cómic con ID " + comicId + " no encontrado."));

        grading oldGrading = comic.getGrading();

        // 1. Aplicar lógica de negocio (cambio de estado)
        comic.setGrading(newGrading);
        comic updatedComic = comicRepo.save(comic);

        // 2. Publicar Evento de Dominio (CORREGIDO)
        eventPublisher.publishEvent(new gradingUpdatedEvent(
                updatedComic.getId(),
                updatedComic.getSku(),
                oldGrading.getCode(),
                newGrading.getCode()
        ));

        return updatedComic;
    }

    //Reducir Stock
    @Transactional
    @Override
    public void byeStocks(Long comicId, int number){
        comic Comic = comicRepo.findById(comicId)
                .orElseThrow(()-> new IllegalArgumentException("Cómic no encontrado: " + comicId));

        Comic.byeStock(number);
        comicRepo.save(Comic);

        if(Comic.needRestock()){
            lowStockAlertEvent lowStock = new lowStockAlertEvent(Comic.getId(), Comic.getTitle(),
                    Comic.getCurrentStock(), Comic.getMinimalStock());
            eventPublisher.publishEvent(lowStock);
        }

    }

    //Aumentar Stock
    @Transactional
    @Override
    public void plusStocks(Long comicId, int number){
        comic Comic =  comicRepo.findById(comicId)
                .orElseThrow(()-> new IllegalArgumentException("Cómic no encontrado: " + comicId));

        Comic.plusStock(number);
        comicRepo.save(Comic);
    }

    //Obtener lista de todos los comics
    @Override
    public List<comic> getAllComics(){

        return comicRepo.findAll();
    }

    //Obtener comic por id
    @Override
    public comic getComicsById(Long comicId){
        return comicRepo.findById(comicId).orElseThrow(()-> new IllegalArgumentException("Cómic no encontrado: " + comicId));
    }

    //Obtener comic con bajo stock
    @Override
    public List<comic> getComicsWithLowStock(){
        return comicRepo.findAll().stream()
                .filter(comic::needRestock)
                .toList();
    }

    //Obtener comic por título
    @Override
    public List<comic> getComicsByTitle(String title){

        return comicRepo.findByTitle(title);
    }

    //Obtener comic por editorial (publisher)
    @Override
    public List<comic> getComicsByPublisher(String publisher){

        return comicRepo.findAllByPublisher(publisher);
    }

    //Verificar Stock
    @Override
    public boolean verifyStock(Long comicId, int number){
        return comicRepo.findById(comicId)
                .map(comic -> comic.yesStock(number))
                .orElse(false);
    }



}
