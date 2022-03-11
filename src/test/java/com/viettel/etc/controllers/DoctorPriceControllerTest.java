package com.viettel.etc.controllers;
import com.viettel.etc.dto.DoctorPriceDTO;
import com.viettel.etc.services.DoctorPriceService;
import com.viettel.etc.utils.ErrorApp;
import com.viettel.etc.utils.FnCommon;
import com.viettel.etc.utils.TeleCareException;
import com.viettel.etc.xlibrary.core.constants.FunctionCommon;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import com.viettel.etc.controllers.DoctorPriceController;
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
class DoctorPriceControllerTest {

	private MockMvc mvc;
	@Mock
	private DoctorPriceService doctorPriceService;
	@InjectMocks 
	DoctorPriceController DoctorPriceController;

	@BeforeEach
	void setUp() {
		mvc = MockMvcBuilders.standaloneSetup(DoctorPriceController).build();
		JacksonTester.initFields(this, new ObjectMapper());
	}

	@Test
	void getDoctorPrices2() throws Exception {
		Authentication authentication = null;
		DoctorPriceDTO dataParams = new DoctorPriceDTO();
		Integer phaseId = 0;

		//mock method
		ResultSelectEntity resultData1 = new ResultSelectEntity();
		when(doctorPriceService.getDoctorPrices(Mockito.any(),Mockito.any())).thenReturn(resultData1);
		MockHttpServletResponse responseActual = mvc.perform(
			get("/prices/"+phaseId+"")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void getDoctorPrices2ThrowException3() throws Exception {
		Authentication authentication = null;
		DoctorPriceDTO dataParams = new DoctorPriceDTO();
		Integer phaseId = 0;

		//mock method
		when(doctorPriceService.getDoctorPrices(Mockito.any(),Mockito.any())).thenThrow(new TeleCareException(ErrorApp.ERROR_INPUTPARAMS));
		MockHttpServletResponse responseActual = mvc.perform(
			get("/prices/"+phaseId+"")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void getDoctorPrices2ThrowException4() throws Exception {
		Integer phaseId = 0;

		//mock method
		when(doctorPriceService.getDoctorPrices(Mockito.any(),Mockito.any())).thenThrow(new RuntimeException(ErrorApp.ERR_UNKNOW.getDescription()));
		MockHttpServletResponse responseActual = mvc.perform(
				get("/prices/"+phaseId+"")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void createDoctorPrices0() throws Exception {
		Authentication authentication = null;
		List dataParams = new ArrayList<>();
		Integer phaseId = 0;

		//mock method
		ResultSelectEntity resultData1 = new ResultSelectEntity();
		Mockito.doNothing().when(doctorPriceService).createDoctorPrices(Mockito.any(),Mockito.anyInt(),Mockito.any(), Mockito.any());
		MockHttpServletResponse responseActual = mvc.perform(
			post("/prices/"+phaseId+"")
			.accept(MediaType.APPLICATION_JSON)
			.content((new ObjectMapper()).writeValueAsString(dataParams))
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void createDoctorPrices0ThrowException3() throws Exception {
		Authentication authentication = null;
		List dataParams = new ArrayList<>();
		Integer phaseId = 0;

		//mock method
		Mockito.doThrow(new TeleCareException(ErrorApp.ERROR_INPUTPARAMS)).when(doctorPriceService).createDoctorPrices(Mockito.any(),Mockito.anyInt(),Mockito.any(), Mockito.any());
		MockHttpServletResponse responseActual = mvc.perform(
			post("/prices/"+phaseId+"")
			.accept(MediaType.APPLICATION_JSON)
			.content((new ObjectMapper()).writeValueAsString(dataParams))
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.BAD_REQUEST.value()));
	}

	@Test
	void getDoctorPricesByPhaseIdAndDoctorId3() throws Exception {
		Authentication authentication = null;
		DoctorPriceDTO dataParams = new DoctorPriceDTO();
		Integer phaseId = 0;
		Integer doctorId = 0;

		//mock method
		ResultSelectEntity resultData1 = new ResultSelectEntity();
		when(doctorPriceService.getDoctorPricesByPhaseIdAndDoctorId(Mockito.any(),Mockito.any())).thenReturn(resultData1);
		MockHttpServletResponse responseActual = mvc.perform(
			get("/"+doctorId+"/prices/"+phaseId+"")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void getDoctorPricesByPhaseIdAndDoctorId3ThrowException4() throws Exception {
		Authentication authentication = null;
		DoctorPriceDTO dataParams = new DoctorPriceDTO();
		Integer phaseId = 0;
		Integer doctorId = 0;

		//mock method
		when(doctorPriceService.getDoctorPricesByPhaseIdAndDoctorId(Mockito.any(),Mockito.any())).thenThrow(new TeleCareException(ErrorApp.ERROR_INPUTPARAMS));
		MockHttpServletResponse responseActual = mvc.perform(
			get("/"+doctorId+"/prices/"+phaseId+"")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void updateDoctorPrices1() throws Exception {
		Authentication authentication = null;
		DoctorPriceDTO dataParams = new DoctorPriceDTO();
		Integer phaseId = 0;
		Integer doctorId = 0;

		//mock method
		ResultSelectEntity resultData1 = new ResultSelectEntity();
		when(doctorPriceService.updateDoctorPrices(Mockito.any(),Mockito.any())).thenReturn(resultData1);
		MockHttpServletResponse responseActual = mvc.perform(
			post("/"+doctorId+"/prices/"+phaseId+"")
			.accept(MediaType.APPLICATION_JSON)
			.content((new ObjectMapper()).writeValueAsString(dataParams))
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void updateDoctorPrices1ThrowException4() throws Exception {
		Authentication authentication = null;
		DoctorPriceDTO dataParams = new DoctorPriceDTO();
		Integer phaseId = 0;
		Integer doctorId = 0;

		//mock method
		when(doctorPriceService.updateDoctorPrices(Mockito.any(),Mockito.any())).thenThrow(new TeleCareException(ErrorApp.ERROR_INPUTPARAMS));
		MockHttpServletResponse responseActual = mvc.perform(
			post("/"+doctorId+"/prices/"+phaseId+"")
			.accept(MediaType.APPLICATION_JSON)
			.content((new ObjectMapper()).writeValueAsString(dataParams))
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

}