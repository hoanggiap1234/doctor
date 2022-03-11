package com.viettel.etc.services.impl;

import com.viettel.etc.dto.DoctorPriceDTO;
import com.viettel.etc.dto.MessageDTO;
import com.viettel.etc.repositories.DoctorPriceRepository;
import com.viettel.etc.repositories.tables.DoctorsPricesRepositoryJPA;
import com.viettel.etc.repositories.tables.entities.DoctorsPricesEntity;
import com.viettel.etc.services.DoctorPriceService;
import com.viettel.etc.services.MessageService;
import com.viettel.etc.utils.*;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
import mockit.MockUp;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Example;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class DoctorPriceServiceImplTest extends BaseTestService {

	@Mock
	private DoctorPriceRepository doctorPriceRepository;

	@Mock
	private DoctorsPricesRepositoryJPA doctorsPricesRepositoryJPA;

	@Mock
	private MessageService messageService;
	@InjectMocks
	private DoctorPriceService doctorPriceService;

	@BeforeEach
	void setUp() {
		doctorPriceService = new DoctorPriceServiceImpl();
		MockitoAnnotations.initMocks(this);
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void getDoctorPrices() throws TeleCareException {
		DoctorPriceDTO doctorPriceDTO = new DoctorPriceDTO();
		doctorPriceDTO.setPhaseId(1);
		doctorPriceDTO.setConsultantFeeVideocall(200.0);
		doctorPriceDTO.setConsultantFeeCall(200.0);
		doctorPriceDTO.setConsultantTimeCall(45);
		doctorPriceDTO.setConsultantTimeVideocall(45);
		List<DoctorPriceDTO> doctorPriceRepositoryResult = Arrays.asList(doctorPriceDTO);
		ResultSelectEntity resultSelectEntity = new ResultSelectEntity();
		resultSelectEntity.setListData(doctorPriceRepositoryResult);
		resultSelectEntity.setCount(doctorPriceRepositoryResult.size());

		TelecareUserEntity userLogin = createTestUser("Telecare_Admin");
		Authentication authentication = new AuthencationTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication1) {
				return userLogin;
			}
		};

		Mockito.when(doctorPriceRepository.getDoctorPrices(doctorPriceDTO, userLogin)).thenReturn(resultSelectEntity);
		ResultSelectEntity resultSelectEntityResult = (ResultSelectEntity) doctorPriceService.getDoctorPrices(doctorPriceDTO, authentication);
		MatcherAssert.assertThat(resultSelectEntityResult.getListData(), Matchers.notNullValue());
		MatcherAssert.assertThat(resultSelectEntityResult.getListData().size(), Matchers.is(1));
	}

	@Test
	void getDoctorPricesByPhaseIdAndDoctorId() throws TeleCareException {
		DoctorPriceDTO doctorPriceDTO = new DoctorPriceDTO();
		doctorPriceDTO.setPhaseId(1);
		doctorPriceDTO.setDoctorId(1);
		doctorPriceDTO.setConsultantFeeVideocall(200.0);
		doctorPriceDTO.setConsultantFeeCall(200.0);
		doctorPriceDTO.setConsultantTimeCall(45);
		doctorPriceDTO.setConsultantTimeVideocall(45);
		List<DoctorPriceDTO> doctorPriceRepositoryResult = Arrays.asList(doctorPriceDTO);
		ResultSelectEntity resultSelectEntity = new ResultSelectEntity();
		resultSelectEntity.setListData(doctorPriceRepositoryResult);
		resultSelectEntity.setCount(doctorPriceRepositoryResult.size());
		Mockito.when(doctorPriceRepository.getDoctorPricesByPhaseIdAndDoctorId(doctorPriceDTO)).thenReturn(resultSelectEntity);

		TelecareUserEntity userLogin = createTestUser("Telecare_Admin");
		Authentication authentication = new AuthencationTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication1) {
				return userLogin;
			}
		};

		ResultSelectEntity resultSelectEntityResult = (ResultSelectEntity) doctorPriceService.getDoctorPricesByPhaseIdAndDoctorId(doctorPriceDTO, authentication);
		MatcherAssert.assertThat(resultSelectEntityResult.getListData(), Matchers.notNullValue());
		MatcherAssert.assertThat(resultSelectEntityResult.getListData().size(), Matchers.is(1));
	}

	@Test
	void createDoctorPricesNormal() throws TeleCareException {
	/*	DoctorPriceDTO doctorPriceDTO = getDoctorPriceDTONormal();
		Mockito.when(doctorsPricesRepositoryJPA.save(new DoctorsPricesEntity())).thenReturn(new DoctorsPricesEntity());
		doctorPriceService.createDoctorPrices(doctorPriceDTO, null);*/
	}

	@Test
	void createDoctorPricesValidateNullMedicalExaminationFee() {
/*		DoctorPriceDTO doctorPriceDTO = getDoctorPriceDTONormal();
		doctorPriceDTO.setMedicalExaminationFee(null);
		Mockito.when(doctorsPricesRepositoryJPA.save(new DoctorsPricesEntity())).thenReturn(new DoctorsPricesEntity());
		try {
			doctorPriceService.createDoctorPrices(doctorPriceDTO, null);
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp().getCode(), Matchers.is(ErrorApp.ERROR_INPUTPARAMS.getCode()));
		}*/
	}

	@Test
	void updateDoctorPricesNormal() throws TeleCareException {
		Authentication authentication = new AuthencationTest();

		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().adminTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}
		};

		DoctorPriceDTO doctorPriceDTO = getDoctorPriceDTONormal();
		doctorPriceDTO.setMedicalExaminationFee(1);

		List<DoctorsPricesEntity> recordsWillUpdate = new ArrayList<>();
		recordsWillUpdate.add(new DoctorsPricesEntity());

		DoctorsPricesEntity example = new DoctorsPricesEntity();
		example.setPhaseId(doctorPriceDTO.getPhaseId());
		example.setDoctorId(doctorPriceDTO.getDoctorId());
		Mockito.when(doctorsPricesRepositoryJPA.findAll(Example.of(example))).thenReturn(recordsWillUpdate);
		Mockito.when(doctorsPricesRepositoryJPA.save(new DoctorsPricesEntity())).thenReturn(new DoctorsPricesEntity());
		doctorPriceService.updateDoctorPrices(doctorPriceDTO, null);
	}

	@Test
	void updateDoctorPricesNormalDelete() throws TeleCareException {
		Authentication authentication = new AuthencationTest();

		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().adminTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}
		};

		DoctorPriceDTO doctorPriceDTO = getDoctorPriceDTONormal();

		List<DoctorsPricesEntity> recordsWillUpdate = new ArrayList<>();
		recordsWillUpdate.add(new DoctorsPricesEntity());

		DoctorsPricesEntity example = new DoctorsPricesEntity();
		example.setPhaseId(doctorPriceDTO.getPhaseId());
		example.setDoctorId(doctorPriceDTO.getDoctorId());
		Mockito.when(doctorsPricesRepositoryJPA.findAll(Example.of(example))).thenReturn(recordsWillUpdate);
		Mockito.when(doctorsPricesRepositoryJPA.save(new DoctorsPricesEntity())).thenReturn(new DoctorsPricesEntity());
		doctorPriceService.updateDoctorPrices(doctorPriceDTO, null);
	}

	@Test
	void updateDoctorPricesValidateNullMedicalExaminationFee() {
		Authentication authentication = new AuthencationTest();

		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().adminTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}
		};

		DoctorPriceDTO doctorPriceDTO = getDoctorPriceDTONormal();
		doctorPriceDTO.setMedicalExaminationFee(null);

		List<DoctorsPricesEntity> recordsWillUpdate = new ArrayList<>();
		recordsWillUpdate.add(new DoctorsPricesEntity());

		DoctorsPricesEntity example = new DoctorsPricesEntity();
		example.setPhaseId(doctorPriceDTO.getPhaseId());
		example.setDoctorId(doctorPriceDTO.getDoctorId());
		Mockito.when(doctorsPricesRepositoryJPA.findAll(Example.of(example))).thenReturn(recordsWillUpdate);
		Mockito.when(doctorsPricesRepositoryJPA.save(new DoctorsPricesEntity())).thenReturn(new DoctorsPricesEntity());

		TelecareUserEntity userLogin = createTestUser("Telecare_Admin");
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication1) {
				return userLogin;
			}
		};
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERROR_INPUTPARAMS, doctorPriceDTO.getLanguage())).thenReturn(messageDTO);
		try {
			doctorPriceService.updateDoctorPrices(doctorPriceDTO, null);
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp().getCode(), Matchers.is(ErrorApp.ERROR_INPUTPARAMS.getCode()));
		}
	}

	@Test
	void updateDoctorPricesValidateRecordNotExistWithPhaseIdAndDoctorId() {
		Authentication authentication = new AuthencationTest();

		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().adminTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}
		};

		DoctorPriceDTO doctorPriceDTO = getDoctorPriceDTONormal();

		List<DoctorsPricesEntity> recordsWillUpdate = new ArrayList<>();
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERR_DATA_DOCTOR_NOT_EXIST, doctorPriceDTO.getLanguage())).thenReturn(messageDTO);
		DoctorsPricesEntity example = new DoctorsPricesEntity();
		example.setPhaseId(doctorPriceDTO.getPhaseId());
		example.setDoctorId(doctorPriceDTO.getDoctorId());
		Mockito.when(doctorsPricesRepositoryJPA.findAll(Example.of(example))).thenReturn(recordsWillUpdate);
		Mockito.when(doctorsPricesRepositoryJPA.save(new DoctorsPricesEntity())).thenReturn(new DoctorsPricesEntity());
		try {
			doctorPriceService.updateDoctorPrices(doctorPriceDTO, null);
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp().getCode(), Matchers.is(ErrorApp.ERR_DATA_DOCTOR_NOT_EXIST.getCode()));
		}
	}

	private DoctorPriceDTO getDoctorPriceDTONormal() {
		DoctorPriceDTO doctorPriceDTO = new DoctorPriceDTO();
		doctorPriceDTO.setPhaseId(1);
		doctorPriceDTO.setDoctorId(1);
		doctorPriceDTO.setMedicalExaminationFee(0);
		doctorPriceDTO.setMedicalExaminationNumber(0);
		doctorPriceDTO.setConsultantFeeVideocall(0.0);
		doctorPriceDTO.setConsultantTimeVideocall(0);
		doctorPriceDTO.setConsultantFeeCall(0.0);
		doctorPriceDTO.setConsultantTimeCall(0);
		doctorPriceDTO.setConsultantChatNumber(0);
		doctorPriceDTO.setConsultantFeeChat(0.0);
		return doctorPriceDTO;
	}

}
