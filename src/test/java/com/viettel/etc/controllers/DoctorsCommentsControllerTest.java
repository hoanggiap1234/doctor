package com.viettel.etc.controllers;import com.viettel.etc.dto.DoctorsCommentsDTO;import com.viettel.etc.services.DoctorsCommentsService;import com.viettel.etc.xlibrary.core.constants.FunctionCommon;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.http.HttpStatus;import org.springframework.http.MediaType;import org.springframework.http.ResponseEntity;import org.springframework.web.bind.annotation.GetMapping;import org.springframework.web.bind.annotation.PathVariable;import org.springframework.web.bind.annotation.RestController;import com.viettel.etc.controllers.DoctorsCommentsController;
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
class DoctorsCommentsControllerTest {

	private MockMvc mvc;
	@Mock
	private DoctorsCommentsService doctorsCommentsService;
	@InjectMocks 	DoctorsCommentsController DoctorsCommentsController;	@BeforeEach
	void setUp() {
		mvc = MockMvcBuilders.standaloneSetup(DoctorsCommentsController).build();
		JacksonTester.initFields(this, new ObjectMapper());
	}

	@Test
	void getDoctorComments0() throws Exception {
		Integer doctorId = 0;
		DoctorsCommentsDTO dataParams = new DoctorsCommentsDTO();

		//mock method
		ResultSelectEntity resultData1 = new ResultSelectEntity();
		when(doctorsCommentsService.getDoctorComments(Mockito.any())).thenReturn(resultData1);
		MockHttpServletResponse responseActual = mvc.perform(
			get("/"+""+doctorId+"/comments")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void getDoctorComments0ThrowException2() throws Exception {
		Integer doctorId = 0;
		DoctorsCommentsDTO dataParams = new DoctorsCommentsDTO();

		//mock method
		ResultSelectEntity resultData1 = new ResultSelectEntity();
		when(doctorsCommentsService.getDoctorComments(Mockito.any())).thenReturn(resultData1);
		MockHttpServletResponse responseActual = mvc.perform(
			get("/"+""+doctorId+"/comments")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

}