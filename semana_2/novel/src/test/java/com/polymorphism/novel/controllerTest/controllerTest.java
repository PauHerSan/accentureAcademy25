package com.polymorphism.novel.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polymorphism.novel.model.novels;
import com.polymorphism.novel.model.publication;
import com.polymorphism.novel.model.webToon;
import com.polymorphism.novel.rest.publicationRest;
import com.polymorphism.novel.service.publicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(publicationRest.class)
public class controllerTest {

    private MockMvc mockMvc;

    @MockitoBean
    private publicationService publicationService;

    @InjectMocks
    private publicationRest publicationRest;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(publicationRest).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAllPublications_shouldReturnOkAndList() throws Exception {
        // Arrange
        publication testPublication = new novels(1L, "Title", "Writer", "Publisher", 2023, "Genre", "Status", "Editor", 100);
        when(publicationService.getAllPublications()).thenReturn(Collections.singletonList(testPublication));

        // Act & Assert
        mockMvc.perform(get("/api/publications/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Title"));
    }

    @Test
    public void testGetPublicationById_shouldReturnOk() throws Exception {
        // Arrange
        publication testPublication = new webToon(1L, "WebTitle", "WebWriter", "WebPub", 2022, "Action", "Ongoing", "Artist", true);
        when(publicationService.getPublicationById(1L)).thenReturn(testPublication);

        // Act & Assert
        mockMvc.perform(get("/api/publications/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("WebTitle"));
    }

    @Test
    public void testGetPublicationById_shouldReturnNotFound() throws Exception {
        // Arrange
        when(publicationService.getPublicationById(anyLong())).thenThrow(new RuntimeException());

        // Act & Assert
        mockMvc.perform(get("/api/publications/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateNovel_shouldReturnCreated() throws Exception {
        // Arrange
        novels novelToCreate = new novels(null, "New Novel", "Author A", "Publisher B", 2024, "Fantasy", "Pending", "Editor X", 200);
        novels createdNovel = new novels(1L, "New Novel", "Author A", "Publisher B", 2024, "Fantasy", "Pending", "Editor X", 200);
        when(publicationService.createPublication(any(novels.class))).thenReturn(createdNovel);

        // Act & Assert
        mockMvc.perform(post("/api/publications/new-novel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novelToCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testUpdatePublication_whenTypesDoNotMatch_shouldReturnNotFound() throws Exception {
        // Arrange
        novels existingNovel = new novels(1L, "Old Title", "Old Writer", "Old Publisher", 2020, "Old Genre", "Old Status", "Old Editor", 150);
        webToon webToonDetails = new webToon(1L, "New Title", "New Writer", "New Publisher", 2021, "New Genre", "New Status", "New Artist", true);

        // Configurar el mock para que la llamada a 'getPublicationById' devuelva la novela
        when(publicationService.getPublicationById(1L)).thenReturn(existingNovel);

        // Y el mock para que 'updatePublication' lance la excepción
        when(publicationService.updatePublication(anyLong(), any(publication.class)))
                .thenThrow(new RuntimeException("Publication type does not match for update."));

        // Act & Assert
        mockMvc.perform(put("/api/publications/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webToonDetails)))
                .andExpect(status().isNotFound()); // El test espera un 404
    }

    @Test
    public void testDeleteByTitle_shouldReturnOk() throws Exception {
        // Arrange
        String titleToDelete = "Test Title";
        when(publicationService.deletePublicationByTitle(titleToDelete)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/publications/by-title/{title}", titleToDelete)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Publication with title '" + titleToDelete + "' deleted successfully"));
    }

    @Test
    public void testDeleteByTitle_shouldReturnNotFound() throws Exception {
        // Arrange
        String titleToDelete = "NonExistent Title";
        when(publicationService.deletePublicationByTitle(titleToDelete)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/publications/by-title/{title}", titleToDelete)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
