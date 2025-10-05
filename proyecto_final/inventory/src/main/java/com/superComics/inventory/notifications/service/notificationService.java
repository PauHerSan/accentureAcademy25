package com.superComics.inventory.notifications.service;

import com.superComics.inventory.inventory.events.gradingUpdatedEvent;
import com.superComics.inventory.inventory.events.lowStockAlertEvent;
import com.superComics.inventory.inventory.events.stockQuantityUpdatedEvent;


public interface notificationService {


    void handleLowStockAlert(lowStockAlertEvent event);

    void handleGradingUpdate(gradingUpdatedEvent event);

    void handleStockQuantityUpdate(stockQuantityUpdatedEvent event);

}
