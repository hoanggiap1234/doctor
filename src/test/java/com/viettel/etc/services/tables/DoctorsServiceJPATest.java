package com.viettel.etc.services.tables;

import com.viettel.etc.dto.RequestChangePasswordDTO;
import com.viettel.etc.repositories.tables.DoctorsRepositoryJPA;
import com.viettel.etc.services.KeycloakService;
import com.viettel.etc.services.impl.AuthencationTest;
import com.viettel.etc.utils.FnCommon;
import com.viettel.etc.utils.TeleCareException;
import mockit.MockUp;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import java.io.IOException;

/**
 * Comment
 */
class DoctorsServiceJPATest {
	@Mock
	DoctorsRepositoryJPA doctors;

	@Mock
	KeycloakService keycloakService;

	@InjectMocks
	DoctorsServiceJPA doctorsServiceJPA;

	@BeforeEach
	void setUp() {
		doctorsServiceJPA = new DoctorsServiceJPA();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void existByPhone() {
		String phone = "123456789";
		Mockito.when(doctors.existsByPhoneNumber(phone)).thenReturn(true);
		doctorsServiceJPA.existByPhone(phone);
	}

	@Test
	void changePassword() throws TeleCareException, IOException {
		RequestChangePasswordDTO requestChangePasswordDTO = new RequestChangePasswordDTO();
		requestChangePasswordDTO.setNewPassword("vtsTele#!kjk20202");
		requestChangePasswordDTO.setOldPassword("vtsTele#!kjk2020");

		Authentication authentication = new AuthencationTest();

		new MockUp<FnCommon>() {
			@mockit.Mock
			public String getUserIdLogin(Authentication authentication) {
				return "keycloakUserId";
			}
		};

		String phoneNumber = "09736101111";

		Mockito.when(doctors.getPhoneNumberByKeycloakId(Mockito.anyString())).thenReturn(phoneNumber);

		String token = "aaaaaa";
		Mockito.when(keycloakService.loginDoctor(phoneNumber, requestChangePasswordDTO.getOldPassword())).thenReturn(token);

		Mockito.doNothing().when(keycloakService).resetPassword(Mockito.any(), Mockito.any(), Mockito.any());

		doctorsServiceJPA.changePassword(requestChangePasswordDTO, authentication);
	}

	@Test
	void changePasswordKeyclockfalse() throws TeleCareException, IOException {
		RequestChangePasswordDTO requestChangePasswordDTO = new RequestChangePasswordDTO();
		requestChangePasswordDTO.setNewPassword("vtsTele#!kjk20202");
		requestChangePasswordDTO.setOldPassword("vtsTele#!kjk2020");

		Authentication authentication = new AuthencationTest();

		new MockUp<FnCommon>() {
			@mockit.Mock
			public String getUserIdLogin(Authentication authentication) {
				return "keycloakUserId";
			}
		};

		String phoneNumber = "09736101111";

		Mockito.when(doctors.getPhoneNumberByKeycloakId(Mockito.anyString())).thenReturn(phoneNumber);

		String token = "aaaaaa";
		Mockito.when(keycloakService.loginDoctor(phoneNumber, requestChangePasswordDTO.getOldPassword())).thenReturn(token);

		Mockito.doThrow(new TeleCareException("User id and newPassword not null")).when(keycloakService).resetPassword(Mockito.any(), Mockito.any(), Mockito.any());

		try {
			doctorsServiceJPA.changePassword(requestChangePasswordDTO, authentication);
		} catch (Exception e) {
			MatcherAssert.assertThat(e, Matchers.isA(TeleCareException.class));
		}
	}

	@Test
	void changeStatusActive() {
	}
}