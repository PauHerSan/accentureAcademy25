package com.info_anime.anime_fullPrograms.serviceTest;

import com.info_anime.anime_fullPrograms.model.animeModel;
import com.info_anime.anime_fullPrograms.repository.animeRepo;
import com.info_anime.anime_fullPrograms.service.animeServiceImplements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class serviceTest {

    @Mock
    private animeRepo animeRepository;

    @InjectMocks
    private animeServiceImplements animeService;

    private animeModel animeTest;
    private List<animeModel> animeList;

    @BeforeEach
    void setUp() {
        animeTest = new animeModel();
        animeTest.setId("1");
        animeTest.setTitulo("Naruto");
        animeTest.setEscritor("Masashi Kishimoto");
        animeTest.setDirector("Hayato Date");
        animeTest.setAnoLanzamiento(2002);
        animeTest.setNumeroCapitulos(720);
        animeTest.setGenero(Arrays.asList("Acción", "Aventura"));

        animeList = Arrays.asList(animeTest);
    }

    // ========================================
    // TESTS PARA FIND ALL
    // ========================================

    @Test
    void testFindAll_Success() {
        // Given
        when(animeRepository.findAll()).thenReturn(animeList);

        // When
        List<animeModel> result = animeService.getAllAnimes();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Naruto", result.get(0).getTitulo());
        verify(animeRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_EmptyList() {
        // Given
        when(animeRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<animeModel> result = animeService.getAllAnimes();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(animeRepository, times(1)).findAll();
    }

    // ========================================
    // TESTS PARA FIND BY ID
    // ========================================

    @Test
    void testFindById_Success() {
        // Given
        when(animeRepository.findById("1")).thenReturn(Optional.of(animeTest));

        // When
        Optional<animeModel> result = animeService.getAnimeById("1");

        // Then
        assertTrue(result.isPresent());
        assertEquals("Naruto", result.get().getTitulo());
        verify(animeRepository, times(1)).findById("1");
    }

    @Test
    void testFindById_NotFound() {
        // Given
        when(animeRepository.findById("999")).thenReturn(Optional.empty());

        // When
        Optional<animeModel> result = animeService.getAnimeById("999");

        // Then
        assertFalse(result.isPresent());
        verify(animeRepository, times(1)).findById("999");
    }

    // ========================================
    // TESTS PARA FIND BY TITULO
    // ========================================

    @Test
    void testFindByTitulo_Success() {
        // Given
        when(animeRepository.findByTituloContainingIgnoreCase("Naruto")).thenReturn(animeList);

        // When
        List<animeModel> result = animeService.getAnimesByTitulo("Naruto");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(animeRepository, times(1)).findByTituloContainingIgnoreCase("Naruto");
    }

    @Test
    void testFindByTitulo_EmptyTitle_ThrowsException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> animeService.getAnimesByTitulo("")
        );
        assertEquals("El título no puede estar vacío", exception.getMessage());
        verify(animeRepository, never()).findByTituloContainingIgnoreCase(anyString());
    }

    @Test
    void testFindByTitulo_NullTitle_ThrowsException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> animeService.getAnimesByTitulo(null)
        );
        assertEquals("El título no puede estar vacío", exception.getMessage());
    }

    // ========================================
    // TESTS PARA CREATE ANIME
    // ========================================

    @Test
    void testCreateAnime_Success() {
        // Given
        when(animeRepository.findByTituloContainingIgnoreCase("Naruto")).thenReturn(Collections.emptyList());
        when(animeRepository.save(animeTest)).thenReturn(animeTest);

        // When
        animeModel result = animeService.createAnime(animeTest);

        // Then
        assertNotNull(result);
        assertEquals("Naruto", result.getTitulo());
        verify(animeRepository, times(1)).save(animeTest);
    }

    @Test
    void testCreateAnime_DuplicateTitle_ThrowsException() {
        // Given
        when(animeRepository.findByTituloContainingIgnoreCase("Naruto")).thenReturn(animeList);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> animeService.createAnime(animeTest)
        );
        assertEquals("Ya existe un anime con este título", exception.getMessage());
        verify(animeRepository, never()).save(any());
    }

    @Test
    void testCreateAnime_NullAnime_ThrowsException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> animeService.createAnime(null)
        );
        assertEquals("El anime no puede ser nulo", exception.getMessage());
    }

    @Test
    void testCreateAnime_EmptyTitle_ThrowsException() {
        // Given
        animeTest.setTitulo("");

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> animeService.createAnime(animeTest)
        );
        assertEquals("El título es obligatorio", exception.getMessage());
    }

    @Test
    void testCreateAnime_InvalidYear_ThrowsException() {
        // Given
        animeTest.setAnoLanzamiento(1800);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> animeService.createAnime(animeTest)
        );
        assertEquals("El año de lanzamiento debe estar entre 1900 y 2030", exception.getMessage());
    }

    // ========================================
    // TESTS PARA DELETE BY ID
    // ========================================

    @Test
    void testDeleteById_Success() {
        // Given
        when(animeRepository.existsById("1")).thenReturn(true);

        // When
        boolean result = animeService.deleteAnimeById("1");

        // Then
        assertTrue(result);
        verify(animeRepository, times(1)).existsById("1");
        verify(animeRepository, times(1)).deleteById("1");
    }

    @Test
    void testDeleteById_NotFound() {
        // Given
        when(animeRepository.existsById("999")).thenReturn(false);

        // When
        boolean result = animeService.deleteAnimeById("999");

        // Then
        assertFalse(result);
        verify(animeRepository, times(1)).existsById("999");
        verify(animeRepository, never()).deleteById(anyString());
    }

    // ========================================
    // TESTS PARA DELETE BY TITULO
    // ========================================

    @Test
    void testDeleteByTitulo_Success() {
        // Given
        when(animeRepository.findByTituloContainingIgnoreCase("Naruto")).thenReturn(animeList);

        // When
        int result = animeService.deleteAnimesByTitulo("Naruto");

        // Then
        assertEquals(1, result);
        verify(animeRepository, times(1)).findByTituloContainingIgnoreCase("Naruto");
        verify(animeRepository, times(1)).deleteAll(animeList);
    }

    @Test
    void testDeleteByTitulo_NoMatches() {
        // Given
        when(animeRepository.findByTituloContainingIgnoreCase("No Existe")).thenReturn(Collections.emptyList());

        // When
        int result = animeService.deleteAnimesByTitulo("No Existe");

        // Then
        assertEquals(0, result);
        verify(animeRepository, never()).deleteAll(anyList());
    }
}
