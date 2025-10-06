package com.superComics.inventory.inventoryTest;

import com.superComics.inventory.inventory.model.comicItem;
import com.superComics.inventory.inventory.model.grading;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ComicItemTest {

    private comicItem comic;

    @BeforeEach
    void setUp() {
        // currentStock: 10, minimalStock: 5
        comic = new comicItem(1L, "SKU123", "Spider-Man #1", 1, "Marvel",
                1000.0, 10, 5, new grading("VF"), 100L);
    }

    // --- Pruebas de byeStock (Reducir Stock) ---
    @Test
    void byeStock_ValidReduction_ShouldDecreaseStock() {
        // Act
        comic.byeStock(3);
        // Assert
        assertEquals(7, comic.getCurrentStock());
    }

    @Test
    void byeStock_ReductionExceedsStock_ShouldThrowException() {
        // Arrange, Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            comic.byeStock(11);
        });
        assertTrue(thrown.getMessage().contains("Hay pocas piezas disponibles."));
    }

    @Test
    void byeStock_InvalidReductionZeroOrNegative_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> comic.byeStock(0));
        assertThrows(IllegalArgumentException.class, () -> comic.byeStock(-1));
    }

    // --- Pruebas de plusStock (Aumentar Stock) ---
    @Test
    void plusStock_ValidIncrease_ShouldIncreaseStock() {
        // Act
        comic.plusStock(5);
        // Assert
        assertEquals(15, comic.getCurrentStock());
    }

    @Test
    void plusStock_InvalidIncreaseZeroOrNegative_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> comic.plusStock(0));
        assertThrows(IllegalArgumentException.class, () -> comic.plusStock(-5));
    }

    // --- Pruebas de needRestock (Stock Bajo) ---
    @Test
    void needRestock_StockBelowMinimal_ShouldReturnTrue() {
        // Arrange
        comic.setCurrentStock(4);
        // Assert
        assertTrue(comic.needRestock());
    }

    @Test
    void needRestock_StockAboveMinimal_ShouldReturnFalse() {
        // Assert
        assertFalse(comic.needRestock());
    }

    // --- Pruebas de yesStock (Verificar Stock) ---
    @Test
    void yesStock_SufficientStock_ShouldReturnTrue() {
        assertTrue(comic.yesStock(10));
    }

    @Test
    void yesStock_InsufficientStock_ShouldReturnFalse() {
        assertFalse(comic.yesStock(11));
    }


}
