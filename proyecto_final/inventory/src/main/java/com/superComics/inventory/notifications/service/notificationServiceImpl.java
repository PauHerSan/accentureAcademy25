package com.superComics.inventory.notifications.service;

import com.superComics.inventory.inventory.events.gradingUpdatedEvent;
import com.superComics.inventory.inventory.events.lowStockAlertEvent;
import com.superComics.inventory.inventory.events.stockQuantityUpdatedEvent;
import com.superComics.inventory.notifications.model.notificationLog;
import com.superComics.inventory.notifications.repository.notificationLogRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class notificationServiceImpl implements notificationService {

    private static final Logger log = LoggerFactory.getLogger(notificationServiceImpl.class);
    private final notificationLogRepo logRepository;

    public notificationServiceImpl(notificationLogRepo logRepository) {
        this.logRepository = logRepository;
    }


    /**
     * Reacciona al evento de alerta de stock bajo.
     */
    @EventListener
    @Override
    public void handleLowStockAlert(lowStockAlertEvent event) {
        log.warn("🚨 ALERTA DE STOCK BAJO RECIBIDA 🚨");
        log.warn("ID Cómic: {}, Título: '{}'", event.getComicId(), event.getTitle());
        log.warn("Stock actual: {} (Mínimo: {}). Se necesita reabastecimiento urgente.",
                event.getCurrentStock(), event.getMinimalStock());

        // PERSISTENCIA EN MONGODB:
        notificationLog logEntry = new notificationLog(
                event.getComicId(),
                "LOW_STOCK_ALERT",
                "Stock bajo: " + event.getCurrentStock() + "/" + event.getMinimalStock()
        );
        logRepository.save(logEntry);
    }

    /**
     * Reacciona al evento de actualización de grading.
     */
    @EventListener
    @Override
    public void handleGradingUpdate(gradingUpdatedEvent event) {
        log.info("📝 CAMBIO DE GRADING REGISTRADO 📝");
        log.info("SKU: {}. Grading cambió de {} a {}.",
                event.getSku(), event.getOldGradingCode(), event.getNewGradingCode());

        // PERSISTENCIA EN MONGODB:
        notificationLog logEntry = new notificationLog(
                event.getComicId(),
                "GRADING_UPDATE",
                "Grading cambiado de " + event.getOldGradingCode() + " a " + event.getNewGradingCode()
        );
        logRepository.save(logEntry);
    }

    /**
     * Reacciona al evento de actualización de stock.
     */
    @EventListener
    @Override
    public void handleStockQuantityUpdate(stockQuantityUpdatedEvent event) {
        String action = event.getChangeQuantity() > 0 ? "AUMENTO" : "REDUCCIÓN";

        log.info("📊 ACTUALIZACIÓN DE STOCK ({}) 📊", action);
        log.info("SKU: {}. Stock anterior: {}, Nuevo stock: {}. Cambio: {}",
                event.getSku(), event.getOldStock(), event.getNewStock(), event.getChangeQuantity());

        // PERSISTENCIA EN MONGODB:
        notificationLog logEntry = new notificationLog(
                event.getComicId(),
                "STOCK_CHANGE",
                "Stock: " + action + " en " + event.getChangeQuantity()
        );

        logRepository.save(logEntry);

    }
}
