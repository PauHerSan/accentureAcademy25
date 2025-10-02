package com.superComics.inventory.inventory.service;

import com.superComics.inventory.inventory.events.comicAddedEvent;
import com.superComics.inventory.inventory.events.gradingUpdatedEvent;
import com.superComics.inventory.inventory.events.lowStockAlertEvent;
import com.superComics.inventory.inventory.events.stockQuantityUpdatedEvent;
import com.superComics.inventory.inventory.model.comicItem;
import com.superComics.inventory.inventory.model.grading;
import com.superComics.inventory.inventory.repository.comicRepo;
import com.superComics.inventory.notifications.notificationService;
import com.superComics.inventory.shared.BusinessException;
import com.superComics.inventory.shared.ComicNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class inventoryServiceImpl implements inventoryServices {

    private final comicRepo comicRepo;
    private final ApplicationEventPublisher eventPublisher;
    private final notificationService notificationService;

    public inventoryServiceImpl(comicRepo comicRepo, ApplicationEventPublisher eventPublisher, notificationService notificarionService) {
        this.comicRepo = comicRepo;
        this.eventPublisher = eventPublisher;
        this.notificationService = notificarionService;
    }

    //Crear un nuevo comic
    @Transactional
    @Override
    public comicItem newComic(Long id, String sku, String title, Integer issueNumber, String publisher, double estimatedValue, Integer currentStock, Integer minimalStock, grading grading, Long traderId){
        comicItem Comic = new comicItem(id, sku, title, issueNumber, publisher,
                estimatedValue, currentStock, minimalStock , grading, traderId);
        comicItem savedComic = comicRepo.save(Comic);

        // Publicar ComicAddedEvent
        eventPublisher.publishEvent(comicAddedEvent.builder()
                .comicId(savedComic.getId())
                .sku(savedComic.getSku())
                .title(savedComic.getTitle())
                .publisher(savedComic.getPublisher())
                .currentStock(savedComic.getCurrentStock())
                .build());


        return comicRepo.save(Comic);
    }

    //Cambio de grading
    @Transactional
    @Override
    public comicItem updateGrading(Long comicId, String newGradingCode) {

        grading newGrading = new grading(newGradingCode); // Uso del Value Object con validación

        comicItem comic = comicRepo.findById(comicId)
                // Usando tu excepción compartida
                .orElseThrow(() -> new BusinessException("Cómic con ID " + comicId + " no encontrado."));

        grading oldGrading = comic.getGrading();

        // 1. Aplicar lógica de negocio (cambio de estado)
        comic.setGrading(newGrading);
        comicItem updatedComic = comicRepo.save(comic);

        // 2. Publicar Evento de Dominio (CORREGIDO)
        eventPublisher.publishEvent(gradingUpdatedEvent.builder()
                .comicId(updatedComic.getId())
                .sku(updatedComic.getSku())
                .oldGradingCode(oldGrading.getCode())
                .newGradingCode(newGrading.getCode())
                .build());

        return updatedComic;
    }

    //Reducir Stock
    @Transactional
    @Override
    public void byeStocks(Long comicId, int number){
        comicItem item = comicRepo.findById(comicId)
                .orElseThrow(()-> new BusinessException("Cómic con ID " + comicId + " no encontrado."));

        int oldStock = item.getCurrentStock();

        item.byeStock(number);
        comicRepo.save(item);

        int newStock = item.getCurrentStock();

        // Publicar StockQuantityUpdatedEvent (el cambio de stock fue negativo)
        eventPublisher.publishEvent(stockQuantityUpdatedEvent.builder()
                .comicId(item.getId())
                .sku(item.getSku())
                .oldStock(oldStock)
                .newStock(newStock)
                .changeQuantity(-number) // La cantidad que se redujo (negativa)
                .build());

        if(item.needRestock()) {

            lowStockAlertEvent lowStock = lowStockAlertEvent.builder()
                    .comicId(item.getId())
                    .title(item.getTitle())
                    .currentStock(item.getCurrentStock())
                    .minimalStock(item.getMinimalStock())
                    .build();

            eventPublisher.publishEvent(lowStock);
        }
    }

    //Aumentar Stock
    @Transactional
    @Override
    public void plusStocks(Long comicId, int number){
        comicItem Comic =  comicRepo.findById(comicId)
                .orElseThrow(()-> new BusinessException("Cómic con ID " + comicId + " no encontrado."));

        int oldStock = Comic.getCurrentStock(); // Stock antes de la operación

        Comic.plusStock(number); // Lógica de negocio (aumenta el stock)
        comicRepo.save(Comic);

        int newStock = Comic.getCurrentStock(); // Stock después de la operación

        // Publicar StockQuantityUpdatedEvent (el cambio de stock fue positivo)
        eventPublisher.publishEvent(stockQuantityUpdatedEvent.builder()
                .comicId(Comic.getId())
                .sku(Comic.getSku())
                .oldStock(oldStock)
                .newStock(newStock)
                .changeQuantity(number) // La cantidad que se agregó (positiva)
                .build());
    }

    //Obtener lista de todos los comics
    @Override
    public List<comicItem> getAllComics(){

        return comicRepo.findAll();
    }

    //Obtener comic por id
    @Override
    public comicItem getComicsById(Long comicId){
        return comicRepo.findById(comicId).orElseThrow(()-> new ComicNotFoundException("Cómic no encontrado: " + comicId));
    }

    //Obtener comic con bajo stock
    @Override
    public List<comicItem> getComicsWithLowStock(){
        return comicRepo.findAll().stream()
                .filter(comicItem::needRestock)
                .toList();
    }

    //Obtener comic por título
    @Override
    public List<comicItem> getComicsByTitle(String title){

        return comicRepo.findByTitle(title);
    }

    //Obtener comic por editorial (publisher)
    @Override
    public List<comicItem> getComicsByPublisher(String publisher){

        return comicRepo.findAllByPublisher(publisher);
    }

    //Verificar Stock
    @Override
    public boolean verifyStock(Long comicId, int number){
        return comicRepo.findById(comicId)
                .map(comicItem -> comicItem.yesStock(number))
                .orElse(false);
    }



}
