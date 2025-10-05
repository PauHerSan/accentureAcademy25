package com.superComics.inventory.notifications.repository;

import com.superComics.inventory.notifications.model.notificationLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface notificationLogRepo extends MongoRepository<notificationLog, String> {

    // Método derivado de ejemplo para consultas
    List<notificationLog> findByEventType(String eventType);

    // Método derivado de ejemplo para buscar por ID de cómic
    List<notificationLog> findByComicItemIdOrderByTimestampDesc(Long comicItemId);

}
