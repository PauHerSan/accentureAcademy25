package com.polymorphism.novel.serviceTest;

import com.polymorphism.novel.repository.novelRepo;
import com.polymorphism.novel.service.publicationServicelmpl;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class serviceTest {

    @Mock
    private novelRepo repository;

    @InjectMocks
    private publicationServicelmpl service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

}
