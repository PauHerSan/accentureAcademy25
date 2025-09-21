package com.polymorphism.novel.serviceTest;

import com.polymorphism.novel.model.novels;
import com.polymorphism.novel.model.publication;
import com.polymorphism.novel.model.webToon;
import com.polymorphism.novel.repository.publicationRepo;
import com.polymorphism.novel.service.publicationService;
import com.polymorphism.novel.service.publicationServicelmpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class publicationServiceTest {

    @Mock
    private publicationRepo publicationRepo;

    @InjectMocks
    private publicationServicelmpl publicationService;

    private novels testNovel;
    private webToon testWebToon;

    @BeforeEach
    void setUp() {
        testNovel = new novels(1L, "Test Novel", "Writer A", "Publisher B", 2023, "Fantasy", "Ongoing", "Editor X", 150);
        testWebToon = new webToon(2L, "Test Webtoon", "Writer C", "Publisher D", 2022, "Action", "Completed", "Artist Y", true);
    }

    @Test
    void getAllPublications_shouldReturnAllPublications() {
        // Arrange
        when(publicationRepo.findAll()).thenReturn(List.of(testNovel, testWebToon));

        // Act
        List<publication> publications = publicationService.getAllPublications();

        // Assert
        assertNotNull(publications);
        assertEquals(2, publications.size());
        verify(publicationRepo, times(1)).findAll();
    }

    @Test
    void getPublicationById_shouldReturnPublication() {
        // Arrange
        when(publicationRepo.findById(1L)).thenReturn(Optional.of(testNovel));

        // Act
        publication foundPublication = publicationService.getPublicationById(1L);

        // Assert
        assertNotNull(foundPublication);
        assertEquals("Test Novel", foundPublication.getTitle());
    }

    @Test
    void getPublicationById_whenNotFound_shouldThrowException() {
        // Arrange
        when(publicationRepo.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> publicationService.getPublicationById(99L));
    }

    @Test
    void createPublication_shouldReturnCreatedPublication() {
        // Arrange
        when(publicationRepo.save(any(novels.class))).thenReturn(testNovel);

        // Act
        publication createdNovel = publicationService.createPublication(testNovel);

        // Assert
        assertNotNull(createdNovel);
        assertEquals("Test Novel", createdNovel.getTitle());
        verify(publicationRepo, times(1)).save(testNovel);
    }

    @Test
    void updatePublication_shouldUpdateNovelSuccessfully() {
        // Arrange
        novels updatedDetails = new novels(1L, "Updated Title", "Writer A", "Publisher B", 2023, "Fantasy", "Ongoing", "New Editor", 200);
        when(publicationRepo.findById(1L)).thenReturn(Optional.of(testNovel));
        when(publicationRepo.save(any(novels.class))).thenReturn(updatedDetails);

        // Act
        publication result = publicationService.updatePublication(1L, updatedDetails);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("New Editor", ((novels) result).getEditor());
    }

    @Test
    void updatePublication_whenTypesDoNotMatch_shouldThrowException() {
        // Arrange
        when(publicationRepo.findById(1L)).thenReturn(Optional.of(testNovel));
        when(publicationRepo.save(any(webToon.class))).thenReturn(testWebToon);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> publicationService.updatePublication(1L, testWebToon));
    }

    @Test
    void deletePublicationByTitle_shouldReturnTrueWhenExists() {
        // Arrange
        when(publicationRepo.existsByTitle("Test Novel")).thenReturn(true);
        doNothing().when(publicationRepo).deleteByTitle("Test Novel");

        // Act
        boolean isDeleted = publicationService.deletePublicationByTitle("Test Novel");

        // Assert
        assertTrue(isDeleted);
        verify(publicationRepo, times(1)).existsByTitle("Test Novel");
        verify(publicationRepo, times(1)).deleteByTitle("Test Novel");
    }

    @Test
    void deletePublicationByTitle_shouldReturnFalseWhenNotExists() {
        // Arrange
        when(publicationRepo.existsByTitle("Non Existent Title")).thenReturn(false);

        // Act
        boolean isDeleted = publicationService.deletePublicationByTitle("Non Existent Title");

        // Assert
        assertFalse(isDeleted);
        verify(publicationRepo, times(1)).existsByTitle("Non Existent Title");
        verify(publicationRepo, never()).deleteByTitle(anyString());
    }

    @Test
    void getAllNovels_shouldReturnOnlyNovels() {
        // Arrange
        when(publicationRepo.findAllNovels()).thenReturn(List.of(testNovel));

        // Act
        List<novels> foundNovels = publicationService.getAllNovels();

        // Assert
        assertNotNull(foundNovels);
        assertEquals(1, foundNovels.size());
        assertEquals("Test Novel", foundNovels.get(0).getTitle());
        verify(publicationRepo, times(1)).findAllNovels();
    }



}
