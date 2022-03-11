package com.viettel.etc.services.impl;

import com.viettel.etc.dto.MessageDTO;
import com.viettel.etc.dto.TimeSlotDTO;
import com.viettel.etc.dto.WorkingSchedulesDTO;
import com.viettel.etc.repositories.WorkingSchedulesRepository;
import com.viettel.etc.services.MessageService;
import com.viettel.etc.services.WorkingSchedulesService;
import com.viettel.etc.services.tables.DoctorsCalendarsServiceJPA;
import com.viettel.etc.utils.*;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class WorkingSchedulesServiceImplTest extends BaseTestService {

	@Mock
	private WorkingSchedulesRepository workingSchedulesRepository;

	@Mock
	private DoctorsCalendarsServiceJPA doctorsCalendarsServiceJPA;

	@Mock
	private MessageService messageService;
	@InjectMocks
	private WorkingSchedulesService workingSchedulesService;

	@BeforeEach
	void setUp() {
		workingSchedulesService = new WorkingSchedulesServiceImpl();
		MockitoAnnotations.initMocks(this);
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void getWorkingSchedules_normal_01() throws TeleCareException {
		WorkingSchedulesDTO inputParam = new WorkingSchedulesDTO();
		List<WorkingSchedulesDTO> workingSchedules = new ArrayList<>();
		WorkingSchedulesDTO outputRecord = new WorkingSchedulesDTO();
		List<String> healthfacilitiesCodeList = Arrays.asList("01", "01000", "01007", "NULL", "01014");
		outputRecord.setHealthfacilitiesCodeList(healthfacilitiesCodeList.stream().collect(Collectors.joining(",")));
		outputRecord.setDoctorId(1);
		outputRecord.setHealthfacilitiesCode("01");
		workingSchedules.add(outputRecord);
		ResultSelectEntity resultSelectEntity = new ResultSelectEntity();
		resultSelectEntity.setCount(workingSchedules.size());
		resultSelectEntity.setListData(workingSchedules);

		TelecareUserEntity loginUser = createTestUser("Telecare_Admin");
		Authentication authentication = new AuthencationTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication1) {
				return loginUser;
			}
		};
		Mockito.when(workingSchedulesRepository.getWorkingSchedules(inputParam, loginUser)).thenReturn(resultSelectEntity);

		List<TimeSlotDTO> timeSlotList = new ArrayList<>();
		TimeSlotDTO timeSlotDTO = new TimeSlotDTO();
		timeSlotList.add(timeSlotDTO);
		timeSlotDTO.setDate(java.sql.Date.valueOf(LocalDate.of(2020, 9, 22)));
		timeSlotDTO.setCalendarId(1);
		timeSlotDTO.setDoctorId(1);
		timeSlotDTO.setTimeslotId(16);
		timeSlotDTO.setStatus(1);
		timeSlotDTO.setHealthfacilitiesCode("01");
		timeSlotDTO.setHoursStart(9);
		timeSlotDTO.setMinuteStart(30);
		timeSlotDTO.setHoursEnd(12);
		timeSlotDTO.setMinuteEnd(30);

		TimeSlotDTO timeSlotDTO2 = new TimeSlotDTO();
		timeSlotList.add(timeSlotDTO2);
		timeSlotDTO2.setDate(null);
		timeSlotDTO2.setCalendarId(5);
		timeSlotDTO2.setDoctorId(1);
		timeSlotDTO2.setTimeslotId(46);
		timeSlotDTO2.setStatus(2);
		timeSlotDTO2.setHealthfacilitiesCode("01");
		timeSlotDTO2.setHoursStart(15);
		timeSlotDTO2.setMinuteStart(30);
		timeSlotDTO2.setHoursEnd(18);
		timeSlotDTO2.setMinuteEnd(30);

		List<String> healthfacilitiesCodeListFilterNull = Arrays.asList("01", "01000", "01007", "01014");
		Mockito.when(workingSchedulesRepository.getWorkingScheduleByDoctorId(outputRecord.getDoctorId(), healthfacilitiesCodeListFilterNull, inputParam)).thenReturn(timeSlotList);

		// result Data
		WorkingSchedulesDTO workingSchedulesDTO = new WorkingSchedulesDTO();
		workingSchedulesDTO.setHealthfacilitiesCodeList("01012,01014");
		ResultSelectEntity resultSelectEntityResultData = new ResultSelectEntity();
		resultSelectEntityResultData.setListData(new ArrayList<>(Arrays.asList(workingSchedulesDTO)));


		Mockito.when(workingSchedulesRepository.getWorkingSchedules(inputParam, loginUser)).thenReturn(resultSelectEntityResultData);


		MatcherAssert.assertThat(workingSchedulesService.getWorkingSchedules(inputParam, authentication), Matchers.notNullValue());
		MatcherAssert.assertThat(workingSchedulesService.getWorkingSchedules(inputParam, authentication), Matchers.isA(ResultSelectEntity.class));
	}

	@Test
	void getWorkingSchedules_normal_02() throws TeleCareException {
		WorkingSchedulesDTO inputParam = new WorkingSchedulesDTO();
		inputParam.setFromTime(1600654537000L);
		inputParam.setToTime(1601172937000L);
		List<WorkingSchedulesDTO> workingSchedules = new ArrayList<>();
		WorkingSchedulesDTO outputRecord = new WorkingSchedulesDTO();
		outputRecord.setHealthfacilitiesCodeList(null);
		outputRecord.setDoctorId(1);
		outputRecord.setHealthfacilitiesCode("01");
		workingSchedules.add(outputRecord);
		ResultSelectEntity resultSelectEntity = new ResultSelectEntity();
		resultSelectEntity.setCount(workingSchedules.size());
		resultSelectEntity.setListData(workingSchedules);
		TelecareUserEntity userLogin = createTestUser("Telecare_Admin");
		Authentication authentication = new AuthencationTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication1) {
				return userLogin;
			}
		};
		Mockito.when(workingSchedulesRepository.getWorkingSchedules(inputParam, userLogin)).thenReturn(resultSelectEntity);

		List<TimeSlotDTO> timeSlotList = new ArrayList<>();

		Mockito.when(workingSchedulesRepository.getWorkingScheduleByDoctorId(outputRecord.getDoctorId(), Collections.EMPTY_LIST, inputParam)).thenReturn(timeSlotList);

		// result Data
		ResultSelectEntity resultSelectEntityResultData = workingSchedulesService.getWorkingSchedules(inputParam, authentication);
		MatcherAssert.assertThat(resultSelectEntityResultData.getListData(), Matchers.notNullValue());
//        MatcherAssert.assertThat(resultSelectEntityResultData.getListData().size(), Matchers.is(1));

//        MatcherAssert.assertThat(((List<WorkingSchedulesDTO>) resultSelectEntityResultData.getListData()).get(0).getWorkingSchedule().size(), Matchers.is(0));
	}

	@Test
	void getWorkingSchedules_from_time_invalid() throws TeleCareException {
		WorkingSchedulesDTO inputParam = new WorkingSchedulesDTO();
		inputParam.setFromTime(32527196963000L);

		TelecareUserEntity userLogin = createTestUser("Telecare_Admin");
		Authentication authentication = new AuthencationTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication1) {
				return userLogin;
			}
		};

		Mockito.when(workingSchedulesRepository.getWorkingSchedules(inputParam, userLogin)).thenReturn(new ResultSelectEntity());
		Mockito.when(workingSchedulesRepository.getWorkingScheduleByDoctorId(Mockito.anyInt(), Mockito.anyList(), Mockito.any(WorkingSchedulesDTO.class))).thenReturn(new ArrayList<>());
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERROR_INPUTPARAMS, inputParam.getLanguage())).thenReturn(messageDTO);
		// result Data
		try {
			workingSchedulesService.getWorkingSchedules(inputParam, authentication);
			Assertions.fail("My method didn't throw Exception");
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERROR_INPUTPARAMS));
		}
	}

	@Test
	void getWorkingSchedules_to_time_invalid() throws TeleCareException {
		TelecareUserEntity userLogin = createTestUser("Telecare_Admin");
		Authentication authentication = new AuthencationTest();
		new MockUp<FnCommon>() {
			@mockit.Mock
			public TelecareUserEntity getTelecareUserInfo(Authentication authentication1) {
				return userLogin;
			}
		};

		WorkingSchedulesDTO inputParam = new WorkingSchedulesDTO();
		inputParam.setToTime(32527196963000L);
		Mockito.when(workingSchedulesRepository.getWorkingSchedules(inputParam, userLogin)).thenReturn(new ResultSelectEntity());
		Mockito.when(workingSchedulesRepository.getWorkingScheduleByDoctorId(Mockito.anyInt(), Mockito.anyList(), Mockito.any(WorkingSchedulesDTO.class))).thenReturn(new ArrayList<>());
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERROR_INPUTPARAMS, inputParam.getLanguage())).thenReturn(messageDTO);
		// result Data
		try {
			workingSchedulesService.getWorkingSchedules(inputParam, authentication);
			Assertions.fail("My method didn't throw Exception");
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERROR_INPUTPARAMS));
		}
	}

	@Test
	void updateWorkingSchedules() throws TeleCareException {
		Mockito.when(workingSchedulesRepository.updateWorkingSchedules(Mockito.any(WorkingSchedulesDTO.class))).thenReturn(new ArrayList<>());
		Mockito.when(doctorsCalendarsServiceJPA.update(Mockito.any(), Mockito.any(WorkingSchedulesDTO.class))).thenReturn(new ArrayList<>());

		try {
			workingSchedulesService.updateWorkingSchedules(new WorkingSchedulesDTO(), new AuthencationTest());
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deleteWorkingSchedules_normal_01() throws TeleCareException {
		WorkingSchedulesDTO inputParam = new WorkingSchedulesDTO();
		Mockito.doNothing().when(workingSchedulesRepository).deleteWorkingSchedules(Mockito.any(WorkingSchedulesDTO.class));
		workingSchedulesService.deleteWorkingSchedules(inputParam);
	}

	@Test
	public void deleteWorkingSchedules_normal_02() throws TeleCareException {
		WorkingSchedulesDTO inputParam = new WorkingSchedulesDTO();
		inputParam.setFromTime(1600654537000L);
		inputParam.setToTime(1601172937000L);
		Mockito.doNothing().when(workingSchedulesRepository).deleteWorkingSchedules(Mockito.any(WorkingSchedulesDTO.class));
		workingSchedulesService.deleteWorkingSchedules(inputParam);
	}

	@Test
	public void deleteWorkingSchedules_from_time_invalid() {
		WorkingSchedulesDTO inputParam = new WorkingSchedulesDTO();
		inputParam.setFromTime(32527196963000L);
		Mockito.doNothing().when(workingSchedulesRepository).deleteWorkingSchedules(Mockito.any(WorkingSchedulesDTO.class));
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERROR_INPUTPARAMS, inputParam.getLanguage())).thenReturn(messageDTO);
		try {
			workingSchedulesService.deleteWorkingSchedules(inputParam);
			Assertions.fail("My method didn't throw Exception");
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERROR_INPUTPARAMS));
		}
	}

	@Test
	public void deleteWorkingSchedules_to_time_invalid() {
		WorkingSchedulesDTO inputParam = new WorkingSchedulesDTO();
		inputParam.setToTime(32527196963000L);
		Mockito.doNothing().when(workingSchedulesRepository).deleteWorkingSchedules(Mockito.any(WorkingSchedulesDTO.class));
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERROR_INPUTPARAMS, inputParam.getLanguage())).thenReturn(messageDTO);
		try {
			workingSchedulesService.deleteWorkingSchedules(inputParam);
			Assertions.fail("My method didn't throw Exception");
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERROR_INPUTPARAMS));
		}
	}

}
