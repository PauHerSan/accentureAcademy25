package com.polymorphism.novel.serviceTest;

import com.polymorphism.novel.model.novels;
import com.polymorphism.novel.model.publication;
import com.polymorphism.novel.model.webToon;
import com.polymorphism.novel.repository.publicationRepo;
import com.polymorphism.novel.service.publicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class publicationServiceTest {

    @Mock
    private publicationRepo repository;

    @InjectMocks
    private publicationService service;

    private publication testPublication;
    private novels testNovel;
    private webToon testWebToon;
    private List<publication> testPublications;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPublications_ReturnsAllPublications() {
        //Preparar datos de prueba
        novels novel = new novels();
        novel.setTitle("Solo Leveling");
        webToon webToon = new webToon();
        webToon.setTitle("Tower of God");

        List<publication> mockPublications = Arrays.asList(novel, webToon);
//
        //Definir el comportamiento del simulacro
        when(repository.findAll()).thenReturn(mockPublications);

        //Llamar al metodo del servicio
        List<publication> result = service.getAllPublications();

        //Verificar el resultado
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Solo Leveling", result.get(0).getTitle());

        //Verificar que el metodo del repositorio fue llamado
        verify(repository, times(1)).findAll();
    }
    @Test
    void testGetAllPublications_EmptyList() {
        //Preparar datos vacíos
        when(repository.findAll()).thenReturn(Collections.emptyList());

        //Ejecutar
        List<publication> result = service.getAllPublications();

        //Verificar
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
    }

}
