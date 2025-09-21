package com.polymorphism.novel.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polymorphism.novel.model.novels;
import com.polymorphism.novel.model.publication;
import com.polymorphism.novel.model.webToon;
import com.polymorphism.novel.rest.publicationRest;
import com.polymorphism.novel.service.publicationService;
import com.polymorphism.novel.service.publicationServicelmpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(publicationRest.class)
public class controllerTest {

    @Autowired
    private MockMvc mockMvcTest;

    @MockitoBean
    private publicationService publicationServiceTest; //

    @MockitoBean
    private publicationRest publicationRestTest;

    private publication testPublicationControl;
    private novels testNovelControl;
    private webToon testWebToonControl;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        testPublicationControl = new novels();
        testPublicationControl.setId(1L);
        testPublicationControl.setTitle("Test Publication");
        testPublicationControl.setWriter("Test Author");
        testPublicationControl.setPublisher("Test Publisher");
        testPublicationControl.setYearOfRelease(1999);
        testPublicationControl.setGenre("Test Genre");
        testPublicationControl.setStatus("Test Status");

        testNovelControl = new novels();
        testNovelControl.setId(2L);
        testNovelControl.setTitle("Novel Title");
        testNovelControl.setEditor("Novel Author");
        testNovelControl.setChapters(123);

        testWebToonControl = new webToon();
        testWebToonControl.setId(3L);
        testWebToonControl.setTitle("WebToon Title");
        testWebToonControl.setArtist("WebToon Artist");
        testWebToonControl.setFullColor(true);

        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllPublications() throws Exception {
        List<publication> allPublications = Arrays.asList(testPublicationControl, testNovelControl, testWebToonControl);
        when(publicationServiceTest.getAllPublications()).thenReturn(allPublications);

        mockMvcTest.perform(get("/api/publications/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Test Publication"))
                .andExpect(jsonPath("$[1].title").value("Novel Title"))
                .andExpect(jsonPath("$[2].title").value("WebToon Title"));

        verify(publicationServiceTest, times(1)).getAllPublications();
    }

    @Test
    void testGetPublicationById_Success() throws Exception {
        when(publicationServiceTest.getPublicationById(1L)).thenReturn(testPublicationControl);

        mockMvcTest.perform(get("/api/publications/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Title"));

        verify(publicationServiceTest, times(1)).getPublicationById(1L);
    }

    @Test
    void testGetPublicationById_NotFound() throws Exception {
        when(publicationServiceTest.getPublicationById(99L)).thenThrow(new RuntimeException("Publication not found"));

        mockMvcTest.perform(get("/api/publications/{id}", 99L))
                .andExpect(status().isNotFound());

        verify(publicationServiceTest, times(1)).getPublicationById(99L);
    }

    @Test
    void testCreatePublication_Success() throws Exception {
        when(publicationServiceTest.createPublication(any(publication.class))).thenReturn(testPublicationControl);

        mockMvcTest.perform(post("/api/publications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPublicationControl)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Title"));

        verify(publicationServiceTest, times(1)).createPublication(any(publication.class));
    }

    @Test
    void testCreatePublication_BadRequest() throws Exception {
        when(publicationServiceTest.createPublication(any(publication.class))).thenThrow(new RuntimeException());

        mockMvcTest.perform(post("/api/publications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeletePublicationByTitle_Success() throws Exception {
        when(publicationServiceTest.deletePublicationByTitle("Test Title")).thenReturn(true);

        mockMvcTest.perform(delete("/api/publications/byeTitle/{title}", "Test Title"))
                .andExpect(status().isOk())
                .andExpect(content().string("Publication with title 'Test Title' deleted successfully"));

        verify(publicationServiceTest, times(1)).deletePublicationByTitle("Test Title");
    }

    @Test
    void testDeletePublicationByTitle_NotFound() throws Exception {
        when(publicationServiceTest.deletePublicationByTitle("Nonexistent Title")).thenReturn(false);

        mockMvcTest.perform(delete("/api/publications/byeTitle/{title}", "Nonexistent Title"))
                .andExpect(status().isNotFound());

        verify(publicationServiceTest, times(1)).deletePublicationByTitle("Nonexistent Title");
    }

    @Test
    void testGetNovelsByGenre_Success() throws Exception {
        List<novels> novels = Collections.singletonList(testNovelControl);
        when(publicationServiceTest.getNovelsByGenre("Fantasy")).thenReturn(novels);

        mockMvcTest.perform(get("/api/publications/genre/{genre}", "Fantasy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].genre").value("Fantasy"));

        verify(publicationServiceTest, times(1)).getNovelsByGenre("Fantasy");
    }

}
