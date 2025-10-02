package com.superComics.inventory.notifications;

import com.superComics.inventory.inventory.events.gradingUpdatedEvent;
import com.superComics.inventory.inventory.events.lowStockAlertEvent;
import com.superComics.inventory.inventory.events.stockQuantityUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class notificationServiceImpl implements notificationService {

    private static final Logger log = LoggerFactory.getLogger(notificationServiceImpl.class);

    /**
     * Reacciona al evento de alerta de stock bajo.
     * Esto enviaría un email al equipo de compras o al proveedor.
     * @param event El evento de alerta de stock bajo.
     */
    @EventListener
    @Override
    public void handleLowStockAlert(lowStockAlertEvent event) {
        log.warn("🚨 ALERTA DE STOCK BAJO RECIBIDA 🚨");
        log.warn("ID Cómic: {}, Título: '{}'", event.getComicId(), event.getTitle());
        log.warn("Stock actual: {} (Mínimo: {}). Se necesita reabastecimiento urgente.",
                event.getCurrentStock(), event.getMinimalStock());

        // Aquí iría la lógica real para enviar un email o un mensaje a un sistema de tickets.
        // Ejemplo de simulación: EmailSender.sendAlert(event.getTitle(), "Stock bajo");
    }

    /**
     * Reacciona al evento de actualización de grading.
     * Esto podría notificar al proveedor que el estado de su cómic ha cambiado o registrar un cambio interno.
     * @param event El evento de actualización de grading.
     */
    @EventListener
    @Override
    public void handleGradingUpdate(gradingUpdatedEvent event) {
        log.info("📝 CAMBIO DE GRADING REGISTRADO 📝");
        log.info("SKU: {}. Grading cambió de {} a {}.",
                event.getSku(), event.getOldGradingCode(), event.getNewGradingCode());

    }

    /**
     * Reacciona al evento de actualización de stock (aumento o reducción).
     * Esto podría usarse para actualizar paneles de control en tiempo real o logs.
     * @param event El evento de actualización de stock.
     */
    @EventListener
    @Override
    public void handleStockQuantityUpdate(stockQuantityUpdatedEvent event) {
        String action = event.getChangeQuantity() > 0 ? "AUMENTO" : "REDUCCIÓN";

        log.info("📊 ACTUALIZACIÓN DE STOCK ({}) 📊", action);
        log.info("SKU: {}. Stock anterior: {}, Nuevo stock: {}. Cambio: {}",
                event.getSku(), event.getOldStock(), event.getNewStock(), event.getChangeQuantity());
    }
}
