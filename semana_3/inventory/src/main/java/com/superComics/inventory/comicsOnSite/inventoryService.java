package com.superComics.inventory.comicsOnSite;

import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class inventoryService {

    private final onSiteRepo onSiteRepo;
    private final ApplicationEventPublisher eventPublisher;

    public inventoryService(onSiteRepo onSiteRepo, ApplicationEventPublisher eventPublisher) {
        this.onSiteRepo = onSiteRepo;
        this.eventPublisher = eventPublisher;
    }

    //Crear un nuevo comic
    @Transactional
    public comic newComic(Long id, String sku, String title, Integer issueNumber, String publisher, double estimatedValue, Integer currentStock, Integer minimalStock, String grading, Long traderId){
        comic Comic = new comic(id, sku, title, issueNumber, publisher, estimatedValue, currentStock, minimalStock , grading, traderId);
        return onSiteRepo.save(Comic);
    }

    //Reducir Stock
    @Transactional
    public void byeStocks(Long comicId, int number){
        comic Comic = onSiteRepo.findById(comicId)
                .orElseThrow(()-> new IllegalArgumentException("Cómic no encontrado: " + comicId));

        Comic.byeStock(number);
        onSiteRepo.save(Comic);

//        if(Comic.needRestock()){
//            lowStock lowStock = lowStock(Comic.getId(), Comic.getTitle(),
//                    Comic.getCurrentStock(), Comic.getMinimalStock());
//            eventPublisher.publishEvent(lowStock);
//        }

    }

    //Aumentar Stock
    @Transactional
    public void plusStocks(Long comicId, int number){
        comic Comic =  onSiteRepo.findById(comicId)
                .orElseThrow(()-> new IllegalArgumentException("Cómic no encontrado: " + comicId));

        Comic.plusStock(number);
        onSiteRepo.save(Comic);
    }

    //Obtener lista de todos los comics
    public List<comic> getAllComics(){
        return onSiteRepo.findAll();
    }

    //Obtener comic por id
    public comic getComicsById(Long comicId){
        return onSiteRepo.findById(comicId).orElseThrow(()-> new IllegalArgumentException("Cómic no encontrado: " + comicId));
    }

    //Obtener comic con bajo stock
    public List<comic> getComicsWithLowStock(){
        return onSiteRepo.findAll().stream()
                .filter(comic::needRestock)
                .toList();
    }

    //Obtener comic por título
    public List<comic> getComicsByTitle(String title){
        return onSiteRepo.findByTitle(title);
    }

    //Obtener comic por editorial (publisher)
    public List<comic> getComicsByPublisher(String publisher){
        return onSiteRepo.findAllByPublisher(publisher);
    }

    //Verificar Stock
    public boolean verifyStock(Long comicId, int number ){
        return onSiteRepo.findById(comicId)
                .map(comic -> comic.yesStock(number))
                .orElse(false);
    }



}
