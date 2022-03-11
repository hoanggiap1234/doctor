package com.viettel.etc.controllers;
import com.viettel.etc.dto.RateConsultantDoctorDTO;
import com.viettel.etc.services.RateConsultantDoctorService;
import com.viettel.etc.xlibrary.core.constants.FunctionCommon;
import com.viettel.etc.xlibrary.core.entities.UserSystemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.viettel.etc.utils.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.viettel.etc.controllers.RateConsultantDoctorController;
import org.hamcrest.MatcherAssert;
import com.viettel.etc.utils.ErrorApp;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import com.viettel.etc.utils.TeleCareException;
import org.hamcrest.MatcherAssert;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.extension.ExtendWith;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@ExtendWith(MockitoExtension.class)
class RateConsultantDoctorControllerTest {

    private MockMvc mvc;
    @Mock
    private RateConsultantDoctorService rateConsultantDoctorService;
    @InjectMocks
    RateConsultantDoctorController RateConsultantDoctorController;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(RateConsultantDoctorController).build();
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    void rateConsultantDoctor3() throws Exception {
        Authentication authentication = null;
        Integer patientId = new Integer(0);
        RateConsultantDoctorDTO dataParams = new RateConsultantDoctorDTO();
        ResultSelectEntity resultData1 = new ResultSelectEntity();
        when(rateConsultantDoctorService.rateConsultantDoctor(Mockito.any(),Mockito.any())).thenReturn(resultData1);
        MockHttpServletResponse responseExpected = mvc.perform(
                post("/"+patientId+"/rate-consultant-doctor")
                        .accept(MediaType.APPLICATION_JSON)
                        .content((new ObjectMapper()).writeValueAsString(dataParams))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // assert result
        assertThat(responseExpected.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
    }

    @Test
    void rateConsultantDoctorThrowException3() throws Exception {
        Authentication authentication = null;
        Integer patientId = new Integer(0);
        RateConsultantDoctorDTO dataParams = new RateConsultantDoctorDTO();
        ResultSelectEntity resultData1 = new ResultSelectEntity();
        when(rateConsultantDoctorService.rateConsultantDoctor(Mockito.any(),Mockito.any())).thenReturn(resultData1);
        MockHttpServletResponse responseExpected = mvc.perform(
                post("/"+patientId+"/rate-consultant-doctor")
                        .accept(MediaType.APPLICATION_JSON)
                        .content((new ObjectMapper()).writeValueAsString(dataParams))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // assert result
        assertThat(responseExpected.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
    }

}