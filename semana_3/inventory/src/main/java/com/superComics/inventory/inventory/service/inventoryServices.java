package com.superComics.inventory.inventory.service;

import com.superComics.inventory.inventory.model.comic;
import com.superComics.inventory.inventory.model.grading;


import java.util.List;


public interface inventoryServices {


    //Crear un nuevo comic
    comic newComic(Long id, String sku, String title, Integer issueNumber, String publisher, double estimatedValue, Integer currentStock, Integer minimalStock, grading grading, Long traderId);

    //Cambio de grading
    comic updateGrading(Long comicId, String newGradingCode);

    //Reducir Stock
    void byeStocks(Long comicId, int number);

    //Aumentar Stock
    void plusStocks(Long comicId, int number);

    //Obtener lista de todos los comics
    List<comic> getAllComics();

    //Obtener comic por id
    comic getComicsById(Long comicId);

    //Obtener comic con bajo stock
    List<comic> getComicsWithLowStock();

    //Obtener comic por título
    List<comic> getComicsByTitle(String title);

    //Obtener comic por editorial (publisher)
    List<comic> getComicsByPublisher(String publisher);

    //Verificar Stock
    boolean verifyStock(Long comicId, int number);
}
