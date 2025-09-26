package com.superComics.inventory.notifications;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
public class notificationEvent {

    @ApplicationModuleListener
    void notifyLowStock(lowStock lowStock){
        System.out.println("Notification Event - Low Stock");
    }

}
