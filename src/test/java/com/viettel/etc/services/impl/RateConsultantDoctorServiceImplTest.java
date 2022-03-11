package com.viettel.etc.services.impl;

import com.viettel.etc.dto.RateConsultantDoctorDTO;
import com.viettel.etc.repositories.RateConsultantDoctorRepository;
import com.viettel.etc.utils.FnCommon;
import com.viettel.etc.utils.TeleCareException;
import com.viettel.etc.utils.TelecareUserEntity;
import mockit.MockUp;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;


class RateConsultantDoctorServiceImplTest {

    @Mock
    private RateConsultantDoctorRepository rateConsultantDoctorRepository;

    @InjectMocks
    @Spy
    RateConsultantDoctorServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new RateConsultantDoctorServiceImpl();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void rateConsultantDoctor() throws TeleCareException {
        RateConsultantDoctorDTO itemParamsEntity = new RateConsultantDoctorDTO();
        itemParamsEntity.setPatientId(1);
        TelecareUserEntity userSystemEntity = new TelecareUserEntity(){
            @Override
            public boolean isPatient() throws TeleCareException {
                return true;
            }
        };
        userSystemEntity.setTelecareUserId(1);
        Authentication authentication = null;

        new MockUp<FnCommon>(){
            @mockit.Mock
            public  TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
                return userSystemEntity;
            }
        };
        List<RateConsultantDoctorDTO> dataResult = new ArrayList<>();
        Mockito.when(rateConsultantDoctorRepository.rateConsultantDoctor(itemParamsEntity))
                .thenReturn(dataResult);
        MatcherAssert.assertThat(service.rateConsultantDoctor(itemParamsEntity, authentication),
                Matchers.notNullValue());

    }
}