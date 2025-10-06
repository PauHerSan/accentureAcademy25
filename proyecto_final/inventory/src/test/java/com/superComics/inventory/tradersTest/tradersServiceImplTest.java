package com.superComics.inventory.tradersTest;

import com.superComics.inventory.shared.BusinessException;
import com.superComics.inventory.traders.model.traders;
import com.superComics.inventory.traders.service.tradersServiceImpl;
import com.superComics.inventory.traders.repository.tradersRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class tradersServiceImplTest {

    @Mock
    private tradersRepo tradersRepo;

    @InjectMocks
    private tradersServiceImpl traderService;

    private traders mockTrader;

    @BeforeEach
    void setUp() {
        // Inicializar Mocks
        tradersRepo = mock(tradersRepo.class);
        ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
        traderService = new tradersServiceImpl(tradersRepo, eventPublisher);

        mockTrader = new traders(1L, "Peter Parker", "peter@dailybugle.com", "Marvel",
                5.0, "Queens, NY", true);

        when(tradersRepo.findById(1L)).thenReturn(Optional.of(mockTrader));
        when(tradersRepo.save(any(traders.class))).thenAnswer(i -> i.getArguments()[0]);
    }

    // --- createNewTrader Tests ---
    @Test
    void createNewTrader_Success_ShouldSaveTrader() {
        // Arrange
        when(tradersRepo.findByEmail("new@trader.com")).thenReturn(Optional.empty());

        // Act
        traders result = traderService.createNewTrader("New Trader", "new@trader.com", "DC",
                3.0, "Gotham");

        // Assert
        verify(tradersRepo).save(any(traders.class));
        assertTrue(result.isActive());
    }

    @Test
    void createNewTrader_EmailExists_ShouldThrowBusinessException() {
        // Arrange
        when(tradersRepo.findByEmail(mockTrader.getEmail())).thenReturn(Optional.of(mockTrader));

        // Act & Assert
        assertThrows(BusinessException.class, () -> {
            traderService.createNewTrader(mockTrader.getName(), mockTrader.getEmail(), "Marvel",
                    5.0, "Queens, NY");
        });
        verify(tradersRepo, never()).save(any());
    }

    // --- deactivateTrader Tests ---
    @Test
    void deactivateTrader_ActiveTrader_ShouldDeactivateAndSave() {
        // Arrange
        mockTrader.setActive(true);

        // Act
        traderService.deactivateTrader(1L);

        // Assert
        verify(tradersRepo).save(mockTrader);
        assertFalse(mockTrader.isActive());
    }

    @Test
    void deactivateTrader_InactiveTrader_ShouldThrowBusinessException() {
        // Arrange
        mockTrader.setActive(false);

        // Act & Assert
        assertThrows(BusinessException.class, () -> {
            traderService.deactivateTrader(1L);
        });
        verify(tradersRepo, never()).save(any());
    }

    @Test
    void getAllActiveTraders_Success() {
        // Arrange
        traders activeTrader = new traders(2L, "Lois", "lois@dailyplanet.com", "DC", 1.0, "Metro", true);
        when(tradersRepo.findAllByIsActiveTrue()).thenReturn(List.of(mockTrader, activeTrader));

        // Act
        List<traders> result = traderService.getAllActiveTraders();

        // Assert
        assertEquals(2, result.size());
        verify(tradersRepo).findAllByIsActiveTrue();
    }

}
