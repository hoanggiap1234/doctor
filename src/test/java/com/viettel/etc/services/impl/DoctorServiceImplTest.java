package com.viettel.etc.services.impl;

import com.squareup.okhttp.Response;
import com.viettel.etc.dto.*;
import com.viettel.etc.kafka.service.KafkaService;
import com.viettel.etc.repositories.CastWardRepository;
import com.viettel.etc.repositories.DoctorRepository;
import com.viettel.etc.repositories.tables.*;
import com.viettel.etc.repositories.tables.entities.*;
import com.viettel.etc.services.BookingInformationService;
import com.viettel.etc.services.KeycloakService;
import com.viettel.etc.services.MessageService;
import com.viettel.etc.services.VideoCallService;
import com.viettel.etc.services.tables.OtpServiceJPA;
import com.viettel.etc.services.tables.SysUsersServiceJPA;
import com.viettel.etc.utils.*;
import com.viettel.etc.xlibrary.core.entities.ExcellSheet;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;

class DoctorServiceImplTest {

	@Mock
	VideoCallService videoCallService;
	@Mock
	private CastWardRepository castWardRepository;
	@Mock
	private DoctorsRepositoryJPA doctorsRepositoryJPA;
	@InjectMocks
	private DoctorServiceImpl doctorService;
	@Mock
	private DoctorsHealthfacilitiesRepositoryJPA doctorsHealthfacilitiesRepositoryJPA;
	@Mock
	private DoctorsSpecialistsRepositoryJPA doctorsSpecialistsRepositoryJPA;
	@Mock
	private CatsHealthfacilitiesRepositoryJPA healthfacilitiesRepositoryJPA;
	@Mock
	private CatsSpecialistsRepositoryJPA specialistsRepositoryJPA;
	@Mock
	private KeycloakService keycloakService;

	@Mock
	private DoctorRepository doctorRepository;

	@Mock
	private BookingInformationService bookingInformationService;

	@Mock
	private OtpServiceJPA otpServiceJPA;

	@Mock
	private SysUsersServiceJPA sysUserServiceJPA;
	@Mock
	private SysUsersRepositoryJPA sysUsersRepositoryJPA;
	@Mock
	private KafkaService startupListener;
	@Mock
	private MessageService messageService;
	@Mock
	private DoctorsCommentsRepositoryJPA doctorsCommentsRepositoryJPA;
	@Mock
	private CatsHealthfacilitiesRepositoryJPA catsHealthfacilitiesRepositoryJPA;
	@BeforeEach
	void setUp() {
		doctorService = new DoctorServiceImpl();
		MockitoAnnotations.initMocks(this);
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void updateDoctor() {
	}

	@Test
	void createDoctor_normal_01() throws TeleCareException, IOException {
		Authentication authentication = new AuthencationTest();
		doctorService = Mockito.spy(new DoctorServiceImpl() {
			@Override
			public void checkPermisisonUpdateDoctor(Authentication authentication, List<String> healthacilitieCodes, String language) {
				return;
			}
		});
		MockitoAnnotations.initMocks(this);
		DoctorDTO doctorDTO = createDoctorDto();

		DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon.convertObjectToObject(doctorDTO, DoctorsEntity.class);
		doctorsEntity.setDoctorId(1);
		doctorsEntity.setDoctorCode(
				FnCommon.nameToNameCode(doctorsEntity.getFullname()) + doctorsEntity.getPhoneNumber());
		Mockito.when(otpServiceJPA.existsById(Mockito.any(OtpIdentify.class))).thenReturn(true);
		OtpEntity otpEntity = new OtpEntity();
		otpEntity.setOtp(doctorDTO.getOtp());
		otpEntity.setSignDate(new Date());
		otpEntity.setDuration(1000);
		Mockito.when(otpServiceJPA.getById(Mockito.any(OtpIdentify.class))).thenReturn(otpEntity);

		new MockUp<FnCommon>() {
			@mockit.Mock
			public boolean checkBriefcaseValid(String fileName, byte[] file, Integer maxFileSizeMb) {
				return true;
			}

			@mockit.Mock
			public String uploadFile(String containerFolder, MultipartFile file) {
				return "";
			}
		};

		new MockUp<Base64Util>() {
			@mockit.Mock
			public MultipartFile base64ToMultipart(String base64) {
				return new MultipartFileTest();
			}
		};

		Mockito.when(castWardRepository.getAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(createWardDto());

		Mockito.when(doctorsRepositoryJPA.existsByPhoneNumber(doctorDTO.getPhoneNumber())).thenReturn(false);

		Mockito.when(doctorsRepositoryJPA.existsByEmail(doctorDTO.getEmail())).thenReturn(false);

		Mockito.when(healthfacilitiesRepositoryJPA.countByHealthfacilitiesCodeIn(doctorDTO.getHealthacilitieCodes()))
				.thenReturn(doctorDTO.getHealthacilitieCodes().size());

		Mockito.when(specialistsRepositoryJPA.countBySpecialistIdIn(doctorDTO.getSpecialistIds()))
				.thenReturn(doctorDTO.getSpecialistIds().size());

		DoctorsEntity doctorsEntitySaved = new DoctorsEntity();
		doctorsEntity.setIsDelete(false);
		doctorsEntity.setDoctorId(null);
		doctorsEntity.setAvatar("");
		doctorsEntitySaved.setDoctorId(1);

		Mockito.when(doctorsRepositoryJPA.save(doctorsEntity)).thenReturn(doctorsEntitySaved);
		Mockito.when(doctorsRepositoryJPA.save(Mockito.any())).thenReturn(new DoctorsEntity());
		Mockito.when(keycloakService.createUserInKeyCloak(Mockito.any(DoctorDTO.class)))
				.thenReturn("keyClockUser");
		doctorsEntitySaved.setKeycloakUserId("keyClockUser");

		DoctorsEntity doctorsEntityUpdate = new DoctorsEntity();
		doctorsEntityUpdate.setKeycloakUserId("keyClockUser");
		doctorsEntityUpdate.setDoctorId(doctorsEntitySaved.getDoctorId());
		Mockito.when(doctorsRepositoryJPA.save(doctorsEntitySaved)).thenReturn(doctorsEntityUpdate);
		// send to sys user create to account service
		SysUsersEntity sysUsersEntity = new SysUsersEntity();
		sysUsersEntity.setKeycloakUserId(doctorsEntityUpdate.getKeycloakUserId());
		sysUsersEntity.setId(doctorsEntityUpdate.getDoctorId());
		sysUsersEntity.setType(Constants.DOCTOR_TYPE);

		Mockito.when(doctorsSpecialistsRepositoryJPA.saveAll(Mockito.any(List.class))).thenReturn(new ArrayList<>());
		Mockito.when(doctorsHealthfacilitiesRepositoryJPA.saveAll(Mockito.any(List.class))).thenReturn(new ArrayList<>());
		Mockito.doNothing().when(videoCallService).registerDevices("0987364758");
		DoctorDTO result = (DoctorDTO) doctorService.createDoctor(doctorDTO, authentication);


		MatcherAssert.assertThat(result, Matchers.notNullValue());
	}

	@Test
	void createDoctor_phoneNumber_invalid() {
		Authentication authentication = new AuthencationTest();
		doctorService = Mockito.spy(new DoctorServiceImpl() {
			@Override
			public void checkPermisisonUpdateDoctor(Authentication authentication, List<String> healthacilitieCodes, String language) {
				return;
			}
		});
		MockitoAnnotations.initMocks(this);
		DoctorDTO doctorDTO = createDoctorDto();
		doctorDTO.setPhoneNumber("abcdef");
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERR_PHONE_NUMBER_INVALID, createDoctorDto().getLanguage())).thenReturn(messageDTO);
		try {
			doctorService.createDoctor(doctorDTO, authentication);
			Assertions.fail();
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERR_PHONE_NUMBER_INVALID));
		} catch (IOException e) {
			Assertions.fail();
		}
	}

	@Test
	void createDoctor_email_invalid() {
		Authentication authentication = new AuthencationTest();
		doctorService = Mockito.spy(new DoctorServiceImpl() {
			@Override
			public void checkPermisisonUpdateDoctor(Authentication authentication, List<String> healthacilitieCodes, String language) {
				return;
			}
		});
		MockitoAnnotations.initMocks(this);
		DoctorDTO doctorDTO = createDoctorDto();
		doctorDTO.setEmail("abcdef");
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERR_EMAIL_INVALID, createDoctorDto().getLanguage())).thenReturn(messageDTO);
		try {
			doctorService.createDoctor(doctorDTO, authentication);
			Assertions.fail();
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERR_EMAIL_INVALID));
		} catch (IOException e) {
			Assertions.fail();
		}
	}
// remove otp create doctor

//    @Test
//    void createDoctor_not_existsById_otpIdentify() {
//        DoctorDTO doctorDTO = createDoctorDto();
//
//        DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon.convertObjectToObject(doctorDTO, DoctorsEntity.class);
//        doctorsEntity.setDoctorId(1);
//
//        Mockito.when(otpServiceJPA.existsById(Mockito.any(OtpIdentify.class))).thenReturn(false);
//
//        try {
//            doctorService.createDoctor(doctorDTO);
//            Assertions.fail();
//        } catch (TeleCareException e) {
//            MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERROR_DATA_OTP_EXIST));
//        } catch (IOException e) {
//            Assertions.fail();
//        }
//    }
//
//    @Test
//    void createDoctor_not_equal_otp() {
//
//        DoctorDTO doctorDTO = createDoctorDto();
//
//        DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon.convertObjectToObject(doctorDTO, DoctorsEntity.class);
//        doctorsEntity.setDoctorId(1);
//
//        Mockito.when(otpServiceJPA.existsById(Mockito.any(OtpIdentify.class))).thenReturn(true);
//        OtpEntity otpEntity = new OtpEntity();
//        otpEntity.setOtp("abc");
//        otpEntity.setSignDate(new Date());
//        otpEntity.setDuration(1000);
//        Mockito.when(otpServiceJPA.getById(Mockito.any(OtpIdentify.class))).thenReturn(otpEntity);
//
//        try {
//            doctorService.createDoctor(doctorDTO);
//            Assertions.fail();
//        } catch (TeleCareException e) {
//            MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERROR_DATA_OTP_INVALID));
//        } catch (IOException e) {
//            Assertions.fail();
//        }
//    }
//
//    @Test
//    void createDoctor_otp_expired() {
//
//        DoctorDTO doctorDTO = createDoctorDto();
//
//        DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon.convertObjectToObject(doctorDTO, DoctorsEntity.class);
//        doctorsEntity.setDoctorId(1);
//
//        Mockito.when(otpServiceJPA.existsById(Mockito.any(OtpIdentify.class))).thenReturn(true);
//        OtpEntity otpEntity = new OtpEntity();
//        otpEntity.setOtp(doctorDTO.getOtp());
//        otpEntity.setSignDate(Date.from(LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
//        otpEntity.setDuration(0);
//        Mockito.when(otpServiceJPA.getById(Mockito.any(OtpIdentify.class))).thenReturn(otpEntity);
//
//        try {
//            doctorService.createDoctor(doctorDTO);
//            Assertions.fail();
//        } catch (TeleCareException e) {
//            MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERROR_DATA_OTP_EXPIRED));
//        } catch (IOException e) {
//            Assertions.fail();
//        }
//    }

	@Test
	void createDoctor_checkBriefcase_inValid() throws TeleCareException {
		Authentication authentication = new AuthencationTest();
		doctorService = Mockito.spy(new DoctorServiceImpl() {
			@Override
			public void checkPermisisonUpdateDoctor(Authentication authentication, List<String> healthacilitieCodes, String language) {
				return;
			}
		});
		MockitoAnnotations.initMocks(this);
		DoctorDTO doctorDTO = createDoctorDto();

		DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon.convertObjectToObject(doctorDTO, DoctorsEntity.class);
		doctorsEntity.setDoctorId(1);

		Mockito.when(otpServiceJPA.existsById(Mockito.any(OtpIdentify.class))).thenReturn(true);
		OtpEntity otpEntity = new OtpEntity();
		otpEntity.setOtp(doctorDTO.getOtp());
		otpEntity.setSignDate(new Date());
		otpEntity.setDuration(1000);
		Mockito.when(otpServiceJPA.getById(Mockito.any(OtpIdentify.class))).thenReturn(otpEntity);
		Mockito.doNothing().when(videoCallService).registerDevices("0987364758");

		new MockUp<FnCommon>() {
			@mockit.Mock
			public boolean checkBriefcaseValid(String fileName, byte[] file, Integer maxFileSizeMb) {
				return false;
			}

			@mockit.Mock
			public String uploadFile(String containerFolder, MultipartFile file) {
				return "";
			}
		};

		new MockUp<Base64Util>() {
			@mockit.Mock
			public MultipartFile base64ToMultipart(String base64) {
				return new MultipartFileTest();
			}
		};

		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERR_IMAGE_FILE_INVALID, createDoctorDto().getLanguage())).thenReturn(messageDTO);
		try {
			doctorService.createDoctor(doctorDTO, authentication);
			Assertions.fail();
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERR_IMAGE_FILE_INVALID));
		} catch (IOException e) {
			Assertions.fail();
		}
	}

	@Test
	void createDoctor_fall_uploadFile() throws TeleCareException {
		Authentication authentication = new AuthencationTest();
		doctorService = Mockito.spy(new DoctorServiceImpl() {
			@Override
			public void checkPermisisonUpdateDoctor(Authentication authentication, List<String> healthacilitieCodes, String language) {
				return;
			}
		});
		MockitoAnnotations.initMocks(this);
		DoctorDTO doctorDTO = createDoctorDto();

		DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon.convertObjectToObject(doctorDTO, DoctorsEntity.class);
		doctorsEntity.setDoctorId(1);

		Mockito.when(otpServiceJPA.existsById(Mockito.any(OtpIdentify.class))).thenReturn(true);
		OtpEntity otpEntity = new OtpEntity();
		otpEntity.setOtp(doctorDTO.getOtp());
		otpEntity.setSignDate(new Date());
		otpEntity.setDuration(1000);
		Mockito.when(otpServiceJPA.getById(Mockito.any(OtpIdentify.class))).thenReturn(otpEntity);
		Mockito.doNothing().when(videoCallService).registerDevices("0987364758");

		new MockUp<FnCommon>() {
			@mockit.Mock
			public boolean checkBriefcaseValid(String fileName, byte[] file, Integer maxFileSizeMb) {
				return true;
			}

			@mockit.Mock
			public String uploadFile(String containerFolder, MultipartFile file) {
				return null;
			}
		};

		new MockUp<Base64Util>() {
			@mockit.Mock
			public MultipartFile base64ToMultipart(String base64) {
				return new MultipartFileTest();
			}
		};
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERR_IMAGE_FILE_INVALID, createDoctorDto().getLanguage())).thenReturn(messageDTO);
		try {
			doctorService.createDoctor(doctorDTO, authentication);
			Assertions.fail();
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERR_IMAGE_FILE_INVALID));
		} catch (IOException e) {
			Assertions.fail();
		}
	}

	@Test
	void createDoctor_null_CastWardDto() throws TeleCareException {
		Authentication authentication = new AuthencationTest();
		doctorService = Mockito.spy(new DoctorServiceImpl() {
			@Override
			public void checkPermisisonUpdateDoctor(Authentication authentication, List<String> healthacilitieCodes, String language) {
				return;
			}
		});
		MockitoAnnotations.initMocks(this);
		DoctorDTO doctorDTO = createDoctorDto();

		DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon.convertObjectToObject(doctorDTO, DoctorsEntity.class);
		doctorsEntity.setDoctorId(1);

		Mockito.when(otpServiceJPA.existsById(Mockito.any(OtpIdentify.class))).thenReturn(true);
		OtpEntity otpEntity = new OtpEntity();
		otpEntity.setOtp(doctorDTO.getOtp());
		otpEntity.setSignDate(new Date());
		otpEntity.setDuration(1000);
		Mockito.when(otpServiceJPA.getById(Mockito.any(OtpIdentify.class))).thenReturn(otpEntity);
		Mockito.doNothing().when(videoCallService).registerDevices("0987364758");

		new MockUp<FnCommon>() {
			@mockit.Mock
			public boolean checkBriefcaseValid(String fileName, byte[] file, Integer maxFileSizeMb) {
				return true;
			}

			@mockit.Mock
			public String uploadFile(String containerFolder, MultipartFile file) {
				return "";
			}
		};

		new MockUp<Base64Util>() {
			@mockit.Mock
			public MultipartFile base64ToMultipart(String base64) {
				return new MultipartFileTest();
			}
		};
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERROR_DATA_LOCATION_INVALID, doctorDTO.getLanguage())).thenReturn(messageDTO);
		Mockito.when(castWardRepository.getAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(null);
		try {
			doctorService.createDoctor(doctorDTO, authentication);
			Assertions.fail();
		} catch (TeleCareException e) {
			// TODO err message
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERROR_DATA_LOCATION_INVALID));
		} catch (IOException e) {
			Assertions.fail();
		}
	}

	@Test
	void createDoctor_existsByPhoneNumber() throws TeleCareException {
		Authentication authentication = new AuthencationTest();
		doctorService = Mockito.spy(new DoctorServiceImpl() {
			@Override
			public void checkPermisisonUpdateDoctor(Authentication authentication, List<String> healthacilitieCodes, String language) {
				return;
			}
		});
		MockitoAnnotations.initMocks(this);
		DoctorDTO doctorDTO = createDoctorDto();

		DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon.convertObjectToObject(doctorDTO, DoctorsEntity.class);
		doctorsEntity.setDoctorId(1);

		Mockito.when(otpServiceJPA.existsById(Mockito.any(OtpIdentify.class))).thenReturn(true);
		OtpEntity otpEntity = new OtpEntity();
		otpEntity.setOtp(doctorDTO.getOtp());
		otpEntity.setSignDate(new Date());
		otpEntity.setDuration(1000);
		Mockito.when(otpServiceJPA.getById(Mockito.any(OtpIdentify.class))).thenReturn(otpEntity);
		Mockito.doNothing().when(videoCallService).registerDevices("0987364758");

		new MockUp<FnCommon>() {
			@mockit.Mock
			public boolean checkBriefcaseValid(String fileName, byte[] file, Integer maxFileSizeMb) {
				return true;
			}

			@mockit.Mock
			public String uploadFile(String containerFolder, MultipartFile file) {
				return "";
			}
		};

		new MockUp<Base64Util>() {
			@mockit.Mock
			public MultipartFile base64ToMultipart(String base64) {
				return new MultipartFileTest();
			}
		};

		Mockito.when(castWardRepository.getAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(createWardDto());

		Mockito.when(doctorsRepositoryJPA.existsByPhoneNumber(doctorDTO.getPhoneNumber())).thenReturn(true);
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERROR_DATA_PHONE_NUMBER_EXIST, createDoctorDto().getLanguage())).thenReturn(messageDTO);
		try {
			doctorService.createDoctor(doctorDTO, authentication);
			Assertions.fail();
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERROR_DATA_PHONE_NUMBER_EXIST));
		} catch (IOException e) {
			Assertions.fail();
		}
	}

	@Test
	void createDoctor_existsByEmail() throws TeleCareException {
		Authentication authentication = new AuthencationTest();
		doctorService = Mockito.spy(new DoctorServiceImpl() {
			@Override
			public void checkPermisisonUpdateDoctor(Authentication authentication, List<String> healthacilitieCodes, String language) {
				return;
			}
		});
		MockitoAnnotations.initMocks(this);
		DoctorDTO doctorDTO = createDoctorDto();

		DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon.convertObjectToObject(doctorDTO, DoctorsEntity.class);
		doctorsEntity.setDoctorId(1);

		Mockito.when(otpServiceJPA.existsById(Mockito.any(OtpIdentify.class))).thenReturn(true);
		OtpEntity otpEntity = new OtpEntity();
		otpEntity.setOtp(doctorDTO.getOtp());
		otpEntity.setSignDate(new Date());
		otpEntity.setDuration(1000);
		Mockito.when(otpServiceJPA.getById(Mockito.any(OtpIdentify.class))).thenReturn(otpEntity);
		Mockito.doNothing().when(videoCallService).registerDevices("0987364758");

		new MockUp<FnCommon>() {
			@mockit.Mock
			public boolean checkBriefcaseValid(String fileName, byte[] file, Integer maxFileSizeMb) {
				return true;
			}

			@mockit.Mock
			public String uploadFile(String containerFolder, MultipartFile file) {
				return "";
			}
		};

		new MockUp<Base64Util>() {
			@mockit.Mock
			public MultipartFile base64ToMultipart(String base64) {
				return new MultipartFileTest();
			}
		};

		Mockito.when(castWardRepository.getAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(createWardDto());

		Mockito.when(doctorsRepositoryJPA.existsByPhoneNumber(doctorDTO.getPhoneNumber())).thenReturn(false);

		Mockito.when(doctorsRepositoryJPA.existsByEmail(doctorDTO.getEmail())).thenReturn(true);
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERROR_DATA_EMAIL_EXIST, createDoctorDto().getLanguage())).thenReturn(messageDTO);
		try {
			doctorService.createDoctor(doctorDTO, authentication);
			Assertions.fail();
		} catch (TeleCareException e) {
			// TODO message
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERROR_DATA_EMAIL_EXIST));
		} catch (IOException e) {
			Assertions.fail();
		}
	}

	@Test
	void createDoctor_existsByHealthacilitieCodes() throws TeleCareException {
		Authentication authentication = new AuthencationTest();
		doctorService = Mockito.spy(new DoctorServiceImpl() {
			@Override
			public void checkPermisisonUpdateDoctor(Authentication authentication, List<String> healthacilitieCodes, String language) {
				return;
			}
		});
		MockitoAnnotations.initMocks(this);
		DoctorDTO doctorDTO = createDoctorDto();

		DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon.convertObjectToObject(doctorDTO, DoctorsEntity.class);
		doctorsEntity.setDoctorId(1);

		Mockito.when(otpServiceJPA.existsById(Mockito.any(OtpIdentify.class))).thenReturn(true);
		OtpEntity otpEntity = new OtpEntity();
		otpEntity.setOtp(doctorDTO.getOtp());
		otpEntity.setSignDate(new Date());
		otpEntity.setDuration(1000);
		Mockito.when(otpServiceJPA.getById(Mockito.any(OtpIdentify.class))).thenReturn(otpEntity);
		Mockito.doNothing().when(videoCallService).registerDevices("0987364758");

		new MockUp<FnCommon>() {
			@mockit.Mock
			public boolean checkBriefcaseValid(String fileName, byte[] file, Integer maxFileSizeMb) {
				return true;
			}

			@mockit.Mock
			public String uploadFile(String containerFolder, MultipartFile file) {
				return "";
			}
		};

		new MockUp<Base64Util>() {
			@mockit.Mock
			public MultipartFile base64ToMultipart(String base64) {
				return new MultipartFileTest();
			}
		};

		Mockito.when(castWardRepository.getAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(createWardDto());

		Mockito.when(doctorsRepositoryJPA.existsByPhoneNumber(doctorDTO.getPhoneNumber())).thenReturn(false);

		Mockito.when(doctorsRepositoryJPA.existsByEmail(doctorDTO.getEmail())).thenReturn(false);

		Mockito.when(healthfacilitiesRepositoryJPA.countByHealthfacilitiesCodeIn(doctorDTO.getHealthacilitieCodes()))
				.thenReturn(doctorDTO.getHealthacilitieCodes().size() + 1);
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERR_HEALTHFACILITIE_NOT_FOUND, createDoctorDto().getLanguage())).thenReturn(messageDTO);
		try {
			doctorService.createDoctor(doctorDTO, authentication);
			Assertions.fail();
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERR_HEALTHFACILITIE_NOT_FOUND));
		} catch (IOException e) {
			Assertions.fail();
		}
	}

	@Test
	void createDoctor_existsBySpecialistId() throws TeleCareException {
		Authentication authentication = new AuthencationTest();
		doctorService = Mockito.spy(new DoctorServiceImpl() {
			@Override
			public void checkPermisisonUpdateDoctor(Authentication authentication, List<String> healthacilitieCodes, String language) {
				return;
			}
		});
		MockitoAnnotations.initMocks(this);
		DoctorDTO doctorDTO = createDoctorDto();

		DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon.convertObjectToObject(doctorDTO, DoctorsEntity.class);
		doctorsEntity.setDoctorId(1);

		Mockito.when(otpServiceJPA.existsById(Mockito.any(OtpIdentify.class))).thenReturn(true);
		OtpEntity otpEntity = new OtpEntity();
		otpEntity.setOtp(doctorDTO.getOtp());
		otpEntity.setSignDate(new Date());
		otpEntity.setDuration(1000);
		Mockito.when(otpServiceJPA.getById(Mockito.any(OtpIdentify.class))).thenReturn(otpEntity);

		new MockUp<FnCommon>() {
			@mockit.Mock
			public boolean checkBriefcaseValid(String fileName, byte[] file, Integer maxFileSizeMb) {
				return true;
			}

			@mockit.Mock
			public String uploadFile(String containerFolder, MultipartFile file) {
				return "";
			}
		};

		new MockUp<Base64Util>() {
			@mockit.Mock
			public MultipartFile base64ToMultipart(String base64) {
				return new MultipartFileTest();
			}
		};

		Mockito.when(castWardRepository.getAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(createWardDto());

		Mockito.when(doctorsRepositoryJPA.existsByPhoneNumber(doctorDTO.getPhoneNumber())).thenReturn(false);

		Mockito.when(doctorsRepositoryJPA.existsByEmail(doctorDTO.getEmail())).thenReturn(false);

		Mockito.when(healthfacilitiesRepositoryJPA.countByHealthfacilitiesCodeIn(doctorDTO.getHealthacilitieCodes()))
				.thenReturn(doctorDTO.getHealthacilitieCodes().size());

		Mockito.when(specialistsRepositoryJPA.countBySpecialistIdIn(doctorDTO.getSpecialistIds()))
				.thenReturn(doctorDTO.getSpecialistIds().size() + 1);
		Mockito.doNothing().when(videoCallService).registerDevices("0987364758");
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERR_SPECIAL_NOT_FOUND, createDoctorDto().getLanguage())).thenReturn(messageDTO);
		try {
			doctorService.createDoctor(doctorDTO, authentication);
			Assertions.fail();
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERR_SPECIAL_NOT_FOUND));
		} catch (IOException e) {
			Assertions.fail();
		}
	}

	@Test
	void createDoctor_existsByUserInKeyCloak() throws TeleCareException {
		Authentication authentication = new AuthencationTest();
		doctorService = Mockito.spy(new DoctorServiceImpl() {
			@Override
			public void checkPermisisonUpdateDoctor(Authentication authentication, List<String> healthacilitieCodes, String language) {
				return;
			}
		});
		MockitoAnnotations.initMocks(this);
		DoctorDTO doctorDTO = createDoctorDto();
		doctorDTO.setLanguage("vi");
		DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon.convertObjectToObject(doctorDTO, DoctorsEntity.class);
		doctorsEntity.setDoctorId(1);

		Mockito.when(otpServiceJPA.existsById(Mockito.any(OtpIdentify.class))).thenReturn(true);
		OtpEntity otpEntity = new OtpEntity();
		otpEntity.setOtp(doctorDTO.getOtp());
		otpEntity.setSignDate(new Date());
		otpEntity.setDuration(1000);
		Mockito.when(otpServiceJPA.getById(Mockito.any(OtpIdentify.class))).thenReturn(otpEntity);

		new MockUp<FnCommon>() {
			@mockit.Mock
			public boolean checkBriefcaseValid(String fileName, byte[] file, Integer maxFileSizeMb) {
				return true;
			}

			@mockit.Mock
			public String uploadFile(String containerFolder, MultipartFile file) {
				return "";
			}
		};

		new MockUp<Base64Util>() {
			@mockit.Mock
			public MultipartFile base64ToMultipart(String base64) {
				return new MultipartFileTest();
			}
		};

		Mockito.when(castWardRepository.getAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(createWardDto());

		Mockito.when(doctorsRepositoryJPA.existsByPhoneNumber(doctorDTO.getPhoneNumber())).thenReturn(false);

		Mockito.when(doctorsRepositoryJPA.existsByEmail(doctorDTO.getEmail())).thenReturn(false);

		Mockito.when(healthfacilitiesRepositoryJPA.countByHealthfacilitiesCodeIn(doctorDTO.getHealthacilitieCodes()))
				.thenReturn(doctorDTO.getHealthacilitieCodes().size());

		Mockito.when(specialistsRepositoryJPA.countBySpecialistIdIn(doctorDTO.getSpecialistIds()))
				.thenReturn(doctorDTO.getSpecialistIds().size());
		Mockito.doNothing().when(videoCallService).registerDevices("0987364758");
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setMessageCode("test");
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.GET_KEYCLOCK_FALSE, doctorDTO.getLanguage())).thenReturn(messageDTO);
		Mockito.when(keycloakService.createUserInKeyCloak(Mockito.any(DoctorDTO.class)))
					.thenThrow(new TeleCareException(""));
		Assertions.assertThrows(TeleCareException.class, () ->{
			//call service
			doctorService.createDoctor(doctorDTO, authentication);
		});

	}

	private CastWardDto createWardDto() {
		CastWardDto dto = new CastWardDto();
		dto.setWardCode("00001");
		dto.setWardName("Phường Phúc Xá");
		dto.setDistrictCode("001");
		dto.setDistrictName("Quận Ba Đình");
		dto.setProvinceCode("01");
		dto.setProvinceName("Thành phố Hà Nội");

		return dto;
	}

	private DoctorDTO createDoctorDto() {
		DoctorDTO dto = new DoctorDTO();

		dto.setFullname("name");
		dto.setDescription("name");
		dto.setSummary("name");
		dto.setAvatar("avatar");
		dto.setAcademicRankId(1);
		dto.setAcademicRankCode("wq");
		dto.setAcademicRankName("nawme");
		dto.setDegreeId(1);
		dto.setWardCode("00001");
		dto.setDistrictCode("001");
		dto.setProvinceCode("01");
		dto.setSpecialistIds(new ArrayList<>(Arrays.asList(1, 2, 3)));
		dto.setPhoneNumber("0976244755");
		dto.setEmail("nguyetnee3gbk87@gmail.com");
		dto.setHealthacilitieCodes(new ArrayList<>(Arrays.asList("01", "01000")));
		dto.setGenderId(1);
		dto.setIsMedicalexamination(true);
		dto.setIsConsultant(true);
		dto.setIsActive(true);
		dto.setOtp("123456");

		return dto;
	}

	@Test
	void getDoctorInfo_normal_01() throws TeleCareException {
		DoctorDTO inputParam = new DoctorDTO();
		inputParam.setDoctorId(1);

		DoctorDTO dataResultRepoMock = new DoctorDTO();
		Mockito.when(doctorRepository.getDoctorInfo(Mockito.any(DoctorDTO.class))).thenReturn(dataResultRepoMock);

		BookingInformationDTO bookingInformationDTO = new BookingInformationDTO();
		bookingInformationDTO.setBookingOrder(1);
		bookingInformationDTO.setBookingAdvisory(2);
		Mockito.when(bookingInformationService.countBookingByBookingGroup(Mockito.any(BookingInformationDTO.class))).thenReturn(bookingInformationDTO);

		DoctorDTO dataResult = (DoctorDTO) doctorService.getDoctorInfo(inputParam, inputParam.getDoctorId());

		MatcherAssert.assertThat(dataResult.getBookingOrder(), Matchers.is(bookingInformationDTO.getBookingOrder()));
		MatcherAssert.assertThat(dataResult.getBookingAdvisory(), Matchers.is(bookingInformationDTO.getBookingAdvisory()));
	}

	@Test
	void getDoctorInfo_doctor_not_exist() {
		DoctorDTO inputParam = new DoctorDTO();
		inputParam.setDoctorId(1);

		DoctorDTO dataResultRepoMock = null;
		Mockito.when(doctorRepository.getDoctorInfo(Mockito.any(DoctorDTO.class))).thenReturn(dataResultRepoMock);

		BookingInformationDTO bookingInformationDTO = new BookingInformationDTO();
		bookingInformationDTO.setBookingOrder(1);
		bookingInformationDTO.setBookingAdvisory(2);
		Mockito.when(bookingInformationService.countBookingByBookingGroup(Mockito.any(BookingInformationDTO.class))).thenReturn(bookingInformationDTO);
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERR_DATA_DOCTOR_NOT_EXIST, inputParam.getLanguage())).thenReturn(messageDTO);
		try {
			doctorService.getDoctorInfo(inputParam, inputParam.getDoctorId());
			Assertions.fail();
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERR_DATA_DOCTOR_NOT_EXIST));
		}
	}

	@Test
	void getDoctorByKeycloakUserId_normal_01() throws TeleCareException {
		DoctorDTO inputParam = new DoctorDTO();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public String getUserIdLogin(Authentication authentication) {
				return "";
			}
		};
		DoctorDTO doctorEntityMock = new DoctorDTO();
		doctorEntityMock.setDoctorId(1);
		doctorEntityMock.setHealthfacilitiesCode("01014");
		Mockito.when(doctorRepository.getDoctorByKeycloakUserId(Mockito.any(DoctorDTO.class))).thenReturn(doctorEntityMock);
		List<String> healthFacilityCodes = new ArrayList<>(Arrays.asList(doctorEntityMock.getHealthfacilitiesCode().split(",")));
		Mockito.when(catsHealthfacilitiesRepositoryJPA.findByHealthfacilitiesCodeInAndIsActiveAndIsDelete(healthFacilityCodes, Constants.IS_ACTIVE, Constants.IS_NOT_DELETE)).thenReturn(Arrays.asList(new CatsHealthfacilitiesEntity()));
		DoctorDTO result = (DoctorDTO) doctorService.getDoctorByKeycloakUserId(inputParam, Mockito.any(Authentication.class));
		MatcherAssert.assertThat(result.getDoctorId(), Matchers.is(doctorEntityMock.getDoctorId()));
	}

	@Test
	void getDoctorByKeycloakUserId_null_keycloakUserId() {
		DoctorDTO inputParam = new DoctorDTO();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public String getUserIdLogin(Authentication authentication) {
				return null;
			}
		};

		DoctorDTO doctorEntityMock = new DoctorDTO();
		doctorEntityMock.setDoctorId(1);
		Mockito.when(doctorRepository.getDoctorByKeycloakUserId(Mockito.any(DoctorDTO.class))).thenReturn(doctorEntityMock);
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERR_DATA_DOCTOR_NOT_EXIST, inputParam.getLanguage())).thenReturn(messageDTO);
		try {
			doctorService.getDoctorByKeycloakUserId(inputParam, null);
			Assertions.fail();
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERR_DATA_DOCTOR_NOT_EXIST));
		}
	}

	@Test
	void getDoctorByKeycloakUserId_null_getDoctorByKeycloakUserId() {
		DoctorDTO inputParam = new DoctorDTO();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public String getUserIdLogin(Authentication authentication) {
				return "";
			}
		};

		DoctorDTO doctorEntityMock = null;
		Mockito.when(doctorRepository.getDoctorByKeycloakUserId(Mockito.any(DoctorDTO.class))).thenReturn(doctorEntityMock);
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERR_DATA_DOCTOR_NOT_EXIST, inputParam.getLanguage())).thenReturn(messageDTO);
		try {
			doctorService.getDoctorByKeycloakUserId(inputParam, Mockito.any(Authentication.class));
			Assertions.fail();
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERR_DATA_DOCTOR_NOT_EXIST));
		}
	}

	@Test
	void getTopDoctors_normal_01() {
		Mockito.when(doctorRepository.getTopDoctors(Mockito.any(DoctorDTO.class))).thenReturn(null);
		doctorService.getTopDoctors(Mockito.any(DoctorDTO.class));
	}

	@Test
	void deleteDoctor_normal_01() {
		DoctorDTO inputParam = new DoctorDTO();
		inputParam.setDoctorId(1);
		Mockito.when(doctorsRepositoryJPA.getOne(inputParam.getDoctorId())).thenReturn(new DoctorsEntity());
		Mockito.doNothing().when(doctorsRepositoryJPA).deleteDoctorById(inputParam.getDoctorId());
		Mockito.doNothing().when(doctorsSpecialistsRepositoryJPA).deleteByDoctorId(inputParam.getDoctorId());
		Mockito.doNothing().when(doctorsHealthfacilitiesRepositoryJPA).deleteByDoctorId(inputParam.getDoctorId());
		// send to topic
		SysUsersEntity sysUserEntity = new SysUsersEntity();
		sysUserEntity.setId(inputParam.getDoctorId());
		sysUserEntity.setType(2);
		Mockito.when(sysUserServiceJPA.getByIdAndType(inputParam.getDoctorId(), Constants.DOCTOR_TYPE)).thenReturn(sysUserEntity);

		Authentication authentication = new AuthencationTest();
		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().adminTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}
		};
		try {
			doctorService.deleteDoctor(inputParam, authentication);
		} catch (TeleCareException | IOException e) {
			Assertions.fail();
		}
	}

	@Test
	void deleteDoctor_not_exits() throws IOException, TeleCareException {
		DoctorDTO inputParam = new DoctorDTO();
		inputParam.setDoctorId(1);
		Mockito.when(doctorsRepositoryJPA.getOne(inputParam.getDoctorId())).thenReturn(null);
		Authentication authentication = new AuthencationTest();
		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().adminTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}
		};
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERR_DATA_DOCTOR_NOT_EXIST, createDoctorDto().getLanguage())).thenReturn(messageDTO);
		try {
			doctorService.deleteDoctor(inputParam, authentication);
			Assertions.fail();
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERR_DATA_DOCTOR_NOT_EXIST));
		}
	}

	@Test
	void getDoctors() throws TeleCareException {
		TelecareUserEntity userSystemEntity = new TelecareUserEntityDataTest().adminTest();
		Authentication authentication = new AuthencationTest();
		DoctorDTO itemParamsEntity = new DoctorDTO();

		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return userSystemEntity;
			}
		};

		Mockito.when(doctorRepository.getDoctors(itemParamsEntity, userSystemEntity)).thenReturn(new ResultSelectEntity());
		MatcherAssert.assertThat(doctorService.getDoctors(itemParamsEntity, authentication), Matchers.isA(ResultSelectEntity.class));
	}

	@Test
	void getDoctorsException() throws TeleCareException {
		TelecareUserEntity userSystemEntity = new TelecareUserEntityDataTest().patientTest();
		Authentication authentication = new AuthencationTest();
		DoctorDTO itemParamsEntity = new DoctorDTO();

		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return userSystemEntity;
			}
		};
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setMessageCode("ERR_USER_PERMISSION");
		messageDTO.setCode(403);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERR_USER_PERMISSION, itemParamsEntity.getLanguage())).thenReturn(messageDTO);
		Mockito.when(doctorRepository.getDoctors(itemParamsEntity, userSystemEntity)).thenReturn(new ResultSelectEntity());

		Assertions.assertThrows(TeleCareException.class, () -> {
			doctorService.getDoctors(itemParamsEntity, authentication);
		});
	}

	@Test
	void getDoctorsExcel() throws TeleCareException {
		TelecareUserEntity userSystemEntity = new TelecareUserEntityDataTest().adminTest();
		Authentication authentication = new AuthencationTest();
		DoctorDTO itemParamsEntity = new DoctorDTO();

		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return userSystemEntity;
			}
		};

		Mockito.when(doctorRepository.getDoctorsExcel(itemParamsEntity)).thenReturn(new ResultSelectEntity());

		MatcherAssert.assertThat(doctorService.getDoctorsExcel(itemParamsEntity, authentication), Matchers.isA(ResultSelectEntity.class));
	}

	@Test
	void getDoctorsExcelException() throws TeleCareException {
		TelecareUserEntity userSystemEntity = new TelecareUserEntityDataTest().patientTest();
		Authentication authentication = new AuthencationTest();
		DoctorDTO itemParamsEntity = new DoctorDTO();

		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return userSystemEntity;
			}
		};

		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(403);
		messageDTO.setDescription("Khong co quyen truy cap");
		Mockito.when(messageService.getMessage(Constants.ERR_USER_PERMISSION, itemParamsEntity.getLanguage())).thenReturn(messageDTO);
		Mockito.when(doctorRepository.getDoctorsExcel(itemParamsEntity)).thenReturn(new ResultSelectEntity());

		Assertions.assertThrows(TeleCareException.class, () -> {
			doctorService.getDoctorsExcel(itemParamsEntity, authentication);
		});
	}

	@Test
	void getDoctorsPublic() throws TeleCareException {
		DoctorDTO itemParamsEntity = new DoctorDTO();

		Mockito.when(doctorRepository.getDoctors(itemParamsEntity, null)).thenReturn(new ResultSelectEntity());
		MatcherAssert.assertThat(doctorService.getDoctorsPublic(itemParamsEntity), Matchers.isA(ResultSelectEntity.class));
	}

	@Test
	void updateDoctor_normal_01() throws TeleCareException, IOException {
		DoctorDTO inputParam = createDoctorDto();
		inputParam.setDoctorId(1);

		DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon.convertObjectToObject(inputParam, DoctorsEntity.class);
		DoctorsEntity doctorsEntityMock = new DoctorsEntity();
		doctorsEntityMock.setDoctorId(1);
		doctorsEntityMock.setAvatar("avatar");
		doctorsEntityMock.setFullname("fullName");
		doctorsEntityMock.setPhoneNumber("012345678");
		Mockito.when(doctorsRepositoryJPA.findByDoctorId(inputParam.getDoctorId())).thenReturn(doctorsEntity);

		new MockUp<Base64Util>() {
			@mockit.Mock
			public MultipartFile base64ToMultipart(String base64) {
				return new MultipartFileTest();
			}

			@mockit.Mock
			public String toBase64(String filePath) {
				return inputParam.getAvatar();
			}

			@mockit.Mock
			public boolean isBase64String(String base64value) {
				return true;
			}
		};

		new MockUp<FnCommon>() {
			@mockit.Mock
			public boolean checkBriefcaseValid(String fileName, byte[] file, Integer maxFileSizeMb) {
				return true;
			}

			@mockit.Mock
			public String uploadFile(String containerFolder, MultipartFile file) {
				return "";
			}
		};

		CastWardDto castWardDtoMock = this.createWardDto();
		castWardDtoMock.setDistrictCode(inputParam.getDistrictCode());
		castWardDtoMock.setProvinceCode(inputParam.getProvinceCode());
		Mockito.when(castWardRepository.getAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(createWardDto());

		Mockito.when(doctorsRepositoryJPA.existsByPhoneNumberAndDoctorIdNot(inputParam.getPhoneNumber(), inputParam.getDoctorId())).thenReturn(false);

		Mockito.when(doctorsRepositoryJPA.existsByEmailAndDoctorIdNot(inputParam.getEmail(), inputParam.getDoctorId())).thenReturn(false);

		Mockito.when(healthfacilitiesRepositoryJPA.countByHealthfacilitiesCodeIn(inputParam.getHealthacilitieCodes()))
				.thenReturn(inputParam.getHealthacilitieCodes().size());

		Mockito.when(specialistsRepositoryJPA.countBySpecialistIdIn(inputParam.getSpecialistIds()))
				.thenReturn(inputParam.getSpecialistIds().size());

		Mockito.when(doctorsRepositoryJPA.save(doctorsEntity)).thenReturn(doctorsEntity);

		List<DoctorsSpecialistsEntity> doctorsSpecialists = new ArrayList<>();
		DoctorsSpecialistsEntity recordFirst = new DoctorsSpecialistsEntity();
		recordFirst.setSpecialistId(inputParam.getSpecialistIds().get(0));
		doctorsSpecialists.add(recordFirst);

		DoctorsSpecialistsEntity recordTwo = new DoctorsSpecialistsEntity();
		recordTwo.setSpecialistId(0);
		doctorsSpecialists.add(recordTwo);
		Mockito.when(doctorsSpecialistsRepositoryJPA.findByDoctorIdAndIsDelete(inputParam.getDoctorId(), false))
				.thenReturn(doctorsSpecialists);

		Mockito.when(doctorsSpecialistsRepositoryJPA.saveAll(Mockito.any(List.class))).thenReturn(new ArrayList<>());

		List<DoctorsHealthfacilitiesEntity> doctorsHealthfacilities = new ArrayList<>();
		DoctorsHealthfacilitiesEntity record_1 = new DoctorsHealthfacilitiesEntity();
		record_1.setHealthfacilitiesCode(inputParam.getHealthacilitieCodes().get(0));
		doctorsHealthfacilities.add(record_1);

		DoctorsHealthfacilitiesEntity record_2 = new DoctorsHealthfacilitiesEntity();
		record_2.setHealthfacilitiesCode("00");
		doctorsHealthfacilities.add(record_2);

		Mockito.when(doctorsHealthfacilitiesRepositoryJPA.findByDoctorIdAndIsDelete(inputParam.getDoctorId(), false)).thenReturn(doctorsHealthfacilities);

		Mockito.when(doctorsHealthfacilitiesRepositoryJPA.saveAll(Mockito.any(List.class))).thenReturn(new ArrayList<>());

		Authentication authentication = new AuthencationTest();
		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().adminTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}
		};

		DoctorDTO result = (DoctorDTO) doctorService.updateDoctor(inputParam, authentication);
		MatcherAssert.assertThat(result, Matchers.notNullValue());
	}

	@Test
	void updateDoctor_not_exits_doctors_by_doctor_id() {
		DoctorDTO inputParam = createDoctorDto();
		inputParam.setDoctorId(1);

		Mockito.when(doctorsRepositoryJPA.findByDoctorId(inputParam.getDoctorId())).thenReturn(null);

		Authentication authentication = new AuthencationTest();
		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().adminTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}
		};
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERR_DATA_DOCTOR_NOT_EXIST, createDoctorDto().getLanguage())).thenReturn(messageDTO);
		try {
			doctorService.updateDoctor(inputParam, authentication);
			Assertions.fail();
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERR_DATA_DOCTOR_NOT_EXIST));
		} catch (IOException e) {
			Assertions.fail();
		}
	}

	@Test
	void ressetPassword() throws TeleCareException {
		RessetPasswordDTO dto = new RessetPasswordDTO();
		dto.setPhoneNumber("0973610926");
		dto.setNewPassword("vtsTele#!kjk20202");
		dto.setOtp("123456");

		OtpIdentify otpId = new OtpIdentify();

		Mockito.when(otpServiceJPA.existsById(Mockito.any())).thenReturn(true);

		OtpEntity otpEntity = new OtpEntity();
		otpEntity.setOtp("123456");
		otpEntity.setSignDate(new Date());
		otpEntity.setDuration(2);

		Mockito.when(otpServiceJPA.getById(Mockito.any())).thenReturn(otpEntity);

		DoctorsEntity doctorsEntity = new DoctorsEntity();
		doctorsEntity.setKeycloakUserId("aaaaaaa");
		Mockito.when(doctorsRepositoryJPA.findByPhoneNumber(dto.getPhoneNumber())).thenReturn(doctorsEntity);

		Mockito.doNothing().when(keycloakService).resetPassword(Mockito.isA(String.class), Mockito.isA(String.class), Mockito.isA(String.class));

		doctorService.ressetPassword(dto);
	}

	@Test
	void ressetPasswordException() throws TeleCareException {
		RessetPasswordDTO dto = new RessetPasswordDTO();
		dto.setPhoneNumber("01111973610926");
		dto.setNewPassword("vtsTele#!kjk20202");
		dto.setOtp("123456");

		OtpIdentify otpId = new OtpIdentify();

		Mockito.when(otpServiceJPA.existsById(Mockito.any())).thenReturn(true);

		OtpEntity otpEntity = new OtpEntity();
		otpEntity.setOtp("123456");

		Mockito.when(otpServiceJPA.getById(Mockito.any())).thenReturn(otpEntity);

		DoctorsEntity doctorsEntity = new DoctorsEntity();
		doctorsEntity.setKeycloakUserId("aaaaaaa");
		Mockito.when(doctorsRepositoryJPA.findByPhoneNumber(dto.getPhoneNumber())).thenReturn(doctorsEntity);

		Mockito.doNothing().when(keycloakService).resetPassword(Mockito.isA(String.class), Mockito.isA(String.class), Mockito.isA(String.class));
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERR_PHONE_NUMBER_INVALID, createDoctorDto().getLanguage())).thenReturn(messageDTO);
		Assertions.assertThrows(TeleCareException.class, () -> {
			doctorService.ressetPassword(dto);
		});
	}

	@Test
	void updateDoctor_checkBriefcaseValid() {
		DoctorDTO inputParam = createDoctorDto();
		inputParam.setDoctorId(1);

		DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon.convertObjectToObject(inputParam, DoctorsEntity.class);
		DoctorsEntity doctorsEntityMock = new DoctorsEntity();
		doctorsEntityMock.setDoctorId(1);
		doctorsEntityMock.setAvatar("avatar");
		doctorsEntityMock.setFullname("fullName");
		doctorsEntityMock.setPhoneNumber("012345678");
		Mockito.when(doctorsRepositoryJPA.findByDoctorId(inputParam.getDoctorId())).thenReturn(doctorsEntity);

		new MockUp<Base64Util>() {
			@mockit.Mock
			public MultipartFile base64ToMultipart(String base64) {
				return new MultipartFileTest();
			}

			@mockit.Mock
			public String toBase64(String filePath) {
				return inputParam.getAvatar();
			}

			@mockit.Mock
			public boolean isBase64String(String base64value) {
				return true;
			}
		};

		new MockUp<FnCommon>() {
			@mockit.Mock
			public boolean checkBriefcaseValid(String fileName, byte[] file, Integer maxFileSizeMb) {
				return false;
			}

			@mockit.Mock
			public String uploadFile(String containerFolder, MultipartFile file) {
				return "";
			}
		};

		Authentication authentication = new AuthencationTest();
		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().adminTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}
		};
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERR_ADDRESS, inputParam.getLanguage())).thenReturn(messageDTO);
		try {
			doctorService.updateDoctor(inputParam, authentication);
			Assertions.fail();
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERR_ADDRESS));
		} catch (IOException e) {
			Assertions.fail();
		}
	}

	@Test
	void updateDoctor_err_uploadFile() {
		DoctorDTO inputParam = createDoctorDto();
		inputParam.setDoctorId(1);

		DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon.convertObjectToObject(inputParam, DoctorsEntity.class);
		DoctorsEntity doctorsEntityMock = new DoctorsEntity();
		doctorsEntityMock.setDoctorId(1);
		doctorsEntityMock.setAvatar("avatar");
		doctorsEntityMock.setFullname("fullName");
		doctorsEntityMock.setPhoneNumber("012345678");
		Mockito.when(doctorsRepositoryJPA.findByDoctorId(inputParam.getDoctorId())).thenReturn(doctorsEntity);

		new MockUp<Base64Util>() {
			@mockit.Mock
			public MultipartFile base64ToMultipart(String base64) {
				return new MultipartFileTest();
			}

			@mockit.Mock
			public String toBase64(String filePath) {
				return inputParam.getAvatar();
			}

			@mockit.Mock
			public boolean isBase64String(String base64value) {
				return true;
			}
		};

		new MockUp<FnCommon>() {
			@mockit.Mock
			public boolean checkBriefcaseValid(String fileName, byte[] file, Integer maxFileSizeMb) {
				return true;
			}

			@mockit.Mock
			public String uploadFile(String containerFolder, MultipartFile file) {
				return null;
			}
		};

		Authentication authentication = new AuthencationTest();
		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().adminTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}
		};
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERR_ADDRESS, inputParam.getLanguage())).thenReturn(messageDTO);
		try {
			doctorService.updateDoctor(inputParam, authentication);
			Assertions.fail();
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERR_ADDRESS));
		} catch (IOException e) {
			Assertions.fail();
		}
	}

	@Test
	void updateDoctor_null_avatar() throws TeleCareException, IOException {
		DoctorDTO inputParam = createDoctorDto();
		inputParam.setDoctorId(1);
		inputParam.setAvatar(null);

		DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon.convertObjectToObject(inputParam, DoctorsEntity.class);
		DoctorsEntity doctorsEntityMock = new DoctorsEntity();
		doctorsEntityMock.setDoctorId(1);
		doctorsEntityMock.setAvatar("avatar");
		doctorsEntityMock.setFullname("fullName");
		doctorsEntityMock.setPhoneNumber("012345678");
		Mockito.when(doctorsRepositoryJPA.findByDoctorId(inputParam.getDoctorId())).thenReturn(doctorsEntity);

		new MockUp<Base64Util>() {
			@mockit.Mock
			public MultipartFile base64ToMultipart(String base64) {
				return new MultipartFileTest();
			}

			@mockit.Mock
			public String toBase64(String filePath) {
				return inputParam.getAvatar();
			}

			@mockit.Mock
			public boolean isBase64String(String base64value) {
				return true;
			}
		};

		new MockUp<FnCommon>() {
			@mockit.Mock
			public boolean checkBriefcaseValid(String fileName, byte[] file, Integer maxFileSizeMb) {
				return true;
			}

			@mockit.Mock
			public String uploadFile(String containerFolder, MultipartFile file) {
				return "";
			}
		};

		CastWardDto castWardDtoMock = this.createWardDto();
		castWardDtoMock.setDistrictCode(inputParam.getDistrictCode());
		castWardDtoMock.setProvinceCode(inputParam.getProvinceCode());
		Mockito.when(castWardRepository.getAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(createWardDto());

		Mockito.when(doctorsRepositoryJPA.existsByPhoneNumberAndDoctorIdNot(inputParam.getPhoneNumber(), inputParam.getDoctorId())).thenReturn(false);

		Mockito.when(doctorsRepositoryJPA.existsByEmailAndDoctorIdNot(inputParam.getEmail(), inputParam.getDoctorId())).thenReturn(false);

		Mockito.when(healthfacilitiesRepositoryJPA.countByHealthfacilitiesCodeIn(inputParam.getHealthacilitieCodes()))
				.thenReturn(inputParam.getHealthacilitieCodes().size());

		Mockito.when(specialistsRepositoryJPA.countBySpecialistIdIn(inputParam.getSpecialistIds()))
				.thenReturn(inputParam.getSpecialistIds().size());

		Mockito.when(doctorsRepositoryJPA.save(doctorsEntity)).thenReturn(doctorsEntity);

		List<DoctorsSpecialistsEntity> doctorsSpecialists = new ArrayList<>();
		DoctorsSpecialistsEntity recordFirst = new DoctorsSpecialistsEntity();
		recordFirst.setSpecialistId(inputParam.getSpecialistIds().get(0));
		doctorsSpecialists.add(recordFirst);

		DoctorsSpecialistsEntity recordTwo = new DoctorsSpecialistsEntity();
		recordTwo.setSpecialistId(0);
		doctorsSpecialists.add(recordTwo);
		Mockito.when(doctorsSpecialistsRepositoryJPA.findByDoctorIdAndIsDelete(inputParam.getDoctorId(), false))
				.thenReturn(doctorsSpecialists);

		Mockito.when(doctorsSpecialistsRepositoryJPA.saveAll(Mockito.any(List.class))).thenReturn(new ArrayList<>());

		List<DoctorsHealthfacilitiesEntity> doctorsHealthfacilities = new ArrayList<>();
		DoctorsHealthfacilitiesEntity record_1 = new DoctorsHealthfacilitiesEntity();
		record_1.setHealthfacilitiesCode(inputParam.getHealthacilitieCodes().get(0));
		doctorsHealthfacilities.add(record_1);

		DoctorsHealthfacilitiesEntity record_2 = new DoctorsHealthfacilitiesEntity();
		record_2.setHealthfacilitiesCode("00");
		doctorsHealthfacilities.add(record_2);

		Mockito.when(doctorsHealthfacilitiesRepositoryJPA.findByDoctorIdAndIsDelete(inputParam.getDoctorId(), false)).thenReturn(doctorsHealthfacilities);

		Mockito.when(doctorsHealthfacilitiesRepositoryJPA.saveAll(Mockito.any(List.class))).thenReturn(new ArrayList<>());

		Authentication authentication = new AuthencationTest();
		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().adminTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}
		};
		DoctorDTO result = (DoctorDTO) doctorService.updateDoctor(inputParam, authentication);
		MatcherAssert.assertThat(result, Matchers.notNullValue());
	}

	@Test
	void updateDoctor_err_address() {
		DoctorDTO inputParam = createDoctorDto();
		inputParam.setDoctorId(1);

		DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon.convertObjectToObject(inputParam, DoctorsEntity.class);
		DoctorsEntity doctorsEntityMock = new DoctorsEntity();
		doctorsEntityMock.setDoctorId(1);
		doctorsEntityMock.setAvatar("avatar");
		doctorsEntityMock.setFullname("fullName");
		doctorsEntityMock.setPhoneNumber("012345678");
		Mockito.when(doctorsRepositoryJPA.findByDoctorId(inputParam.getDoctorId())).thenReturn(doctorsEntity);

		new MockUp<Base64Util>() {
			@mockit.Mock
			public MultipartFile base64ToMultipart(String base64) {
				return new MultipartFileTest();
			}

			@mockit.Mock
			public String toBase64(String filePath) {
				return inputParam.getAvatar();
			}

			@mockit.Mock
			public boolean isBase64String(String base64value) {
				return true;
			}
		};

		new MockUp<FnCommon>() {
			@mockit.Mock
			public boolean checkBriefcaseValid(String fileName, byte[] file, Integer maxFileSizeMb) {
				return true;
			}

			@mockit.Mock
			public String uploadFile(String containerFolder, MultipartFile file) {
				return "";
			}
		};

		CastWardDto castWardDtoMock = this.createWardDto();
		castWardDtoMock.setDistrictCode(inputParam.getDistrictCode());
		castWardDtoMock.setProvinceCode(inputParam.getProvinceCode() + "test");
		Mockito.when(castWardRepository.getAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(castWardDtoMock);

		Authentication authentication = new AuthencationTest();
		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().adminTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}
		};
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERR_ADDRESS, inputParam.getLanguage())).thenReturn(messageDTO);
		try {
			doctorService.updateDoctor(inputParam, authentication);
			Assertions.fail();
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERR_ADDRESS));
		} catch (IOException e) {
			Assertions.fail();
		}
	}

	@Test
	void updateDoctor_err_PhoneNumber() {
		DoctorDTO inputParam = createDoctorDto();
		inputParam.setDoctorId(1);

		DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon.convertObjectToObject(inputParam, DoctorsEntity.class);
		DoctorsEntity doctorsEntityMock = new DoctorsEntity();
		doctorsEntityMock.setDoctorId(1);
		doctorsEntityMock.setAvatar("avatar");
		doctorsEntityMock.setFullname("fullName");
		doctorsEntityMock.setPhoneNumber("012345678");
		Mockito.when(doctorsRepositoryJPA.findByDoctorId(inputParam.getDoctorId())).thenReturn(doctorsEntity);

		new MockUp<Base64Util>() {
			@mockit.Mock
			public MultipartFile base64ToMultipart(String base64) {
				return new MultipartFileTest();
			}

			@mockit.Mock
			public String toBase64(String filePath) {
				return inputParam.getAvatar();
			}

			@mockit.Mock
			public boolean isBase64String(String base64value) {
				return true;
			}
		};

		new MockUp<FnCommon>() {
			@mockit.Mock
			public boolean checkBriefcaseValid(String fileName, byte[] file, Integer maxFileSizeMb) {
				return true;
			}

			@mockit.Mock
			public String uploadFile(String containerFolder, MultipartFile file) {
				return "";
			}
		};

		CastWardDto castWardDtoMock = this.createWardDto();
		castWardDtoMock.setDistrictCode(inputParam.getDistrictCode());
		castWardDtoMock.setProvinceCode(inputParam.getProvinceCode());
		Mockito.when(castWardRepository.getAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(createWardDto());

		Mockito.when(doctorsRepositoryJPA.existsByPhoneNumberAndDoctorIdNot(inputParam.getPhoneNumber(), inputParam.getDoctorId())).thenReturn(true);

		Authentication authentication = new AuthencationTest();
		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().adminTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}
		};
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERROR_DATA_PHONE_NUMBER_EXIST, inputParam.getLanguage())).thenReturn(messageDTO);
		try {
			doctorService.updateDoctor(inputParam, authentication);
			Assertions.fail();
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERROR_DATA_PHONE_NUMBER_EXIST));
		} catch (IOException e) {
			Assertions.fail();
		}
	}

	@Test
	void updateDoctor_err_Email() {
		DoctorDTO inputParam = createDoctorDto();
		inputParam.setDoctorId(1);

		DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon.convertObjectToObject(inputParam, DoctorsEntity.class);
		DoctorsEntity doctorsEntityMock = new DoctorsEntity();
		doctorsEntityMock.setDoctorId(1);
		doctorsEntityMock.setAvatar("avatar");
		doctorsEntityMock.setFullname("fullName");
		doctorsEntityMock.setPhoneNumber("012345678");
		Mockito.when(doctorsRepositoryJPA.findByDoctorId(inputParam.getDoctorId())).thenReturn(doctorsEntity);

		new MockUp<Base64Util>() {
			@mockit.Mock
			public MultipartFile base64ToMultipart(String base64) {
				return new MultipartFileTest();
			}

			@mockit.Mock
			public String toBase64(String filePath) {
				return inputParam.getAvatar();
			}

			@mockit.Mock
			public boolean isBase64String(String base64value) {
				return true;
			}
		};

		new MockUp<FnCommon>() {
			@mockit.Mock
			public boolean checkBriefcaseValid(String fileName, byte[] file, Integer maxFileSizeMb) {
				return true;
			}

			@mockit.Mock
			public String uploadFile(String containerFolder, MultipartFile file) {
				return "";
			}
		};

		CastWardDto castWardDtoMock = this.createWardDto();
		castWardDtoMock.setDistrictCode(inputParam.getDistrictCode());
		castWardDtoMock.setProvinceCode(inputParam.getProvinceCode());
		Mockito.when(castWardRepository.getAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(createWardDto());

		Mockito.when(doctorsRepositoryJPA.existsByPhoneNumberAndDoctorIdNot(inputParam.getPhoneNumber(), inputParam.getDoctorId())).thenReturn(false);

		Mockito.when(doctorsRepositoryJPA.existsByEmailAndDoctorIdNot(inputParam.getEmail(), inputParam.getDoctorId())).thenReturn(true);

		Authentication authentication = new AuthencationTest();
		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().adminTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}
		};
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERROR_DATA_EMAIL_EXIST, inputParam.getLanguage())).thenReturn(messageDTO);
		try {
			doctorService.updateDoctor(inputParam, authentication);
			Assertions.fail();
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERROR_DATA_EMAIL_EXIST));
		} catch (IOException e) {
			Assertions.fail();
		}
	}

	@Test
	void updateDoctor_err_HealthacilitieCodes() {
		DoctorDTO inputParam = createDoctorDto();
		inputParam.setDoctorId(1);

		DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon.convertObjectToObject(inputParam, DoctorsEntity.class);
		DoctorsEntity doctorsEntityMock = new DoctorsEntity();
		doctorsEntityMock.setDoctorId(1);
		doctorsEntityMock.setAvatar("avatar");
		doctorsEntityMock.setFullname("fullName");
		doctorsEntityMock.setPhoneNumber("012345678");
		Mockito.when(doctorsRepositoryJPA.findByDoctorId(inputParam.getDoctorId())).thenReturn(doctorsEntity);

		new MockUp<Base64Util>() {
			@mockit.Mock
			public MultipartFile base64ToMultipart(String base64) {
				return new MultipartFileTest();
			}

			@mockit.Mock
			public String toBase64(String filePath) {
				return inputParam.getAvatar();
			}

			@mockit.Mock
			public boolean isBase64String(String base64value) {
				return true;
			}
		};

		new MockUp<FnCommon>() {
			@mockit.Mock
			public boolean checkBriefcaseValid(String fileName, byte[] file, Integer maxFileSizeMb) {
				return true;
			}

			@mockit.Mock
			public String uploadFile(String containerFolder, MultipartFile file) {
				return "";
			}
		};

		CastWardDto castWardDtoMock = this.createWardDto();
		castWardDtoMock.setDistrictCode(inputParam.getDistrictCode());
		castWardDtoMock.setProvinceCode(inputParam.getProvinceCode());
		Mockito.when(castWardRepository.getAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(createWardDto());

		Mockito.when(doctorsRepositoryJPA.existsByPhoneNumberAndDoctorIdNot(inputParam.getPhoneNumber(), inputParam.getDoctorId())).thenReturn(false);

		Mockito.when(doctorsRepositoryJPA.existsByEmailAndDoctorIdNot(inputParam.getEmail(), inputParam.getDoctorId())).thenReturn(false);

		Mockito.when(healthfacilitiesRepositoryJPA.countByHealthfacilitiesCodeIn(inputParam.getHealthacilitieCodes()))
				.thenReturn(inputParam.getHealthacilitieCodes().size() + 1);

		Authentication authentication = new AuthencationTest();
		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().adminTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}
		};
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERR_HEALTHFACILITIE_NOT_FOUND, inputParam.getLanguage())).thenReturn(messageDTO);
		try {
			doctorService.updateDoctor(inputParam, authentication);
			Assertions.fail();
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERR_HEALTHFACILITIE_NOT_FOUND));
		} catch (IOException e) {
			Assertions.fail();
		}
	}

	@Test
	void updateDoctor_err_SpecialistIds() {
		DoctorDTO inputParam = createDoctorDto();
		inputParam.setDoctorId(1);

		DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon.convertObjectToObject(inputParam, DoctorsEntity.class);
		DoctorsEntity doctorsEntityMock = new DoctorsEntity();
		doctorsEntityMock.setDoctorId(1);
		doctorsEntityMock.setAvatar("avatar");
		doctorsEntityMock.setFullname("fullName");
		doctorsEntityMock.setPhoneNumber("012345678");
		Mockito.when(doctorsRepositoryJPA.findByDoctorId(inputParam.getDoctorId())).thenReturn(doctorsEntity);

		new MockUp<Base64Util>() {
			@mockit.Mock
			public MultipartFile base64ToMultipart(String base64) {
				return new MultipartFileTest();
			}

			@mockit.Mock
			public String toBase64(String filePath) {
				return inputParam.getAvatar();
			}

			@mockit.Mock
			public boolean isBase64String(String base64value) {
				return true;
			}
		};

		new MockUp<FnCommon>() {
			@mockit.Mock
			public boolean checkBriefcaseValid(String fileName, byte[] file, Integer maxFileSizeMb) {
				return true;
			}

			@mockit.Mock
			public String uploadFile(String containerFolder, MultipartFile file) {
				return "";
			}
		};

		CastWardDto castWardDtoMock = this.createWardDto();
		castWardDtoMock.setDistrictCode(inputParam.getDistrictCode());
		castWardDtoMock.setProvinceCode(inputParam.getProvinceCode());
		Mockito.when(castWardRepository.getAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(createWardDto());

		Mockito.when(doctorsRepositoryJPA.existsByPhoneNumberAndDoctorIdNot(inputParam.getPhoneNumber(), inputParam.getDoctorId())).thenReturn(false);

		Mockito.when(doctorsRepositoryJPA.existsByEmailAndDoctorIdNot(inputParam.getEmail(), inputParam.getDoctorId())).thenReturn(false);

		Mockito.when(healthfacilitiesRepositoryJPA.countByHealthfacilitiesCodeIn(inputParam.getHealthacilitieCodes()))
				.thenReturn(inputParam.getHealthacilitieCodes().size());

		Mockito.when(specialistsRepositoryJPA.countBySpecialistIdIn(inputParam.getSpecialistIds()))
				.thenReturn(inputParam.getSpecialistIds().size() + 1);

		Authentication authentication = new AuthencationTest();
		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().adminTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}
		};
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERR_SPECIAL_NOT_FOUND, inputParam.getLanguage())).thenReturn(messageDTO);
		try {
			doctorService.updateDoctor(inputParam, authentication);
			Assertions.fail();
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERR_SPECIAL_NOT_FOUND));
		} catch (IOException e) {
			Assertions.fail();
		}
	}

	@Test
	void checkPermisisonUpdateDoctor() throws TeleCareException {
		Authentication authentication = new AuthencationTest();
		List<String> healthacilitieCodes = Arrays.asList("01014");
		String language = "vi";
		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().adminTest();

		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}
		};

		doctorService.checkPermisisonUpdateDoctor(authentication, healthacilitieCodes, language);
	}

	@Test
	void checkPermisisonUpdateDoctorException() throws TeleCareException {
		Authentication authentication = new AuthencationTest();
		List<String> healthacilitieCodes = Arrays.asList("01014");
		String language = "vi";
		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().patientTest();

		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}
		};
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(403);
		messageDTO.setDescription("Khong co quyen truy cap");
		Mockito.when(messageService.getMessage(Constants.ERR_USER_PERMISSION, language)).thenReturn(messageDTO);
		Assertions.assertThrows(TeleCareException.class, () -> {
			doctorService.checkPermisisonUpdateDoctor(authentication, healthacilitieCodes, language);
		});
	}

	@Test
	void convertListDataDoctorToExcel() {
		List<DoctorDTO> listDataExport = new ArrayList<>();
		DoctorDTO dataParams = new DoctorDTO();

		new MockUp<FnCommon>() {
			@mockit.Mock
			public boolean createFolder(String containerFolder) {
				return true;
			}
		};

		new MockUp<ExcelCommon>() {
			@mockit.Mock
			public Optional<Path> exportExcel(ExcellSheet sheetExport, String pathFile, String strTitle) {
				return Optional.ofNullable(null);
			}
		};

		MatcherAssert.assertThat(doctorService.convertListDataDoctorToExcel(listDataExport, dataParams), Matchers.notNullValue());
	}

//	@Test
//	void getDoctorHomepageWeb() throws IOException, TeleCareException {
//		Authentication authentication = null;
//		String language = "vi";
//		TelecareUserEntity telecareUser = new TelecareUserEntity(){
//			@Override
//			public boolean isDoctor() throws TeleCareException {
//				return true;
//			}
//		};
//		new MockUp<FnCommon>(){
//			@mockit.Mock
//			public  TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
//				return telecareUser;
//			}
//
//			@mockit.Mock
//			public String getStringToken(Authentication authentication) {
//				return "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJwQW9EZHExTXZYNmE0bFFIYkFmWl9WRG5PemtDdHJtT05kSEwybEJDcGw0In0.eyJleHAiOjE2MTQxNTY1NzIsImlhdCI6MTYxNDA3MDE3MiwianRpIjoiMzhmYTNmYzMtOGIxNS00MDBjLTljODMtZWY3NmU0YThkNmMxIiwiaXNzIjoiaHR0cDovLzEwLjYwLjE1NS4zMjo5MzU5L2F1dGgvcmVhbG1zL3RlbGVjYXJlIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6ImU0YjE2MjI2LTkxNjUtNGNlNi1hNjdjLWEwMGFhNTY3ZDRiNyIsInR5cCI6IkJlYXJlciIsImF6cCI6InRlbGVjYXJlIiwic2Vzc2lvbl9zdGF0ZSI6IjEyNGNmNmQ1LWU3M2ItNDNjMS1hOTAxLTZiZjk5OGRlOTk3MSIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiaHR0cDovLzEwLjYwLjE1NS4zMjo4NTc2IiwiaHR0cDovLzEwLjYwLjE1NS4zMjo4Nzg3IiwiaHR0cDovL2xvY2FsaG9zdDo0MjAwIl0sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsidGVsZWNhcmUiOnsicm9sZXMiOlsiVGVsZWNhcmVfRG9jdG9yIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJCYWMgc3kgQW5uZHJldyIsInByZWZlcnJlZF91c2VybmFtZSI6IjA3MDcwNzA3MDciLCJ0ZWxlY2FyZVVzZXJJZCI6IjEwMSIsImdpdmVuX25hbWUiOiJCYWMgc3kgQW5uZHJldyIsImZhbWlseV9uYW1lIjoiIiwiZW1haWwiOiJhYmMwN0BnbWFpbC5jb20ifQ.GJ2EbhJ668PqaNzVN70dVPb8QQWuaS_-Y1TuzuvAgBU3kgn6W5o8wpy696Hac94X7XrLrmHAzZpD7N27-yQEvuqOYxctEbKdY2Q3UYUGaJnZ19xTUvwH5NrTEhiLEcyGybCQ98F4CF1FWyrcM2BFDvRmPjry1X6a0AbgR9fqhd6zJbwSeyj7cjny9mmkYb6HldYRqPagxYBwWc-Lw7nq1XHgJUoysbnVUeCp7lPOnXEeoEWnGVDZq5RdHLKWqR9Pw5QzVEPMcKlzF9JgNtPjhXROKUhk8SEXiXDgMFxAr3txc1qXpyFlpQ9qs7B-MLga_JxQW4aKEaURA2oj8NrqtA";
//			}
//		};
//
//		telecareUser.setKeycloakUserId("1");
//		telecareUser.setTelecareUserId(1);
//		Mockito.when(sysUsersRepositoryJPA.findFirstByKeycloakUserId(telecareUser.getKeycloakUserId())).thenReturn(new SysUsersEntity());
//		Integer doctorId = telecareUser.getTelecareUserId();
//		Mockito.when(doctorRepository.getPatients(doctorId)).thenReturn(new DoctorHomepageWebDTO());
//		Mockito.when(doctorsCommentsRepositoryJPA.countByDoctorIdAndIsActiveAndIsDelete(doctorId, Constants.IS_ACTIVE, Constants.IS_NOT_DELETE)).thenReturn(1);
//		new MockUp<System>() {
//			@mockit.Mock
//			public String getenv(String name) {
//				if("Communications".equals(name)) {
//					return "http://10.60.155.32:8576/api/v1/communication-history";
//				}
//				return "";
//			}
//		};
//		DoctorHomepageWebDTO actualResult = (DoctorHomepageWebDTO) doctorService.getDoctorHomepageWeb(authentication, language);
//		assertThat(actualResult, Matchers.notNullValue());
//	}

	@Test
	void createDoctorVideoCall_normal_01() throws TeleCareException, IOException {
		Authentication authentication = new AuthencationTest();
		doctorService = Mockito.spy(new DoctorServiceImpl() {
			@Override
			public void checkPermisisonUpdateDoctor(Authentication authentication, List<String> healthacilitieCodes, String language) {
				return;
			}
		});
		MockitoAnnotations.initMocks(this);
		DoctorDTO doctorDTO = createDoctorDto();

		DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon.convertObjectToObject(doctorDTO, DoctorsEntity.class);
		doctorsEntity.setDoctorId(1);
		doctorsEntity.setDoctorCode(
				FnCommon.nameToNameCode(doctorsEntity.getFullname()) + doctorsEntity.getPhoneNumber());
		Mockito.when(otpServiceJPA.existsById(Mockito.any(OtpIdentify.class))).thenReturn(true);
		OtpEntity otpEntity = new OtpEntity();
		otpEntity.setOtp(doctorDTO.getOtp());
		otpEntity.setSignDate(new Date());
		otpEntity.setDuration(1000);
		Mockito.when(otpServiceJPA.getById(Mockito.any(OtpIdentify.class))).thenReturn(otpEntity);

		new MockUp<FnCommon>() {
			@mockit.Mock
			public boolean checkBriefcaseValid(String fileName, byte[] file, Integer maxFileSizeMb) {
				return true;
			}

			@mockit.Mock
			public String uploadFile(String containerFolder, MultipartFile file) {
				return "";
			}
		};

		new MockUp<Base64Util>() {
			@mockit.Mock
			public MultipartFile base64ToMultipart(String base64) {
				return new MultipartFileTest();
			}
		};

		Mockito.when(castWardRepository.getAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(createWardDto());

		Mockito.when(doctorsRepositoryJPA.existsByPhoneNumber(doctorDTO.getPhoneNumber())).thenReturn(false);

		Mockito.when(doctorsRepositoryJPA.existsByEmail(doctorDTO.getEmail())).thenReturn(false);

		Mockito.when(healthfacilitiesRepositoryJPA.countByHealthfacilitiesCodeIn(doctorDTO.getHealthacilitieCodes()))
				.thenReturn(doctorDTO.getHealthacilitieCodes().size());

		Mockito.when(specialistsRepositoryJPA.countBySpecialistIdIn(doctorDTO.getSpecialistIds()))
				.thenReturn(doctorDTO.getSpecialistIds().size());

		DoctorsEntity doctorsEntitySaved = new DoctorsEntity();
		doctorsEntity.setIsDelete(false);
		doctorsEntity.setDoctorId(null);
		doctorsEntity.setAvatar("");
		doctorsEntitySaved.setDoctorId(1);

		Mockito.when(doctorsRepositoryJPA.save(doctorsEntity)).thenReturn(doctorsEntitySaved);
		Mockito.when(doctorsRepositoryJPA.save(Mockito.any())).thenReturn(new DoctorsEntity());
		Mockito.when(keycloakService.createUserInKeyCloak(Mockito.any(DoctorDTO.class)))
				.thenReturn("keyClockUser");
		doctorsEntitySaved.setKeycloakUserId("keyClockUser");

		DoctorsEntity doctorsEntityUpdate = new DoctorsEntity();
		doctorsEntityUpdate.setKeycloakUserId("keyClockUser");
		doctorsEntityUpdate.setDoctorId(doctorsEntitySaved.getDoctorId());
		Mockito.when(doctorsRepositoryJPA.save(doctorsEntitySaved)).thenReturn(doctorsEntityUpdate);
		// send to sys user create to account service
		SysUsersEntity sysUsersEntity = new SysUsersEntity();
		sysUsersEntity.setKeycloakUserId(doctorsEntityUpdate.getKeycloakUserId());
		sysUsersEntity.setId(doctorsEntityUpdate.getDoctorId());
		sysUsersEntity.setType(Constants.DOCTOR_TYPE);

		Mockito.when(doctorsSpecialistsRepositoryJPA.saveAll(Mockito.any(List.class))).thenReturn(new ArrayList<>());
		Mockito.when(doctorsHealthfacilitiesRepositoryJPA.saveAll(Mockito.any(List.class))).thenReturn(new ArrayList<>());
		Mockito.doNothing().when(videoCallService).registerDevices("0987364758");
		DoctorDTO result = (DoctorDTO) doctorService.createDoctorAndVideoCall(doctorDTO, authentication);


		MatcherAssert.assertThat(result, Matchers.notNullValue());
	}

	@Test
	void createDoctorImport_normal_01() throws TeleCareException, IOException {
		Authentication authentication = new AuthencationTest();
		doctorService = Mockito.spy(new DoctorServiceImpl() {
			@Override
			public void checkPermisisonUpdateDoctor(Authentication authentication, List<String> healthacilitieCodes, String language) {
				return;
			}
			@Override
			public Integer getSysUserId(Authentication authentication1, String language){
				return 1;
			}
		});
		MockitoAnnotations.initMocks(this);
		DoctorDTO doctorDTO = createDoctorDto();

		DoctorsEntity doctorsEntity = (DoctorsEntity) FnCommon.convertObjectToObject(doctorDTO, DoctorsEntity.class);
		doctorsEntity.setDoctorId(1);
		doctorsEntity.setDoctorCode(
				FnCommon.nameToNameCode(doctorsEntity.getFullname()) + doctorsEntity.getPhoneNumber());
		Mockito.when(otpServiceJPA.existsById(Mockito.any(OtpIdentify.class))).thenReturn(true);
		OtpEntity otpEntity = new OtpEntity();
		otpEntity.setOtp(doctorDTO.getOtp());
		otpEntity.setSignDate(new Date());
		otpEntity.setDuration(1000);
		Mockito.when(otpServiceJPA.getById(Mockito.any(OtpIdentify.class))).thenReturn(otpEntity);

		new MockUp<FnCommon>() {
			@mockit.Mock
			public boolean checkBriefcaseValid(String fileName, byte[] file, Integer maxFileSizeMb) {
				return true;
			}

			@mockit.Mock
			public String uploadFile(String containerFolder, MultipartFile file) {
				return "";
			}
		};

		new MockUp<Base64Util>() {
			@mockit.Mock
			public MultipartFile base64ToMultipart(String base64) {
				return new MultipartFileTest();
			}
		};

		Mockito.when(castWardRepository.getAddress(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(createWardDto());

		Mockito.when(doctorsRepositoryJPA.existsByPhoneNumber(doctorDTO.getPhoneNumber())).thenReturn(false);

		Mockito.when(doctorsRepositoryJPA.existsByEmail(doctorDTO.getEmail())).thenReturn(false);

		Mockito.when(healthfacilitiesRepositoryJPA.countByHealthfacilitiesCodeIn(doctorDTO.getHealthacilitieCodes()))
				.thenReturn(doctorDTO.getHealthacilitieCodes().size());

		Mockito.when(specialistsRepositoryJPA.countBySpecialistIdIn(doctorDTO.getSpecialistIds()))
				.thenReturn(doctorDTO.getSpecialistIds().size());

		DoctorsEntity doctorsEntitySaved = new DoctorsEntity();
		doctorsEntity.setIsDelete(false);
		doctorsEntity.setDoctorId(null);
		doctorsEntity.setAvatar("");
		doctorsEntitySaved.setDoctorId(1);

		Mockito.when(doctorsRepositoryJPA.save(doctorsEntity)).thenReturn(doctorsEntitySaved);
		Mockito.when(doctorsRepositoryJPA.save(Mockito.any())).thenReturn(new DoctorsEntity());
		Mockito.when(keycloakService.createUserInKeyCloak(Mockito.any(DoctorDTO.class)))
				.thenReturn("keyClockUser");
		doctorsEntitySaved.setKeycloakUserId("keyClockUser");

		DoctorsEntity doctorsEntityUpdate = new DoctorsEntity();
		doctorsEntityUpdate.setKeycloakUserId("keyClockUser");
		doctorsEntityUpdate.setDoctorId(doctorsEntitySaved.getDoctorId());
		Mockito.when(doctorsRepositoryJPA.save(doctorsEntitySaved)).thenReturn(doctorsEntityUpdate);
		// send to sys user create to account service
		SysUsersEntity sysUsersEntity = new SysUsersEntity();
		sysUsersEntity.setKeycloakUserId(doctorsEntityUpdate.getKeycloakUserId());
		sysUsersEntity.setId(doctorsEntityUpdate.getDoctorId());
		sysUsersEntity.setType(Constants.DOCTOR_TYPE);

		Mockito.when(doctorsSpecialistsRepositoryJPA.saveAll(Mockito.any(List.class))).thenReturn(new ArrayList<>());
		Mockito.when(doctorsHealthfacilitiesRepositoryJPA.saveAll(Mockito.any(List.class))).thenReturn(new ArrayList<>());
		Mockito.doNothing().when(videoCallService).registerDevices("0987364758");
		DoctorDTO result = (DoctorDTO) doctorService.createDoctorImport(doctorDTO, authentication);


		MatcherAssert.assertThat(result, Matchers.notNullValue());
	}
}

class MultipartFileTest implements MultipartFile {

	@Override
	public String getName() {
		return "";
	}

	@Override
	public String getOriginalFilename() {
		return "";
	}

	@Override
	public String getContentType() {
		return "";
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public long getSize() {
		return 0;
	}

	@Override
	public byte[] getBytes() throws IOException {
		return new byte[0];
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return null;
	}

	@Override
	public void transferTo(File file) throws IOException, IllegalStateException {

	}
}
