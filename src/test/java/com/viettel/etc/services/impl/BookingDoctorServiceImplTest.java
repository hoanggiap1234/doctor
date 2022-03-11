package com.viettel.etc.services.impl;

import com.viettel.etc.dto.BookingDoctorRequestDTO;
import com.viettel.etc.dto.BookingDoctorResponseDTO;
import com.viettel.etc.dto.HealthfacilitiesCalendarTimeSlotDTO;
import com.viettel.etc.dto.MessageDTO;
import com.viettel.etc.repositories.BookingDoctorRepository;
import com.viettel.etc.services.BookingDoctorService;
import com.viettel.etc.services.MessageService;
import com.viettel.etc.services.tables.BookingInformationsServiceJPA;
import com.viettel.etc.utils.Constants;
import com.viettel.etc.utils.ErrorApp;
import com.viettel.etc.utils.TeleCareException;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class BookingDoctorServiceImplTest {

	@Mock
	BookingInformationsServiceJPA bookingInformationServiceJPA;
	@Mock
	private BookingDoctorRepository doctorBookingRepository;
	@Mock
	private MessageService messageService;
	@InjectMocks
	private BookingDoctorService bookingDoctorService;

	@BeforeEach
	void setUp() {
		bookingDoctorService = new BookingDoctorServiceImpl();
		MockitoAnnotations.initMocks(this);
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void getBookingDaysDoctors() {
		ResultSelectEntity resultSelectEntityMock = new ResultSelectEntity();
		resultSelectEntityMock.setListData(Arrays.asList(new BookingDoctorResponseDTO()));
		resultSelectEntityMock.setCount(1);
		Mockito.when(doctorBookingRepository.getBookingDaysDoctors(Mockito.any(BookingDoctorRequestDTO.class))).thenReturn(resultSelectEntityMock);
		ResultSelectEntity resultSelectEntity = bookingDoctorService.getBookingDaysDoctors(new BookingDoctorRequestDTO());
		MatcherAssert.assertThat(resultSelectEntity.getListData(), Matchers.notNullValue());
		MatcherAssert.assertThat(resultSelectEntity.getListData().get(0), Matchers.isA(BookingDoctorResponseDTO.class));
	}

	@Test
	void getBookingHoursDoctors_normal_01() throws TeleCareException, ParseException {
		BookingDoctorRequestDTO dataParams = new BookingDoctorRequestDTO();
		dataParams.setDay(1601714731000L);
		dataParams.setDoctorId(1);
		dataParams.setHealthfacilitiesCode("01014");

		HealthfacilitiesCalendarTimeSlotDTO record_1 = new HealthfacilitiesCalendarTimeSlotDTO();
		record_1.setHourStart(11);
		record_1.setHourEnd(12);
		record_1.setMinuteStart(1);
		record_1.setMinuteEnd(1);
		record_1.setMaxNumberBookingService(10);

		// duration
		record_1.setTimeFrame(30);
		// AM
		record_1.setAmFromHouse(8);
		record_1.setAmFromMinute(30);
		record_1.setAmToHouse(11);
		record_1.setAmToMinute(0);
		// PM
		record_1.setPmFromHouse(13);
		record_1.setPmFromMinute(0);
		record_1.setPmToHouse(15);
		record_1.setPmToMinute(30);

		List<HealthfacilitiesCalendarTimeSlotDTO> healthFacilityCalendarTimeSlotDTOList = new ArrayList<>(Arrays.asList(record_1));

		Mockito.when(doctorBookingRepository.getBookingHoursDoctor(dataParams)).thenReturn(healthFacilityCalendarTimeSlotDTOList);
		Mockito.when(bookingInformationServiceJPA.countNumberBooking(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(0);

		MatcherAssert.assertThat(bookingDoctorService.getBookingHoursDoctors(dataParams), Matchers.isA(ResultSelectEntity.class));
		MatcherAssert.assertThat(bookingDoctorService.getBookingHoursDoctors(dataParams), Matchers.notNullValue());
	}

	@Test
	void getBookingHoursDoctors_normal_02() throws TeleCareException, ParseException {
		BookingDoctorRequestDTO inputParam = new BookingDoctorRequestDTO();
		inputParam.setDay(1601714731000L);

		List<HealthfacilitiesCalendarTimeSlotDTO> healthFacilityCalendarTimeSlotDTOList = new ArrayList<>();
		HealthfacilitiesCalendarTimeSlotDTO record_1 = new HealthfacilitiesCalendarTimeSlotDTO();
		healthFacilityCalendarTimeSlotDTOList.add(record_1);

		Mockito.when(doctorBookingRepository.getBookingHoursDoctors(inputParam)).thenReturn(healthFacilityCalendarTimeSlotDTOList);

		ResultSelectEntity resultSelectEntity = bookingDoctorService.getBookingHoursDoctors(inputParam);
		MatcherAssert.assertThat(resultSelectEntity.getListData().size(), Matchers.is(0));
	}

	@Test
	void getBookingHoursDoctors_err_day() {
		BookingDoctorRequestDTO inputParam = new BookingDoctorRequestDTO();
		inputParam.setDay(33158624393000L);

		List<HealthfacilitiesCalendarTimeSlotDTO> healthFacilityCalendarTimeSlotDTOList = new ArrayList<>();
		HealthfacilitiesCalendarTimeSlotDTO record_1 = new HealthfacilitiesCalendarTimeSlotDTO();
		healthFacilityCalendarTimeSlotDTOList.add(record_1);

		Mockito.when(doctorBookingRepository.getBookingHoursDoctors(inputParam)).thenReturn(healthFacilityCalendarTimeSlotDTOList);
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERROR_INPUTPARAMS, inputParam.getLanguage())).thenReturn(messageDTO);
		TeleCareException teleCareException = Assertions.assertThrows(TeleCareException.class, () -> bookingDoctorService.getBookingHoursDoctors(inputParam));

		MatcherAssert.assertThat(teleCareException.getErrorApp(), Matchers.is(ErrorApp.ERROR_INPUTPARAMS));

	}

}
