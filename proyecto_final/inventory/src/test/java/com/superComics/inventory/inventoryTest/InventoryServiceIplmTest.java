package com.superComics.inventory.inventoryTest;

import com.superComics.inventory.inventory.events.gradingUpdatedEvent;
import com.superComics.inventory.inventory.events.lowStockAlertEvent;
import com.superComics.inventory.inventory.events.stockQuantityUpdatedEvent;
import com.superComics.inventory.inventory.model.comicItem;
import com.superComics.inventory.inventory.model.grading;
import com.superComics.inventory.inventory.repository.comicRepo;
import com.superComics.inventory.inventory.service.inventoryServiceImpl;
import com.superComics.inventory.notifications.service.notificationService;
import com.superComics.inventory.shared.BusinessException;
import com.superComics.inventory.shared.ComicNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InventoryServiceIplmTest {

    private comicRepo comicRepo;
    private ApplicationEventPublisher eventPublisher;
    private notificationService notificationService;
    private inventoryServiceImpl inventoryService;
    private comicItem mockComic;

    @BeforeEach
    void setUp() {
        comicRepo = mock(comicRepo.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        notificationService = mock(notificationService.class);
        inventoryService = new inventoryServiceImpl(comicRepo, eventPublisher, notificationService);

        mockComic = new comicItem(1L, "SKU123", "Spider-Man #1", 1, "Marvel",
                100.0, 10, 5, new grading("NM"), 100L);

        when(comicRepo.findById(1L)).thenReturn(Optional.of(mockComic));
        when(comicRepo.save(any(comicItem.class))).thenReturn(mockComic);
    }

    // --- updateGrading Tests ---
    @Test
    void updateGrading_Success_ShouldUpdateAndPublishEvent() {
        // Arrange
        String newGradingCode = "GM";
        when(comicRepo.save(any(comicItem.class))).thenAnswer(invocation -> {
            comicItem c = invocation.getArgument(0);
            c.setGrading(new grading(newGradingCode));
            return c;
        });

        // Act
        comicItem result = inventoryService.updateGrading(1L, newGradingCode);

        // Assert
        assertEquals(newGradingCode, result.getGrading().getCode());

        ArgumentCaptor<gradingUpdatedEvent> eventCaptor = ArgumentCaptor.forClass(gradingUpdatedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        assertEquals("NM", eventCaptor.getValue().getOldGradingCode()); // Verifica el oldGrading
    }

    @Test
    void updateGrading_ComicNotFound_ShouldThrowBusinessException() {
        // Arrange
        when(comicRepo.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessException.class, () -> {
            inventoryService.updateGrading(2L, "GM");
        });
        verify(eventPublisher, never()).publishEvent(any());
    }

    // --- byeStocks Tests ---
    @Test
    void byeStocks_Success_ShouldReduceStockAndPublishEvent() {
        // Act: 10 -> 7. No Low Stock.
        inventoryService.byeStocks(1L, 3);

        // Assert
        assertEquals(7, mockComic.getCurrentStock());
        verify(eventPublisher).publishEvent(any(stockQuantityUpdatedEvent.class));
        verify(eventPublisher, never()).publishEvent(any(lowStockAlertEvent.class));
    }

    @Test
    void byeStocks_TriggersLowStockAlert_ShouldPublishTwoEvents() {
        // Arrange: 10 -> 4. Low Stock alert (minimal=5).
        // Act
        inventoryService.byeStocks(1L, 6);

        // Assert
        assertEquals(4, mockComic.getCurrentStock());
        ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
        verify(eventPublisher, times(2)).publishEvent(eventCaptor.capture());

        List<Object> capturedEvents = eventCaptor.getAllValues();
        assertTrue(capturedEvents.get(0) instanceof stockQuantityUpdatedEvent);
        assertTrue(capturedEvents.get(1) instanceof lowStockAlertEvent);
    }

    @Test
    void byeStocks_InsufficientStock_ShouldThrowException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            inventoryService.byeStocks(1L, 11);
        });
        verify(comicRepo, never()).save(any());
    }

    // --- plusStocks Tests ---
    @Test
    void plusStocks_Success_ShouldIncreaseStockAndPublishEvent() {
        // Act: 10 -> 15.
        inventoryService.plusStocks(1L, 5);

        // Assert
        assertEquals(15, mockComic.getCurrentStock());
        verify(eventPublisher).publishEvent(any(stockQuantityUpdatedEvent.class));
    }

    @Test
    void getComicsById_NotFound_ShouldThrowComicNotFoundException() {
        // Arrange
        when(comicRepo.findById(2L)).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(ComicNotFoundException.class, () -> {
            inventoryService.getComicsById(2L);
        });
    }

    @Test
    void getComicsWithLowStock_ShouldFilterCorrectly() {
        // Arrange
        comicItem c1 = new comicItem(1L, "S1", "C1", 1, "M", 100.0, 2, 5, new grading("NM"), 1L); // Low
        comicItem c2 = new comicItem(2L, "S2", "C2", 1, "M", 100.0, 6, 5, new grading("NM"), 1L); // Normal
        when(comicRepo.findAll()).thenReturn(List.of(c1, c2));

        // Act
        List<comicItem> result = inventoryService.getComicsWithLowStock();

        // Assert
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

}
