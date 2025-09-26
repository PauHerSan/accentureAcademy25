package com.superComics.inventory.comicsOnSite;

import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class inventoryService {

    private final comicRepo comicRepo;
    private final ApplicationEventPublisher eventPublisher;

    public inventoryService(comicRepo comicRepo, ApplicationEventPublisher eventPublisher) {
        this.comicRepo = comicRepo;
        this.eventPublisher = eventPublisher;
    }

    //Crear un nuevo comic
    @Transactional
    public comic newComic(Long id, String sku, String title, Integer issueNumber, String publisher, double estimatedValue, Integer currentStock, Integer minimalStock, grading grading, Long traderId){
        comic Comic = new comic(id, sku, title, issueNumber, publisher, estimatedValue, currentStock, minimalStock , grading, traderId);
        return comicRepo.save(Comic);
    }

    //Cambio de grading
    @Transactional
    public comic updateGrading(Long comicId, String newGradingCode) {

        grading newGrading = new grading(newGradingCode);

        comic comic = comicRepo.findById(comicId)
                .orElseThrow(() -> new RuntimeException("comic no encontrado"));

        comic.setGrading(newGrading);
        comic updatedComic = comicRepo.save(comic);

//        events.publishEvent(new GradingUpdated(
//                updatedComic.getId(),
//                updatedComic.getSku(),
//                newGradingCode));

        return updatedComic;
    }

    //Reducir Stock
    @Transactional
    public void byeStocks(Long comicId, int number){
        comic Comic = comicRepo.findById(comicId)
                .orElseThrow(()-> new IllegalArgumentException("Cómic no encontrado: " + comicId));

        Comic.byeStock(number);
        comicRepo.save(Comic);

//        if(Comic.needRestock()){
//            lowStock lowStock = lowStock(Comic.getId(), Comic.getTitle(),
//                    Comic.getCurrentStock(), Comic.getMinimalStock());
//            eventPublisher.publishEvent(lowStock);
//        }

    }

    //Aumentar Stock
    @Transactional
    public void plusStocks(Long comicId, int number){
        comic Comic =  comicRepo.findById(comicId)
                .orElseThrow(()-> new IllegalArgumentException("Cómic no encontrado: " + comicId));

        Comic.plusStock(number);
        comicRepo.save(Comic);
    }

    //Obtener lista de todos los comics
    public List<comic> getAllComics(){

        return comicRepo.findAll();
    }

    //Obtener comic por id
    public comic getComicsById(Long comicId){
        return comicRepo.findById(comicId).orElseThrow(()-> new IllegalArgumentException("Cómic no encontrado: " + comicId));
    }

    //Obtener comic con bajo stock
    public List<comic> getComicsWithLowStock(){
        return comicRepo.findAll().stream()
                .filter(comic::needRestock)
                .toList();
    }

    //Obtener comic por título
    public List<comic> getComicsByTitle(String title){

        return comicRepo.findByTitle(title);
    }

    //Obtener comic por editorial (publisher)
    public List<comic> getComicsByPublisher(String publisher){

        return comicRepo.findAllByPublisher(publisher);
    }

    //Verificar Stock
    public boolean verifyStock(Long comicId, int number ){
        return comicRepo.findById(comicId)
                .map(comic -> comic.yesStock(number))
                .orElse(false);
    }



}
