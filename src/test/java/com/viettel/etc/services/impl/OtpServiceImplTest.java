package com.viettel.etc.services.impl;

import com.viettel.etc.dto.MessageDTO;
import com.viettel.etc.repositories.tables.entities.OtpEntity;
import com.viettel.etc.repositories.tables.entities.OtpIdentify;
import com.viettel.etc.services.MessageService;
import com.viettel.etc.services.OtpService;
import com.viettel.etc.services.tables.DoctorsServiceJPA;
import com.viettel.etc.services.tables.OtpServiceJPA;
import com.viettel.etc.utils.Constants;
import com.viettel.etc.utils.ErrorApp;
import com.viettel.etc.utils.SendSMS;
import com.viettel.etc.utils.TeleCareException;
import mockit.MockUp;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class OtpServiceImplTest {

	@Mock
	private OtpServiceJPA otpServiceJPA;

	@Mock
	private DoctorsServiceJPA doctorsServiceJPA;

	@Mock
	private MessageService messageService;
	@InjectMocks
	private OtpService otpService;

	@BeforeEach
	void setUp() {
		otpService = new OtpServiceImpl();
		MockitoAnnotations.initMocks(this);
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void requestOtpRegister_normal() throws TeleCareException {
		Mockito.when(doctorsServiceJPA.existByPhone(Mockito.any(String.class))).thenReturn(false);
		Mockito.when(otpServiceJPA.existsById(Mockito.any(OtpIdentify.class))).thenReturn(false);
		Mockito.when(otpServiceJPA.save(Mockito.any(OtpEntity.class))).thenReturn(null);
		new MockUp<SendSMS>() {
			@mockit.Mock
			public void sendAsyncDev(String phone, String content) {
				return;
			}
		};
		String language = "vi";
		otpService.requestOtpRegister("012345678", language);
	}

	@Test
	void requestOtpRegister_existByPhone() {
		Mockito.when(doctorsServiceJPA.existByPhone(Mockito.any(String.class))).thenReturn(true);
		String language = "vi";
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERROR_DATA_PHONE_NUMBER_EXIST, language)).thenReturn(messageDTO);
		try {
			otpService.requestOtpRegister("012345678", language);
			Assertions.fail();
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERROR_DATA_PHONE_NUMBER_EXIST));
		}
	}

	@Test
	void requestOtpRegister_existsById() throws TeleCareException {
		Mockito.when(doctorsServiceJPA.existByPhone(Mockito.any(String.class))).thenReturn(false);

		Mockito.when(otpServiceJPA.existsById(Mockito.any(OtpIdentify.class))).thenReturn(true);
		Mockito.when(otpServiceJPA.getById(Mockito.any(OtpIdentify.class))).thenReturn(new OtpEntity());
		Mockito.doNothing().when(otpServiceJPA).delete(Mockito.any(OtpEntity.class));

		Mockito.when(otpServiceJPA.save(Mockito.any(OtpEntity.class))).thenReturn(null);

		Mockito.when(otpServiceJPA.save(Mockito.any(OtpEntity.class))).thenReturn(null);
		new MockUp<SendSMS>() {
			@mockit.Mock
			public void sendAsyncDev(String phone, String content) {
				return;
			}
		};

		otpService.requestOtpRegister("012345678", "vi");
	}

	@Test
	void requestOtpValidate_normal() throws TeleCareException {
		Mockito.when(otpServiceJPA.existsById(Mockito.any(OtpIdentify.class))).thenReturn(true);

		OtpEntity otpEntity = new OtpEntity();
		Mockito.when(otpServiceJPA.getById(Mockito.any())).thenReturn(otpEntity);
		Mockito.doNothing().when(otpServiceJPA).delete(otpEntity);

		new MockUp<SendSMS>() {
			@mockit.Mock
			public void sendAsyncDev(String phone, String content) {
				return;
			}
		};

		Mockito.when(otpServiceJPA.save(Mockito.any())).thenReturn(otpEntity);

		MatcherAssert.assertThat(otpService.requestOtpValidate("012345678"), Matchers.notNullValue());
	}

}
