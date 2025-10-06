package com.superComics.inventory.inventoryTest;

import com.superComics.inventory.inventory.model.grading;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GradingTest {

    // Prueba para un código válido
    @Test
    void constructor_WithValidCode_ShouldCreateGrading() {
        // Arrange & Act
        grading g = new grading("NM");
        // Assert
        assertEquals("NM", g.getCode());
    }

    // Prueba para un código válido en minúsculas (debe normalizar a mayúsculas)
    @Test
    void constructor_WithLowercaseCode_ShouldNormalize() {
        // Arrange & Act
        grading g = new grading("vf");
        // Assert
        assertEquals("VF", g.getCode());
    }

    // Prueba para un código no válido (excepción)
    @Test
    void constructor_WithInvalidCode_ShouldThrowException() {
        // Arrange, Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new grading("EX");
        });
        assertTrue(thrown.getMessage().contains("Formato de grading no válido."));
    }

    // Prueba para código nulo (excepción)
    @Test
    void constructor_WithNullCode_ShouldThrowException() {
        // Arrange, Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            new grading(null);
        });
        assertTrue(thrown.getMessage().contains("no puede ser nulo o vacío."));
    }

    // Prueba de igualdad
    @Test
    void equals_and_hashCode_ShouldBeConsistent() {
        grading g1 = new grading("GM");
        grading g2 = new grading("GM");
        grading g3 = new grading("NMM");

        // Equals
        assertEquals(g1, g2);
        assertNotEquals(g1, g3);

        // HashCode
        assertEquals(g1.hashCode(), g2.hashCode());
        assertNotEquals(g1.hashCode(), g3.hashCode());
    }

}
