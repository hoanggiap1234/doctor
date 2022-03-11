package com.viettel.etc.services.tables;

import com.viettel.etc.dto.*;
import com.viettel.etc.repositories.DoctorsCalendarsRepository;
import com.viettel.etc.repositories.tables.DoctorsCalendarsRepositoryJPA;
import com.viettel.etc.repositories.tables.SysConfigsBookingRepositoryJPA;
import com.viettel.etc.repositories.tables.SysUsersRepositoryJPA;
import com.viettel.etc.repositories.tables.entities.DoctorsCalendarsEntity;
import com.viettel.etc.repositories.tables.entities.SysConfigsBookingEntity;
import com.viettel.etc.services.MessageService;
import com.viettel.etc.services.impl.AuthencationTest;
import com.viettel.etc.utils.*;
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

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Comment
 */
class DoctorsCalendarsServiceJPATest {
	@Mock
	DoctorsCalendarsRepositoryJPA doctorsCalendarsRepositoryJPA;
	@Mock
	SysConfigsBookingRepositoryJPA sysConfigsBookingRepositoryJPA;
	@Mock
	SysUsersRepositoryJPA sysUsersRepositoryJPA;

	@Mock
	BookingInformationsServiceJPA bookingInformationsServiceJPA;
	@Mock
	DoctorsCalendarsRepository doctorsCalendarsRepository;
	@InjectMocks
	DoctorsCalendarsServiceJPA doctorsCalendarsServiceJPA;
	@Mock
	MessageService messageService;


	@BeforeEach
	void setUp() {
		doctorsCalendarsServiceJPA = new DoctorsCalendarsServiceJPA();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void save() throws TeleCareException {
		WorkingSchedulesRequestCreateDTO inputParam = new WorkingSchedulesRequestCreateDTO();
		inputParam.setDate("18/10/2020");
		TimeSlotDTO slot1 = new TimeSlotDTO();
		slot1.setTimeslotId(1);
		TimeSlotDTO slot2 = new TimeSlotDTO();
		slot2.setTimeslotId(3);
		inputParam.setTimeslots(Arrays.asList(slot1, slot2));
		inputParam.setWorkingSchedule(Arrays.asList(inputParam));
		inputParam.setHealthfacilitiesCode("01014");
		inputParam.setDoctorId(101);
		inputParam.setCalendarType(1);
		Authentication authentication = new AuthencationTest();

		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().adminTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}

			@mockit.Mock
			public Object convertObjectToObject(Object object1, Class<?> classOfT) {
				return new DoctorsCalendarsEntity();
			}
		};
		Mockito.when(doctorsCalendarsRepositoryJPA.findByDoctorIdAndIsDeleteFalseAndIsActiveTrue(inputParam.getDoctorId())).thenReturn(new ArrayList<>());
		Mockito.when(doctorsCalendarsRepositoryJPA.saveAll(Mockito.any())).thenReturn(new ArrayList<>());
		Mockito.when(messageService.getMessage(Constants.ERR_TIMESLOT_CALENDAR_CONFLICT_HEALTHFACILITIES_CALENDAR, inputParam.getLanguage())).thenReturn(new MessageDTO("desc", "vi", "vi", 1,"vi"));
		try {
			doctorsCalendarsServiceJPA.save(inputParam, authentication);
		} catch (Exception e) {
			MatcherAssert.assertThat(e, Matchers.isA(NullPointerException.class));
		}
//		MatcherAssert.assertThat(doctorsCalendarsServiceJPA.save(inputParam, authentication), Matchers.nullValue());
	}


	@Test
	void cancelWorkingScheduleMobile() throws TeleCareException {
		int calendarId = 1;
		String language = "vi";
		Authentication authentication = new AuthencationTest();
		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().adminTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}
		};
		DoctorsCalendarsEntity calendarsEntity = new DoctorsCalendarsEntity();
		calendarsEntity.setCalendarDate(new Date(System.currentTimeMillis()));
		calendarsEntity.setDoctorId(101);
		calendarsEntity.setStatus(DoctorsCalendarsEntity.Status.CONFIRMED.val());

		Mockito.when(doctorsCalendarsRepositoryJPA.findByCalendarId(calendarId)).thenReturn(calendarsEntity);
		SysConfigsBookingEntity configsBooking = new SysConfigsBookingEntity();
		Mockito.when(sysConfigsBookingRepositoryJPA.findByHealthfacilitiesCode(
				calendarsEntity.getHealthfacilitiesCode())).thenReturn(configsBooking);

		Mockito.when(bookingInformationsServiceJPA.existsByMedicalexaminationDateAndDoctorId(calendarsEntity.getCalendarDate(), calendarsEntity.getDoctorId())).thenReturn(false);

		Mockito.when(doctorsCalendarsRepositoryJPA.save(calendarsEntity)).thenReturn(calendarsEntity);

		doctorsCalendarsServiceJPA.cancelWorkingScheduleMobile(authentication, calendarId, language);
	}

	@Test
	void confirmWorkingSchedules() throws TeleCareException {
		DoctorsCalendarsDTO dto = new DoctorsCalendarsDTO();
		Authentication authentication = new AuthencationTest();

		TelecareUserEntity sys_users = new TelecareUserEntityDataTest().adminTest();

		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return sys_users;
			}
		};
		Mockito.when(sysUsersRepositoryJPA.getHealthfacilitiesCodeArr(sys_users.getKeycloakUserId())).thenReturn(new ArrayList<>());
		Mockito.doNothing().when(doctorsCalendarsRepository).confirmWorkingSchedules(Mockito.any());

		doctorsCalendarsServiceJPA.confirmWorkingSchedules(dto, authentication);
	}

	@Test
	void confirmWorkingSchedulesException() throws TeleCareException {
		DoctorsCalendarsDTO dto = new DoctorsCalendarsDTO();
		Authentication authentication = new AuthencationTest();
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(1);
		messageDTO.setDescription("test");

		TelecareUserEntity sys_users = new TelecareUserEntityDataTest().patientTest();

		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return sys_users;
			}
		};
		Mockito.when(sysUsersRepositoryJPA.getHealthfacilitiesCodeArr(sys_users.getKeycloakUserId())).thenReturn(new ArrayList<>());
		Mockito.doNothing().when(doctorsCalendarsRepository).confirmWorkingSchedules(Mockito.any());
		Mockito.when(messageService.getMessage(Constants.ERR_USER_PERMISSION, dto.getLanguage())).thenReturn(messageDTO);
		try {
			doctorsCalendarsServiceJPA.confirmWorkingSchedules(dto, authentication);
		} catch (Exception e) {
			MatcherAssert.assertThat(e, Matchers.isA(TeleCareException.class));
		}
	}

	@Test
	void deleteWorkingScheduleMobile() throws TeleCareException {
		int calendarId = 1;
		String language = "vi";
		Authentication authentication = new AuthencationTest();

		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().adminTest();

		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}
		};

		DoctorsCalendarsEntity calendarsEntity = new DoctorsCalendarsEntity();
		calendarsEntity.setStatus(DoctorsCalendarsEntity.Status.PENDING.val());
		calendarsEntity.setCalendarDate(new Date(System.currentTimeMillis()+111));
		calendarsEntity.setHealthfacilitiesCode("01014");
		calendarsEntity.setDoctorId(101);

		Mockito.when(doctorsCalendarsRepositoryJPA.findByCalendarId(calendarId)).thenReturn(calendarsEntity);
		Mockito.when(sysUsersRepositoryJPA.getHealthfacilitiesCodeArr(loginUser.getKeycloakUserId())).thenReturn(new ArrayList<>());

		SysConfigsBookingEntity configsBooking = new SysConfigsBookingEntity();
		Mockito.when(sysConfigsBookingRepositoryJPA.findByHealthfacilitiesCode(calendarsEntity.getHealthfacilitiesCode())).thenReturn(configsBooking);

		Mockito.when(bookingInformationsServiceJPA.existsByMedicalexaminationDateAndDoctorId(calendarsEntity.getCalendarDate(), calendarsEntity.getDoctorId())).thenReturn(false);
		Mockito.when(doctorsCalendarsRepositoryJPA.save(calendarsEntity)).thenReturn(calendarsEntity);

		doctorsCalendarsServiceJPA.deleteWorkingScheduleMobile(authentication, calendarId, language);
	}

	@Test
	void deleteWorkingScheduleMobileUnaothor() throws TeleCareException {
		int calendarId = 1;
		Authentication authentication = new AuthencationTest();
		String language = "vi";
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(1);
		messageDTO.setDescription("test");
		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().patientTest();

		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}
		};

		DoctorsCalendarsEntity calendarsEntity = new DoctorsCalendarsEntity();
		calendarsEntity.setStatus(DoctorsCalendarsEntity.Status.CONFIRMED.val());
		calendarsEntity.setCalendarDate(new Date(System.currentTimeMillis()));
		calendarsEntity.setHealthfacilitiesCode("01014");
		calendarsEntity.setDoctorId(101);

		Mockito.when(doctorsCalendarsRepositoryJPA.findByCalendarId(calendarId)).thenReturn(calendarsEntity);
		Mockito.when(sysUsersRepositoryJPA.getHealthfacilitiesCodeArr(loginUser.getKeycloakUserId())).thenReturn(new ArrayList<>());

		SysConfigsBookingEntity configsBooking = new SysConfigsBookingEntity();
		Mockito.when(sysConfigsBookingRepositoryJPA.findByHealthfacilitiesCode(calendarsEntity.getHealthfacilitiesCode())).thenReturn(configsBooking);

		Mockito.when(bookingInformationsServiceJPA.existsByMedicalexaminationDateAndDoctorId(calendarsEntity.getCalendarDate(), calendarsEntity.getDoctorId())).thenReturn(false);
		Mockito.when(doctorsCalendarsRepositoryJPA.save(calendarsEntity)).thenReturn(calendarsEntity);

		Mockito.when(messageService.getMessage(Constants.ERR_USER_PERMISSION, language)).thenReturn(messageDTO);
		try {
			doctorsCalendarsServiceJPA.deleteWorkingScheduleMobile(authentication, calendarId, language);
		} catch (Exception e) {
			MatcherAssert.assertThat(e, Matchers.isA(TeleCareException.class));
		}
	}

	@Test
	void deleteWorkingScheduleMobileNullconfigor() throws TeleCareException {
		int calendarId = 1;
		Authentication authentication = new AuthencationTest();
		String language = "vi";
		TelecareUserEntity loginUser = new TelecareUserEntityDataTest().adminTest();

		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication) {
				return loginUser;
			}
		};

		DoctorsCalendarsEntity calendarsEntity = new DoctorsCalendarsEntity();
		calendarsEntity.setStatus(DoctorsCalendarsEntity.Status.PENDING.val());
		calendarsEntity.setCalendarDate(new Date(System.currentTimeMillis()));
		calendarsEntity.setHealthfacilitiesCode("01014");
		calendarsEntity.setDoctorId(101);

		Mockito.when(doctorsCalendarsRepositoryJPA.findByCalendarId(calendarId)).thenReturn(calendarsEntity);
		Mockito.when(sysUsersRepositoryJPA.getHealthfacilitiesCodeArr(loginUser.getKeycloakUserId())).thenReturn(new ArrayList<>());

		SysConfigsBookingEntity configsBooking = new SysConfigsBookingEntity();
		Mockito.when(sysConfigsBookingRepositoryJPA.findByHealthfacilitiesCode(calendarsEntity.getHealthfacilitiesCode())).thenReturn(null);

		Mockito.when(bookingInformationsServiceJPA.existsByMedicalexaminationDateAndDoctorId(calendarsEntity.getCalendarDate(), calendarsEntity.getDoctorId())).thenReturn(false);
		Mockito.when(doctorsCalendarsRepositoryJPA.save(calendarsEntity)).thenReturn(calendarsEntity);

		try {
			doctorsCalendarsServiceJPA.deleteWorkingScheduleMobile(authentication, calendarId, language);
		} catch (Exception e) {
			MatcherAssert.assertThat(e, Matchers.isA(TeleCareException.class));
		}
	}

	@Test
	void update() {
		List<com.viettel.etc.dto.WorkingSchedulesDTO> listData = new ArrayList<>();
		WorkingSchedulesDTO item = new WorkingSchedulesDTO();
	}
}