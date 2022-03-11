package com.viettel.etc.controllers;
import com.viettel.etc.dto.OtpDTO;
import com.viettel.etc.services.OtpService;
import com.viettel.etc.services.tables.OtpServiceJPA;
import com.viettel.etc.utils.FnCommon;
import com.viettel.etc.utils.TeleCareException;
import com.viettel.etc.xlibrary.core.constants.FunctionCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import com.viettel.etc.controllers.OTPController;
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
class OTPControllerTest {

	private MockMvc mvc;
	@Mock
	OtpService otpService;
	@Mock
	OtpServiceJPA otpServiceJPA;
	@InjectMocks 
	OTPController OTPController;

	@BeforeEach
	void setUp() {
		mvc = MockMvcBuilders.standaloneSetup(OTPController).build();
		JacksonTester.initFields(this, new ObjectMapper());
	}

	@Test
	void otpRegister2() throws Exception {
		String phone = new String("test");

		//mock method
		ResultSelectEntity resultData1 = new ResultSelectEntity();
		when(otpService.requestOtpRegister(Mockito.any(), Mockito.any())).thenReturn(resultData1);
		MockHttpServletResponse responseActual = mvc.perform(
			get("/otp/register")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void otpRegister2ThrowException1() throws Exception {
		//mock method
		when(otpService.requestOtpRegister(Mockito.any(), Mockito.any())).thenThrow(new TeleCareException(ErrorApp.ERROR_INPUTPARAMS));
		MockHttpServletResponse responseActual = mvc.perform(
			get("/otp/register")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.queryParam("phone", "0967824666"))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.BAD_REQUEST.value()));
	}

	@Test
	void otpValidate1() throws Exception {
		String phone = new String("test");

		//mock method
		ResultSelectEntity resultData1 = new ResultSelectEntity();
		when(otpService.requestOtpValidate(Mockito.any())).thenReturn(resultData1);
		MockHttpServletResponse responseActual = mvc.perform(
			get("/otp/validate")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void otpValidate1ThrowException1() throws Exception {
		String phone = new String("test");

		//mock method
		when(otpService.requestOtpValidate(Mockito.any())).thenThrow(new TeleCareException(ErrorApp.ERROR_INPUTPARAMS));
		MockHttpServletResponse responseActual = mvc.perform(
			get("/otp/validate")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.BAD_REQUEST.value()));
	}

	@Test
	void validateDoctorOtp0() throws Exception {
		String phoneNumber = new String("test");
		String otp = new String("test");

		//mock method
		OtpDTO resultData1 = null;
		when(otpServiceJPA.validateOtp(Mockito.any(),Mockito.any(), Mockito.any())).thenReturn(resultData1);
		MockHttpServletResponse responseActual = mvc.perform(
			get("/validate-otp")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

	@Test
	void validateDoctorOtp0ThrowException2() throws Exception {
		String phoneNumber = new String("test");
		String otp = new String("test");

		//mock method
		OtpDTO resultData1 = null;
		when(otpServiceJPA.validateOtp(Mockito.any(),Mockito.any(),Mockito.any())).thenReturn(resultData1);
		MockHttpServletResponse responseActual = mvc.perform(
			get("/validate-otp")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();

		// assert result
		assertThat(responseActual.getStatus(), Matchers.equalTo(HttpStatus.OK.value()));
	}

}