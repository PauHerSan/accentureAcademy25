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

    @MockitoBean
    private publicationRepo repoTest;

    @InjectMocks
    private publicationServicelmpl serviceTest; //

    private novels testNovelPublication;
    private novels testNovel;
    private webToon testWebToon;
    private publication testGenericPublication;


    @BeforeEach
    void setUp() {

        testNovelPublication = new novels();
        testNovelPublication.setId(1L);
        testNovelPublication.setTitle("Test Publication");
        testNovelPublication.setWriter("Test Author");
        testNovelPublication.setPublisher("Test Publisher");
        testNovelPublication.setYearOfRelease(1999);
        testNovelPublication.setGenre("Test Genre");
        testNovelPublication.setStatus("Test Status");


        testNovel = new novels();
        testNovel.setId(2L);
        testNovel.setTitle("Test Novel");
        testNovel.setEditor("Test Editor");
        testNovel.setChapters(250);


        testWebToon = new webToon();
        testWebToon.setId(3L);
        testWebToon.setTitle("Test WebToon");
        testWebToon.setArtist("Test Artist");
        testWebToon.setFullColor(true);

        testGenericPublication = new novels();
        testGenericPublication.setId(4L);
        testGenericPublication.setTitle("Generic Publication");
        testGenericPublication.setWriter("Generic Author");

    }

    @Test
    void testGetAllPublications() {
        List<publication> allPublications = Arrays.asList(testNovelPublication, testNovel, testWebToon);
        when(repoTest.findAll()).thenReturn(allPublications);

        List<publication> result = serviceTest.getAllPublications();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Test Publication", result.get(0).getTitle());
        verify(repoTest, times(1)).findAll();
    }

    @Test
    void testGetAllNovels() {
        List<publication> mixedList = Arrays.asList(testNovelPublication, testNovel, testWebToon);
        when(repoTest.findAll()).thenReturn(mixedList);

        List<novels> result = serviceTest.getAllNovels();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test Publication", result.get(0).getTitle());
        assertEquals("Test Novel", result.get(1).getTitle());
        verify(repoTest, times(1)).findAll();
    }

    @Test
    void testGetAllWebToons() {
        List<publication> mixedList = Arrays.asList(testNovelPublication, testNovel, testWebToon);
        when(repoTest.findAll()).thenReturn(mixedList);

        List<webToon> result = serviceTest.getAllWebToons();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test WebToon", result.get(0).getTitle());
        verify(repoTest, times(1)).findAll();
    }

    @Test
    void testGetPublicationById_Success() {
        when(repoTest.findById(1L)).thenReturn(Optional.of(testNovelPublication));

        publication result = serviceTest.getPublicationById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(repoTest, times(1)).findById(1L);
    }

    @Test
    void testGetPublicationById_NotFound() {
        when(repoTest.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> serviceTest.getPublicationById(99L));

        verify(repoTest, times(1)).findById(99L);
    }

    @Test
    void testGetPublicationsByTitle() {
        when(repoTest.findByTitleIgnoreCase("Test Publication")).thenReturn(Collections.singletonList(testNovelPublication));

        List<publication> result = serviceTest.getPublicationsByTitle("Test Publication");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Publication", result.get(0).getTitle());
        verify(repoTest, times(1)).findByTitleIgnoreCase("Test Publication");
    }

    @Test
    void testGetPublicationsByAuthor() {
        when(repoTest.findByWriterIgnoreCase("Test Author")).thenReturn(Collections.singletonList(testNovelPublication));

        List<publication> result = serviceTest.getPublicationsByAuthor("Test Author");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Author", result.get(0).getWriter());
        verify(repoTest, times(1)).findByWriterIgnoreCase("Test Author");
    }

    @Test
    void testGetNovelsByGenre() {
        List<publication> mixedList = Arrays.asList(testNovel, testNovelPublication, testWebToon);
        when(repoTest.findAll()).thenReturn(mixedList);

        List<novels> result = serviceTest.getNovelsByGenre("Fantasy");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Novel", result.get(0).getTitle());
        verify(repoTest, times(1)).findAll();
    }

    @Test
    void testCreatePublication() {
        when(repoTest.save(any(publication.class))).thenReturn(testGenericPublication);

        publication result = serviceTest.createPublication(testGenericPublication);

        assertNotNull(result);
        assertEquals("Generic Publication", result.getTitle());
        verify(repoTest, times(1)).save(any(publication.class));
    }

    @Test
    void testUpdatePublication_Novel() {
        novels updatedNovelDetails = new novels();
        updatedNovelDetails.setTitle("Updated Novel Title");
        updatedNovelDetails.setWriter("Updated Novel Author");
        updatedNovelDetails.setEditor("New Editor");
        updatedNovelDetails.setChapters(150);

        when(repoTest.findById(2L)).thenReturn(Optional.of(testNovel));
        when(repoTest.save(any(novels.class))).thenReturn(updatedNovelDetails);

        novels updatedNovel = (novels) serviceTest.updatePublication(2L, updatedNovelDetails);

        assertNotNull(updatedNovel);
        assertEquals("Updated Novel Title", updatedNovel.getTitle());
        assertEquals("New Editor", updatedNovel.getEditor());
        assertEquals(150, updatedNovel.getChapters());
        verify(repoTest, times(1)).findById(2L);
        verify(repoTest, times(1)).save(any(novels.class));
    }

    @Test
    void testDeletePublicationByTitle() {
        doNothing().when(repoTest).deleteByTitle("Test Publication");

        boolean isDeleted = serviceTest.deletePublicationByTitle("Test Publication");

        assertTrue(isDeleted);
        verify(repoTest, times(1)).deleteByTitle("Test Publication");
    }

    @Test
    void testUpdatePublication_WebToon() {
        webToon updatedWebToonDetails = new webToon();
        updatedWebToonDetails.setTitle("Updated WebToon Title");
        updatedWebToonDetails.setWriter("Updated WebToon Author");
        updatedWebToonDetails.setArtist("New Artist");
        updatedWebToonDetails.setFullColor(false);

        when(repoTest.findById(3L)).thenReturn(Optional.of(testWebToon));
        when(repoTest.save(any(webToon.class))).thenReturn(updatedWebToonDetails);

        webToon updatedWebToon = (webToon) serviceTest.updatePublication(3L, updatedWebToonDetails);

        assertNotNull(updatedWebToon);
        assertEquals("Updated WebToon Title", updatedWebToon.getTitle());
        assertEquals("New Artist", updatedWebToon.getArtist());
        assertFalse(updatedWebToon.isFullColor());
        verify(repoTest, times(1)).findById(3L);
        verify(repoTest, times(1)).save(any(webToon.class));
    }



}
