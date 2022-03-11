package com.viettel.etc.controllers;
import com.viettel.etc.dto.WorkingSchedulesDTO;
import com.viettel.etc.dto.WorkingSchedulesRequestCreateDTO;
import com.viettel.etc.services.WorkingSchedulesService;
import com.viettel.etc.services.tables.DoctorsCalendarsServiceJPA;
import com.viettel.etc.utils.FnCommon;
import com.viettel.etc.utils.TeleCareException;
import com.viettel.etc.xlibrary.core.constants.FunctionCommon;
import com.viettel.etc.xlibrary.core.entities.CoreErrorApp;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import com.viettel.etc.controllers.WorkingSchedulesController;
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
class WorkingSchedulesControllerTest {

	private MockMvc mvc;
	@Mock
	private WorkingSchedulesService workingSchedulesService;
	@Mock
	private DoctorsCalendarsServiceJPA doctorsCalendarsServiceJPA;
	@InjectMocks 
	WorkingSchedulesController WorkingSchedulesController;

	@BeforeEach
	void setUp() {
		mvc = MockMvcBuilders.standaloneSetup(WorkingSchedulesController).build();
		JacksonTester.initFields(this, new ObjectMapper());
	}

	@Test
	void getWorkingSchedules3() throws Exception {
		Authentication authentication = null;
		WorkingSchedulesDTO dataParams = new WorkingSchedulesDTO();

		//mock method
		ResultSelectEntity resultData1 = null;
		when(workingSchedulesService.getWorkingSchedules(Mockito.any(),Mockito.any())).thenReturn(resultData1);
		MockHttpServletResponse responseActual = mvc.perform(
			get("/"+"working-schedules")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void getWorkingSchedules3ThrowException2() throws Exception {
		Authentication authentication = null;
		WorkingSchedulesDTO dataParams = new WorkingSchedulesDTO();

		//mock method
		when(workingSchedulesService.getWorkingSchedules(Mockito.any(),Mockito.any())).thenThrow(new TeleCareException(ErrorApp.ERROR_INPUTPARAMS));
		MockHttpServletResponse responseActual = mvc.perform(
			get("/"+"working-schedules")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void createWorkingSchedules0() throws Exception {
		Authentication authentication = null;
		WorkingSchedulesRequestCreateDTO dataParams = new WorkingSchedulesRequestCreateDTO();
		// TODO sua cac tham so sau de khop voi dieu kien trong DTO

		//mock method
		ResultSelectEntity resultData1 = new ResultSelectEntity();
		when(doctorsCalendarsServiceJPA.save(Mockito.any(),Mockito.any())).thenReturn(resultData1);
		MockHttpServletResponse responseActual = mvc.perform(
			post("/"+"working-schedules")
			.accept(MediaType.APPLICATION_JSON)
			.content((new ObjectMapper()).writeValueAsString(dataParams))
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void createWorkingSchedules0ThrowException2() throws Exception {
		Authentication authentication = null;
		WorkingSchedulesRequestCreateDTO dataParams = new WorkingSchedulesRequestCreateDTO();
		// TODO sua cac tham so sau de khop voi dieu kien trong DTO

		//mock method
		when(doctorsCalendarsServiceJPA.save(Mockito.any(),Mockito.any())).thenThrow(new TeleCareException(ErrorApp.ERROR_INPUTPARAMS));
		MockHttpServletResponse responseActual = mvc.perform(
			post("/"+"working-schedules")
			.accept(MediaType.APPLICATION_JSON)
			.content((new ObjectMapper()).writeValueAsString(dataParams))
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.BAD_REQUEST.value()));
	}

	@Test
	void getDetailWorkingSchedules4() throws Exception {
		Authentication authentication = null;
		WorkingSchedulesDTO dataParams = new WorkingSchedulesDTO();
		Integer doctorId = 0;

		//mock method
		ResultSelectEntity resultData1 = new ResultSelectEntity();
		when(workingSchedulesService.getWorkingSchedules(Mockito.any(),Mockito.any())).thenReturn(resultData1);
		MockHttpServletResponse responseActual = mvc.perform(
			get("/"+""+doctorId+"/detail-working-schedule")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void getDetailWorkingSchedules4ThrowException3() throws Exception {
		Authentication authentication = null;
		WorkingSchedulesDTO dataParams = new WorkingSchedulesDTO();
		Integer doctorId = 0;

		//mock method
		when(workingSchedulesService.getWorkingSchedules(Mockito.any(),Mockito.any())).thenThrow(new TeleCareException(ErrorApp.ERROR_INPUTPARAMS));
		MockHttpServletResponse responseActual = mvc.perform(
			get("/"+""+doctorId+"/detail-working-schedule")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void updateWorkingSchedules1() throws Exception {
		Authentication authentication = null;
		WorkingSchedulesDTO dataParams = new WorkingSchedulesDTO();
		// TODO sua cac tham so sau de khop voi dieu kien trong DTO
		Integer doctorId = 0;

		//mock method
		ResultSelectEntity resultData1 = new ResultSelectEntity();
		when(workingSchedulesService.updateWorkingSchedules(Mockito.any(),Mockito.any())).thenReturn(resultData1);
		MockHttpServletResponse responseActual = mvc.perform(
			post("/"+""+doctorId+"/detail-working-schedule")
			.accept(MediaType.APPLICATION_JSON)
			.content((new ObjectMapper()).writeValueAsString(dataParams))
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void updateWorkingSchedules1ThrowException3() throws Exception {
		Authentication authentication = null;
		WorkingSchedulesDTO dataParams = new WorkingSchedulesDTO();
		// TODO sua cac tham so sau de khop voi dieu kien trong DTO
		Integer doctorId = 0;

		//mock method
		when(workingSchedulesService.updateWorkingSchedules(Mockito.any(),Mockito.any())).thenThrow(new TeleCareException(ErrorApp.ERROR_INPUTPARAMS));
		MockHttpServletResponse responseActual = mvc.perform(
			post("/"+""+doctorId+"/detail-working-schedule")
			.accept(MediaType.APPLICATION_JSON)
			.content((new ObjectMapper()).writeValueAsString(dataParams))
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void deleteWorkingSchedules2() throws Exception {
		Authentication authentication = null;
		WorkingSchedulesDTO dataParams = new WorkingSchedulesDTO();
		// TODO sua cac tham so sau de khop voi dieu kien trong DTO

		//mock method
		Mockito.doNothing().when(workingSchedulesService).deleteWorkingSchedules(Mockito.any());
		MockHttpServletResponse responseActual = mvc.perform(
			post("/"+"delete-working-schedule")
			.accept(MediaType.APPLICATION_JSON)
			.content((new ObjectMapper()).writeValueAsString(dataParams))
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void deleteWorkingSchedules2ThrowException2() throws Exception {
		Authentication authentication = null;
		WorkingSchedulesDTO dataParams = new WorkingSchedulesDTO();
		// TODO sua cac tham so sau de khop voi dieu kien trong DTO

		//mock method
		Mockito.doThrow(new TeleCareException(ErrorApp.ERROR_INPUTPARAMS)).when(workingSchedulesService).deleteWorkingSchedules(Mockito.any());
		MockHttpServletResponse responseActual = mvc.perform(
			post("/"+"delete-working-schedule")
			.accept(MediaType.APPLICATION_JSON)
			.content((new ObjectMapper()).writeValueAsString(dataParams))
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

}