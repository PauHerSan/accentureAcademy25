package com.superComics.inventory.notifications;

import com.superComics.inventory.inventory.events.lowStockAlertEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
public class notificationService {

    @ApplicationModuleListener
    void notifyLowStock(lowStockAlertEvent lowStock) {
        System.out.println("⚠️ ALERTA: Stock bajo detectado!");
        System.out.println("   Producto: " + lowStock.getTitle());
        System.out.println("   Stock actual: " + lowStock.getCurrentStock());
        System.out.println("   Stock mínimo: " + lowStock.getMinimalStock());
    }

}
