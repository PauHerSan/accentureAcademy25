package com.info_anime.anime_fullPrograms.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.info_anime.anime_fullPrograms.model.animeModel;
import com.info_anime.anime_fullPrograms.rest.animeRest;
import com.info_anime.anime_fullPrograms.service.animeServiceImplements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
public class controllerTest {

    // MOCKEAR: La dependencia del controlador.
    @Mock
    private animeServiceImplements animeService;

    // INYECTAR: El controlador bajo prueba.
    @InjectMocks
    private animeRest animeController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    // Modelos de prueba
    private animeModel animeTest;
    private List<animeModel> animeList;

    @BeforeEach
    void setUp() {
        // Inicialización manual de MockMvc para aislar la capa web.
        mockMvc = MockMvcBuilders.standaloneSetup(animeController).build();
        objectMapper = new ObjectMapper();

        // Configuración de datos de prueba
        animeTest = new animeModel();
        animeTest.setId("1");
        animeTest.setTitulo("Naruto");
        animeTest.setEscritor("Masashi Kishimoto");
        animeTest.setDirector("Hayato Date");
        animeTest.setAnoLanzamiento(2002);
        animeTest.setNumeroCapitulos(720);
        animeTest.setGenero(Arrays.asList("Acción", "Aventura"));

        animeList = List.of(animeTest);
    }

    // --- PRUEBAS DEL ENDPOINT GET /api/anime ---
    @Test
    void getAllAnimes_debeRetornarListaYStatus200() throws Exception {
        when(animeService.getAllAnimes()).thenReturn(animeList);

        mockMvc.perform(get("/api/anime"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Naruto"));
    }

    @Test
    void getAllAnimes_debeManejarExcepcionInternaYRetornar500() throws Exception {
        // Simular una excepción de base de datos o interna del servicio
        when(animeService.getAllAnimes()).thenThrow(new RuntimeException("Error DB"));

        mockMvc.perform(get("/api/anime"))
                .andExpect(status().isInternalServerError());
    }

    // --- PRUEBAS DEL ENDPOINT GET /api/anime/{id} ---
    @Test
    void getAnimeById_debeRetornarAnimeYStatus200() throws Exception {
        when(animeService.getAnimeById("1")).thenReturn(Optional.of(animeTest));

        mockMvc.perform(get("/api/anime/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void getAnimeById_debeRetornar404SiAnimeNoExiste() throws Exception {
        when(animeService.getAnimeById("99")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/anime/{id}", "99"))
                .andExpect(status().isNotFound());
    }

    // --- PRUEBAS DEL ENDPOINT GET /api/anime/titulo/{titulo} ---
    @Test
    void getAnimesByTitulo_debeRetornarListaYStatus200() throws Exception {
        when(animeService.getAnimesByTitulo("Naruto")).thenReturn(animeList);

        mockMvc.perform(get("/api/anime/titulo/{titulo}", "Naruto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void getAnimesByTitulo_debeRetornar404SiNoHayResultados() throws Exception {
        when(animeService.getAnimesByTitulo("Inexistente")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/anime/titulo/{titulo}", "Inexistente"))
                .andExpect(status().isNotFound());
    }

    // --- PRUEBAS DEL ENDPOINT POST /api/anime ---
    @Test
    void createAnime_debeRetornarAnimeCreadoYStatus201() throws Exception {
        animeModel nuevoAnime = new animeModel();
        nuevoAnime.setTitulo("One Piece");

        // El servicio crea y asigna un ID
        animeModel animeCreado = new animeModel();
        animeCreado.setId("2");
        animeCreado.setTitulo("One Piece");

        when(animeService.createAnime(any(animeModel.class))).thenReturn(animeCreado);

        mockMvc.perform(post("/api/anime")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoAnime)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("One Piece"));
    }

    @Test
    void createAnime_debeRetornar400PorFalloDeValidacion() throws Exception {
        animeModel animeInvalido = new animeModel();
        animeInvalido.setTitulo(null); // Asumiendo que el título no puede ser nulo

        // Simular una excepción de validación (ej. campos nulos, reglas de negocio)
        when(animeService.createAnime(any(animeModel.class)))
                .thenThrow(new IllegalArgumentException("Título no puede ser nulo"));

        mockMvc.perform(post("/api/anime")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(animeInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: Título no puede ser nulo"));
    }

    // --- PRUEBAS DEL ENDPOINT PUT /api/anime/{id} ---
    @Test
    void updateAnime_debeRetornarAnimeActualizadoYStatus200() throws Exception {
        animeModel detallesActualizados = new animeModel();
        detallesActualizados.setTitulo("Naruto Shipuuden");

        animeModel animeActualizado = new animeModel();
        animeActualizado.setId("1");
        animeActualizado.setTitulo("Naruto Shipuuden");

        // Usamos eq("1") para asegurar que el ID coincida, y any() para el cuerpo
        when(animeService.updateAnime(eq("1"), any(animeModel.class)))
                .thenReturn(Optional.of(animeActualizado));

        mockMvc.perform(put("/api/anime/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(detallesActualizados)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Naruto Shipuuden"));

        //Verificamos que el metodo del servicio fue llamado exactamente una vez
        verify(animeService, times(1)).updateAnime(eq("1"), any(animeModel.class));
    }

    @Test
    void updateAnime_debeRetornar404SiNoExisteElId() throws Exception {
        animeModel detalles = new animeModel();
        detalles.setTitulo("Nuevo");

        when(animeService.updateAnime(eq("99"), any(animeModel.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/anime/{id}", "99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(detalles)))
                .andExpect(status().isNotFound());
    }

    // --- PRUEBAS DEL ENDPOINT DELETE /api/anime/{id} ---
    @Test
    void deleteAnime_debeRetornarStatus200SiEsExitoso() throws Exception {
        when(animeService.deleteAnimeById("1")).thenReturn(true);

        mockMvc.perform(delete("/api/anime/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Anime eliminado exitosamente"));
    }

    @Test
    void deleteAnime_debeRetornar404SiAnimeNoExiste() throws Exception {
        when(animeService.deleteAnimeById("99")).thenReturn(false);

        mockMvc.perform(delete("/api/anime/{id}", "99"))
                .andExpect(status().isNotFound());
    }
}
