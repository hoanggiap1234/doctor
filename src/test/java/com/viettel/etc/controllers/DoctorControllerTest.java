package com.viettel.etc.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.etc.dto.DoctorDTO;
import com.viettel.etc.dto.RequestChangePasswordDTO;
import com.viettel.etc.dto.RessetPasswordDTO;
import com.viettel.etc.services.DoctorService;
import com.viettel.etc.services.impl.AuthencationTest;
import com.viettel.etc.services.tables.DoctorsServiceJPA;
import com.viettel.etc.utils.*;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
import mockit.MockUp;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
class DoctorControllerTest {

	@InjectMocks
	DoctorController DoctorController;
	private MockMvc mvc;
	@Mock
	private DoctorService doctorService;
	@Mock
	private DoctorsServiceJPA doctorServiceJPA;

	@BeforeEach
	void setUp() {
		mvc = MockMvcBuilders.standaloneSetup(DoctorController).build();
		JacksonTester.initFields(this, new ObjectMapper());
	}

	@Test
	void createDoctor4() throws Exception {
		Authentication authentication = null;
		DoctorDTO dataParams = new DoctorDTO();
		// TODO sua cac tham so sau de khop voi dieu kien trong DTO
		dataParams.setFullname(new String("test"));
		dataParams.setDoctorType(0);
		dataParams.setProvinceCode(new String("test"));
		dataParams.setSpecialistIds(Arrays.asList(new Integer(1)));
		dataParams.setPhoneNumber(new String("0967824666"));
		dataParams.setHealthacilitieCodes(Arrays.asList(new String("Test")));

		//mock method
		ResultSelectEntity resultData1 = new ResultSelectEntity();
		when(doctorService.createDoctor(Mockito.any(), Mockito.any())).thenReturn(resultData1);
		MockHttpServletResponse responseActual = mvc.perform(
				post("/")
						.accept(MediaType.APPLICATION_JSON)
						.content((new ObjectMapper()).writeValueAsString(dataParams))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void createDoctor4ThrowException2() throws Exception {
		Authentication authentication = null;
		DoctorDTO dataParams = new DoctorDTO();
		// TODO sua cac tham so sau de khop voi dieu kien trong DTO
		dataParams.setFullname(new String("test"));
		dataParams.setDoctorType(0);
		dataParams.setProvinceCode(new String("test"));
		dataParams.setSpecialistIds(Arrays.asList(new Integer(1)));
		dataParams.setPhoneNumber(new String("0967824666"));
		dataParams.setHealthacilitieCodes(Arrays.asList(new String("Test")));

		//mock method
		when(doctorService.createDoctor(Mockito.any(), Mockito.any())).thenThrow(new TeleCareException(ErrorApp.ERROR_INPUTPARAMS));
		MockHttpServletResponse responseActual = mvc.perform(
				post("/")
						.accept(MediaType.APPLICATION_JSON)
						.content((new ObjectMapper()).writeValueAsString(dataParams))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.BAD_REQUEST.value()));
	}

	@Test
	void deleteDoctor5() throws Exception {
		DoctorDTO dataParams = new DoctorDTO();
		int doctorId = 0;

		Authentication authentication = new AuthencationTest();
		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().adminTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}
		};

		//mock method
		Mockito.doNothing().when(doctorService).deleteDoctor(Mockito.any(), Mockito.any());
		MockHttpServletResponse responseActual = mvc.perform(
				delete("/" + doctorId + "")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void deleteDoctor5ThrowException3() throws Exception {
		DoctorDTO dataParams = new DoctorDTO();
		int doctorId = 0;

		Authentication authentication = new AuthencationTest();
		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().adminTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}
		};
		//mock method
		Mockito.doThrow(new TeleCareException(ErrorApp.ERROR_INPUTPARAMS)).when(doctorService).deleteDoctor(Mockito.any(), Mockito.any());
		MockHttpServletResponse responseActual = mvc.perform(
				delete("/" + doctorId + "")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void updateDoctor6() throws Exception {
		Authentication authentication = new AuthencationTest();
		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().adminTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}
		};
		Integer doctorId = 0;
		DoctorDTO dataParams = new DoctorDTO();

		//mock method
		ResultSelectEntity resultData1 = new ResultSelectEntity();
		when(doctorService.updateDoctor(Mockito.any(), Mockito.any())).thenReturn(resultData1);
		MockHttpServletResponse responseActual = mvc.perform(
				put("/" + doctorId + "")
						.accept(MediaType.APPLICATION_JSON)
						.content((new ObjectMapper()).writeValueAsString(dataParams))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void updateDoctor6ThrowException3() throws Exception {
		Integer doctorId = 0;
		DoctorDTO dataParams = new DoctorDTO();

		Authentication authentication = new AuthencationTest();
		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().adminTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}
		};

		//mock method
		when(doctorService.updateDoctor(Mockito.any(), Mockito.any())).thenThrow(new TeleCareException(ErrorApp.ERROR_INPUTPARAMS));
		MockHttpServletResponse responseActual = mvc.perform(
				put("/" + doctorId + "")
						.accept(MediaType.APPLICATION_JSON)
						.content((new ObjectMapper()).writeValueAsString(dataParams))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void getDoctors7() throws Exception {
		Authentication authentication = null;
		DoctorDTO dataParams = new DoctorDTO();

		//mock method
		ResultSelectEntity resultData1 = new ResultSelectEntity();
		when(doctorService.getDoctors(Mockito.any(), Mockito.any())).thenReturn(resultData1);
		MockHttpServletResponse responseActual = mvc.perform(
				get("/")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void getDoctors7ThrowException2() throws Exception {
		Authentication authentication = null;
		DoctorDTO dataParams = new DoctorDTO();

		//mock method
		when(doctorService.getDoctors(Mockito.any(), Mockito.any())).thenThrow(new TeleCareException(ErrorApp.ERROR_INPUTPARAMS));
		MockHttpServletResponse responseActual = mvc.perform(
				get("/")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void getDoctorsPublic1() throws Exception {
		DoctorDTO dataParams = new DoctorDTO();

		//mock method
		ResultSelectEntity resultData1 = new ResultSelectEntity();
		when(doctorService.getDoctorsPublic(Mockito.any())).thenReturn(resultData1);
		MockHttpServletResponse responseActual = mvc.perform(
				get("/public")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void getDoctorsPublic1ThrowException1() throws Exception {
		DoctorDTO dataParams = new DoctorDTO();

		//mock method
		when(doctorService.getDoctorsPublic(Mockito.any())).thenThrow(new TeleCareException(ErrorApp.ERROR_INPUTPARAMS));
		MockHttpServletResponse responseActual = mvc.perform(
				get("/public")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void getDoctorInfo3() throws Exception {
		Integer doctorId = 0;
		DoctorDTO dataParams = new DoctorDTO();

		//mock method
		ResultSelectEntity resultData1 = new ResultSelectEntity();
		when(doctorService.getDoctorInfo(Mockito.any(), Mockito.any())).thenReturn(resultData1);
		MockHttpServletResponse responseActual = mvc.perform(
				get("/" + doctorId + "")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void getDoctorInfo3ThrowException2() throws Exception {
		Integer doctorId = 0;
		DoctorDTO dataParams = new DoctorDTO();

		//mock method
		when(doctorService.getDoctorInfo(Mockito.any(), Mockito.any())).thenThrow(new TeleCareException(ErrorApp.ERROR_INPUTPARAMS));
		MockHttpServletResponse responseActual = mvc.perform(
				get("/" + doctorId + "")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.NOT_FOUND.value()));
	}

	@Test
	void getTopDoctors10() throws Exception {
		Authentication authentication = null;
		DoctorDTO dataParams = new DoctorDTO();

		//mock method
		ResultSelectEntity resultData1 = new ResultSelectEntity();
		when(doctorService.getTopDoctors(Mockito.any())).thenReturn(resultData1);
		MockHttpServletResponse responseActual = mvc.perform(
				get("/top-doctors")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void getTopDoctors10ThrowException2() throws Exception {
		Authentication authentication = null;
		DoctorDTO dataParams = new DoctorDTO();

		//mock method
		ResultSelectEntity resultData1 = new ResultSelectEntity();
		when(doctorService.getTopDoctors(Mockito.any())).thenReturn(resultData1);
		MockHttpServletResponse responseActual = mvc.perform(
				get("/top-doctors")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void getDoctorInfo2() throws Exception {
		Authentication authentication = null;
		DoctorDTO dataParams = new DoctorDTO();

		//mock method
		ResultSelectEntity resultData1 = new ResultSelectEntity();
		when(doctorService.getDoctorByKeycloakUserId(Mockito.any(), Mockito.any())).thenReturn(resultData1);
		MockHttpServletResponse responseActual = mvc.perform(
				get("/info")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void getDoctorInfo2ThrowException2() throws Exception {
		Authentication authentication = null;
		DoctorDTO dataParams = new DoctorDTO();

		//mock method
		when(doctorService.getDoctorByKeycloakUserId(Mockito.any(), Mockito.any())).thenThrow(new TeleCareException(ErrorApp.ERROR_INPUTPARAMS));
		MockHttpServletResponse responseActual = mvc.perform(
				get("/info")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.NOT_FOUND.value()));
	}

	@Test
	void exportBookingHistoriesByDoctor0() throws Exception {
		Authentication authentication = null;
		DoctorDTO dataParams = new DoctorDTO();

		//mock method
		ResultSelectEntity resultData1 = new ResultSelectEntity();
		when(doctorService.getDoctorsExcel(Mockito.any(), Mockito.any())).thenReturn(resultData1);
		MockHttpServletResponse responseActual = mvc.perform(
				post("/export-data/excel")
						.accept(MediaType.APPLICATION_JSON)
						.content((new ObjectMapper()).writeValueAsString(dataParams))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.NOT_FOUND.value()));
	}

	@Test
	void exportBookingHistoriesByDoctor0ThrowException2() throws Exception {
		Authentication authentication = null;
		DoctorDTO dataParams = new DoctorDTO();

		//mock method
		when(doctorService.getDoctorsExcel(Mockito.any(), Mockito.any())).thenThrow(new TeleCareException(ErrorApp.ERROR_INPUTPARAMS));
		MockHttpServletResponse responseActual = mvc.perform(
				post("/export-data/excel")
						.accept(MediaType.APPLICATION_JSON)
						.content((new ObjectMapper()).writeValueAsString(dataParams))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void resetPassword8() throws Exception {
		RessetPasswordDTO dataParams = new RessetPasswordDTO();
		// TODO sua cac tham so sau de khop voi dieu kien trong DTO
		dataParams.setPhoneNumber(new String("0967824666"));
		dataParams.setNewPassword(new String("Hoangbui123!@#"));
		dataParams.setOtp(new String("123456"));

		//mock method
		Mockito.doNothing().when(doctorService).ressetPassword(Mockito.any());
		MockHttpServletResponse responseActual = mvc.perform(
				put("/reset-password")
						.accept(MediaType.APPLICATION_JSON)
						.content((new ObjectMapper()).writeValueAsString(dataParams))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void resetPassword8ThrowException1() throws Exception {
		RessetPasswordDTO dataParams = new RessetPasswordDTO();
		// TODO sua cac tham so sau de khop voi dieu kien trong DTO
		dataParams.setPhoneNumber(new String("0967824666"));
		dataParams.setNewPassword(new String("Hoangbui123!@#"));
		dataParams.setOtp(new String("123456"));

		//mock method
		Mockito.doThrow(new TeleCareException(ErrorApp.ERROR_INPUTPARAMS)).when(doctorService).ressetPassword(Mockito.any());
		MockHttpServletResponse responseActual = mvc.perform(
				put("/reset-password")
						.accept(MediaType.APPLICATION_JSON)
						.content((new ObjectMapper()).writeValueAsString(dataParams))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void changePassword9() throws Exception {
		Authentication authentication = null;
		Integer doctorId = 0;
		RequestChangePasswordDTO dataParams = new RequestChangePasswordDTO();

		//mock method
		ResultSelectEntity resultData1 = new ResultSelectEntity();
		when(doctorServiceJPA.changePassword(Mockito.any(), Mockito.any())).thenReturn(resultData1);
		MockHttpServletResponse responseActual = mvc.perform(
				put("/" + doctorId + "/change-password")
						.accept(MediaType.APPLICATION_JSON)
						.content((new ObjectMapper()).writeValueAsString(dataParams))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void changePassword9ThrowException3() throws Exception {
		Authentication authentication = null;
		Integer doctorId = 0;
		RequestChangePasswordDTO dataParams = new RequestChangePasswordDTO();

		//mock method
		when(doctorServiceJPA.changePassword(Mockito.any(), Mockito.any())).thenThrow(new TeleCareException(ErrorApp.ERROR_INPUTPARAMS));
		MockHttpServletResponse responseActual = mvc.perform(
				put("/" + doctorId + "/change-password")
						.accept(MediaType.APPLICATION_JSON)
						.content((new ObjectMapper()).writeValueAsString(dataParams))
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.BAD_REQUEST.value()));
	}

	@Test
	void getDoctorHomepageWeb10() throws Exception {
		Authentication authentication = null;
		Optional lang = Optional.empty();

		//mock method
		ResultSelectEntity resultData1 = new ResultSelectEntity();
		when(doctorService.getDoctorHomepageWeb(Mockito.any(),Mockito.any())).thenReturn(resultData1);
		MockHttpServletResponse responseActual = mvc.perform(
				get("/homepage-web")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void getDoctorHomepageWeb10ThrowException2() throws Exception {
		Authentication authentication = null;
		Optional lang = Optional.empty();

		//mock method
		when(doctorService.getDoctorHomepageWeb(Mockito.any(),Mockito.any())).thenThrow(new TeleCareException(ErrorApp.ERROR_INPUTPARAMS));
		MockHttpServletResponse responseActual = mvc.perform(
				get("/homepage-web")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.BAD_REQUEST.value()));
	}
}