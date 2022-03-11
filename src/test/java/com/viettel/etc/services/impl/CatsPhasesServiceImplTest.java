package com.viettel.etc.services.impl;

import com.viettel.etc.dto.CatsPhasesDTO;
import com.viettel.etc.repositories.CatsPhasesRepository;
import com.viettel.etc.services.CatsPhasesService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

class CatsPhasesServiceImplTest {

    @Mock
    private CatsPhasesRepository catsPhasesRepository;

    @InjectMocks
    private CatsPhasesService catsPhasesService;

    @BeforeEach
    void setUp() {
        catsPhasesService = new CatsPhasesServiceImpl();
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getCatsPhases_01() {
        Mockito.when(catsPhasesRepository.getCatsPhases(Mockito.any(CatsPhasesDTO.class))).thenReturn(Mockito.any(List.class));
        catsPhasesService.getCatsPhases(new CatsPhasesDTO());
    }
}
