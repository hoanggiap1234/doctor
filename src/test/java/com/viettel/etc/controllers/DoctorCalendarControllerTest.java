package com.viettel.etc.controllers;
import com.viettel.etc.dto.DoctorsCalendarsDTO;
import com.viettel.etc.services.tables.DoctorsCalendarsServiceJPA;
import com.viettel.etc.utils.ErrorApp;
import com.viettel.etc.utils.FnCommon;
import com.viettel.etc.utils.TeleCareException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import com.viettel.etc.controllers.DoctorCalendarController;
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

import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class DoctorCalendarControllerTest {

	private MockMvc mvc;
	@Mock
	DoctorsCalendarsServiceJPA doctorsCalendarsServiceJPA;
	@InjectMocks 
	DoctorCalendarController DoctorCalendarController;

	@BeforeEach
	void setUp() {
		mvc = MockMvcBuilders.standaloneSetup(DoctorCalendarController).build();
		JacksonTester.initFields(this, new ObjectMapper());
	}

	@Test
	void cancelWorkingScheduleMobile0() throws Exception {
		int calendarId = 0;
		Authentication authentication = null;

		//mock method
		Mockito.doNothing().when(doctorsCalendarsServiceJPA).cancelWorkingScheduleMobile(Mockito.any(),Mockito.anyInt(), Mockito.any());
		MockHttpServletResponse responseActual = mvc.perform(
			put("/"+"cancel-working-schedules-mobile/"+calendarId+"")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void cancelWorkingScheduleMobile0ThrowException2() throws Exception {
		int calendarId = 0;
		Authentication authentication = null;

		//mock method
		Mockito.doThrow(new TeleCareException(ErrorApp.ERROR_INPUTPARAMS)).when(doctorsCalendarsServiceJPA).cancelWorkingScheduleMobile(Mockito.any(),Mockito.anyInt(), Mockito.any());
		MockHttpServletResponse responseActual = mvc.perform(
			put("/"+"cancel-working-schedules-mobile/"+calendarId+"")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void confirmWorkingSchedules2() throws Exception {
//		@RequestParam Long fromDate, @RequestParam Long toDate, @RequestParam String doctorIds

		//mock method
		Mockito.doNothing().when(doctorsCalendarsServiceJPA).confirmWorkingSchedules(Mockito.any(),Mockito.any());
		MockHttpServletResponse responseActual = mvc.perform(
			put("/"+"confirm-working-schedules")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.queryParam("fromDate", "1607940427000")
			.queryParam("toDate", "1607940427000")
			.queryParam("doctorIds", "1,2"))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void confirmWorkingSchedules2ThrowException5() throws Exception {

		//mock method
		Mockito.doThrow(new TeleCareException(ErrorApp.ERROR_INPUTPARAMS)).when(doctorsCalendarsServiceJPA).confirmWorkingSchedules(Mockito.any(),Mockito.any());
		MockHttpServletResponse responseActual = mvc.perform(
			put("/"+"confirm-working-schedules")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.queryParam("fromDate", "1607940427000")
			.queryParam("toDate", "1607940427000")
			.queryParam("doctorIds", "1,2"))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void deleteWorkingScheduleMobile1() throws Exception {
		int calendarId = 0;
		Authentication authentication = null;

		//mock method
		Mockito.doNothing().when(doctorsCalendarsServiceJPA).deleteWorkingScheduleMobile(Mockito.any(),Mockito.anyInt(), Mockito.any());
		MockHttpServletResponse responseActual = mvc.perform(
			delete("/"+"delete-working-schedules-mobile/"+calendarId+"")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void deleteWorkingScheduleMobile1ThrowException2() throws Exception {
		int calendarId = 0;
		Authentication authentication = null;

		//mock method
		Mockito.doThrow(new TeleCareException(ErrorApp.ERROR_INPUTPARAMS)).when(doctorsCalendarsServiceJPA).deleteWorkingScheduleMobile(Mockito.any(),Mockito.anyInt(), Mockito.any());
		MockHttpServletResponse responseActual = mvc.perform(
			delete("/"+"delete-working-schedules-mobile/"+calendarId+"")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

}