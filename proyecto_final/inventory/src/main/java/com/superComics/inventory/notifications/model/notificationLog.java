package com.superComics.inventory.notifications.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notificationLogs")
public class notificationLog {


    @Id
    private String id;

    private Long comicItemId;
    private Long traderId; // Útil para logs de órdenes
    private String eventType; // Por ejemplo: "LOW_STOCK_ALERT", "GRADING_UPDATE", "ORDER_COMPLETE"
    private String message;
    private Instant timestamp;

    // Constructor simplificado para la persistencia de eventos
    public notificationLog(Long comicItemId, String eventType, String message) {
        this.comicItemId = comicItemId;
        this.eventType = eventType;
        this.message = message;
        this.timestamp = Instant.now();
        this.traderId = null; // Se inicializa a null si no es relevante
    }

    // Constructor con traderId (útil para eventos de órdenes)
    public notificationLog(Long comicItemId, Long traderId, String eventType, String message) {
        this.comicItemId = comicItemId;
        this.traderId = traderId;
        this.eventType = eventType;
        this.message = message;
        this.timestamp = Instant.now();
    }
}
