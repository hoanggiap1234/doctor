package com.viettel.etc.controllers;
import com.viettel.etc.dto.BookingDaysConsultantDoctorsDTO;
import com.viettel.etc.services.BookingDaysConsultantDoctorsService;
import com.viettel.etc.xlibrary.core.constants.FunctionCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import com.viettel.etc.controllers.BookingDaysConsultantDoctorsController;
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

import java.util.Map;

@ExtendWith(MockitoExtension.class)
class BookingDaysConsultantDoctorsControllerTest {

	private MockMvc mvc;
	@Mock
	private BookingDaysConsultantDoctorsService bookingDaysConsultantDoctorsService;
	@InjectMocks 
	BookingDaysConsultantDoctorsController BookingDaysConsultantDoctorsController;

	@BeforeEach
	void setUp() {
		mvc = MockMvcBuilders.standaloneSetup(BookingDaysConsultantDoctorsController).build();
		JacksonTester.initFields(this, new ObjectMapper());
	}

	@Test
	void getBookingDaysConsultantDoctors0() throws Exception {
		Authentication authentication = null;
		BookingDaysConsultantDoctorsDTO dataParams = new BookingDaysConsultantDoctorsDTO();
		// TODO sua cac tham so sau de khop voi dieu kien trong DTO
		dataParams.setHealthfacilitiesCode(new String("test"));

		//mock method
		ResultSelectEntity resultData1 = null;
		when(bookingDaysConsultantDoctorsService.getBookingDaysConsultantDoctors(Mockito.any())).thenReturn(resultData1);
		MockHttpServletResponse responseActual = mvc.perform(
			get("/"+"booking-days-consultant-doctors")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.queryParam("healthfacilitiesCode", "test"))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void getBookingDaysConsultantDoctors0ThrowException2() throws Exception {
		Authentication authentication = null;
		BookingDaysConsultantDoctorsDTO dataParams = new BookingDaysConsultantDoctorsDTO();
		// TODO sua cac tham so sau de khop voi dieu kien trong DTO
		dataParams.setHealthfacilitiesCode(new String("test"));

		//mock method
		ResultSelectEntity resultData1 = null;
		when(bookingDaysConsultantDoctorsService.getBookingDaysConsultantDoctors(Mockito.any())).thenReturn(resultData1);
		MockHttpServletResponse responseActual = mvc.perform(
			get("/"+"booking-days-consultant-doctors")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.queryParam("healthfacilitiesCode", "test"))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

}