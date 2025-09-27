package com.superComics.inventory.inventory.events;

import com.superComics.inventory.shared.lowStock;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
public class inventoryAlertListener {

    @ApplicationModuleListener
    void notifyLowStock(lowStock lowStock) {
        System.out.println("⚠️ ALERTA: Stock bajo detectado!");
        System.out.println("   Producto: " + lowStock.getTitle());
        System.out.println("   Stock actual: " + lowStock.getCurrentStock());
        System.out.println("   Stock mínimo: " + lowStock.getMinimalStock());
    }

}
