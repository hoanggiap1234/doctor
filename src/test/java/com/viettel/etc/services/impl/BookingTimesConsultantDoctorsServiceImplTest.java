package com.viettel.etc.services.impl;

import com.viettel.etc.dto.BookingInformationDTO;
import com.viettel.etc.dto.BookingTimesConsultantDoctorsDTO;
import com.viettel.etc.dto.DoctorsCalendarBookingTimesConsultantDTO;
import com.viettel.etc.dto.MessageDTO;
import com.viettel.etc.repositories.BookingInformationRepository;
import com.viettel.etc.repositories.BookingTimesConsultantDoctorsRepository;
import com.viettel.etc.services.BookingTimesConsultantDoctorsService;
import com.viettel.etc.services.MessageService;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class BookingTimesConsultantDoctorsServiceImplTest {
	@Mock
	private BookingTimesConsultantDoctorsRepository bookingTimesConsultantDoctorsRepository;
	@Mock
	private BookingInformationRepository bookingInformationRepository;
	@Mock
	private MessageService messageService;
	@InjectMocks
	private BookingTimesConsultantDoctorsService bookingTimesConsultantDoctorsService;

	@BeforeEach
	void setUp() {
		bookingTimesConsultantDoctorsService = new BookingTimesConsultantDoctorsServiceImpl();
		MockitoAnnotations.initMocks(this);
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void getBookingTimesConsultantDoctors_null_calendarDate() {
		BookingTimesConsultantDoctorsDTO inputParam = new BookingTimesConsultantDoctorsDTO();
		inputParam.setCalendarDate(null);
		inputParam.setDuration(1);
		inputParam.setDoctorId(1);
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERR_DATA_BOOKING_TIME_INFO, inputParam.getLanguage())).thenReturn(messageDTO);
		try {
			bookingTimesConsultantDoctorsService.getBookingTimesConsultantDoctors(inputParam);
			Assertions.fail();
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERR_DATA_BOOKING_TIME_INFO));
		}
	}

	@Test
	void getBookingTimesConsultantDoctors_null_duration() {
		BookingTimesConsultantDoctorsDTO inputParam = new BookingTimesConsultantDoctorsDTO();
		inputParam.setCalendarDate("1601612859000");
		inputParam.setDuration(null);
		inputParam.setDoctorId(1);
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERR_DATA_BOOKING_TIME_INFO, inputParam.getLanguage())).thenReturn(messageDTO);
		try {
			bookingTimesConsultantDoctorsService.getBookingTimesConsultantDoctors(inputParam);
			Assertions.fail();
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERR_DATA_BOOKING_TIME_INFO));
		}
	}

	@Test
	void getBookingTimesConsultantDoctors_null_doctorId() {
		BookingTimesConsultantDoctorsDTO inputParam = new BookingTimesConsultantDoctorsDTO();
		inputParam.setCalendarDate("1601612859000");
		inputParam.setDuration(1);
		inputParam.setDoctorId(null);
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setCode(400);
		messageDTO.setDescription("test");
		Mockito.when(messageService.getMessage(Constants.ERR_DATA_BOOKING_TIME_INFO, inputParam.getLanguage())).thenReturn(messageDTO);
		try {
			bookingTimesConsultantDoctorsService.getBookingTimesConsultantDoctors(inputParam);
			Assertions.fail();
		} catch (TeleCareException e) {
			MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERR_DATA_BOOKING_TIME_INFO));
		}
	}

	@Test
	void getBookingTimesConsultantDoctors_normal_01() throws TeleCareException {
		BookingTimesConsultantDoctorsDTO inputParam = new BookingTimesConsultantDoctorsDTO();
		inputParam.setCalendarDate("1601612859000");
		inputParam.setDuration(30);
		inputParam.setDoctorId(1);

		List<BookingTimesConsultantDoctorsDTO> listData = new ArrayList<>();
		BookingTimesConsultantDoctorsDTO recordFirst = new BookingTimesConsultantDoctorsDTO();
		recordFirst.setHoursStart(8);
		recordFirst.setMinuteStart(0);
		recordFirst.setHoursEnd(11);
		recordFirst.setMinuteEnd(30);
		listData.add(recordFirst);
		Mockito.when(bookingTimesConsultantDoctorsRepository.getBookingTimesConsultantDoctors(Mockito.any(BookingTimesConsultantDoctorsDTO.class))).thenReturn(listData);

		Mockito.when(bookingInformationRepository.getDataByParam(Mockito.any(BookingInformationDTO.class))).thenReturn(new ArrayList<>());

		BookingTimesConsultantDoctorsDTO maxNumberBookingDoctorDTO = new BookingTimesConsultantDoctorsDTO();
		maxNumberBookingDoctorDTO.setMaxNumberBookingDoctor(100);
		Mockito.when(bookingTimesConsultantDoctorsRepository.getMaxNumberBookingDoctor(Mockito.any(BookingTimesConsultantDoctorsDTO.class))).thenReturn(maxNumberBookingDoctorDTO);

		ResultSelectEntity resultEntity = (ResultSelectEntity) bookingTimesConsultantDoctorsService.getBookingTimesConsultantDoctors(inputParam);

		MatcherAssert.assertThat(resultEntity.getListData().size(), Matchers.is(7));

		MatcherAssert.assertThat(((BookingTimesConsultantDoctorsDTO) resultEntity.getListData().get(0)).getValue(), Matchers.is("08:00"));
		MatcherAssert.assertThat(((BookingTimesConsultantDoctorsDTO) resultEntity.getListData().get(0)).getPeriod(), Matchers.is("08:00-08:30"));
		MatcherAssert.assertThat(((BookingTimesConsultantDoctorsDTO) resultEntity.getListData().get(0)).getCanBook(), Matchers.is(true));

		MatcherAssert.assertThat(((BookingTimesConsultantDoctorsDTO) resultEntity.getListData().get(6)).getValue(), Matchers.is("11:00"));
		MatcherAssert.assertThat(((BookingTimesConsultantDoctorsDTO) resultEntity.getListData().get(6)).getPeriod(), Matchers.is("11:00-11:30"));
		MatcherAssert.assertThat(((BookingTimesConsultantDoctorsDTO) resultEntity.getListData().get(6)).getCanBook(), Matchers.is(true));
	}

	@Test
	void getBookingTimesConsultantDoctors_normal_02() throws TeleCareException {
		BookingTimesConsultantDoctorsDTO inputParam = new BookingTimesConsultantDoctorsDTO();
		inputParam.setCalendarDate("1601612859000");
		inputParam.setDuration(30);
		inputParam.setDoctorId(1);

		List<BookingTimesConsultantDoctorsDTO> listData = new ArrayList<>();
		BookingTimesConsultantDoctorsDTO recordFirst = new BookingTimesConsultantDoctorsDTO();
		recordFirst.setHoursStart(9);
		recordFirst.setMinuteStart(30);
		recordFirst.setHoursEnd(11);
		recordFirst.setMinuteEnd(0);
		listData.add(recordFirst);
		Mockito.when(bookingTimesConsultantDoctorsRepository.getBookingTimesConsultantDoctors(Mockito.any(BookingTimesConsultantDoctorsDTO.class))).thenReturn(listData);

		Mockito.when(bookingInformationRepository.getDataByParam(Mockito.any(BookingInformationDTO.class))).thenReturn(new ArrayList<>());

		Mockito.when(bookingTimesConsultantDoctorsRepository.getMaxNumberBookingDoctor(Mockito.any(BookingTimesConsultantDoctorsDTO.class))).thenReturn(null);

		ResultSelectEntity resultEntity = (ResultSelectEntity) bookingTimesConsultantDoctorsService.getBookingTimesConsultantDoctors(inputParam);

		MatcherAssert.assertThat(resultEntity.getListData().size(), Matchers.is(0));

	}

	@Test
	void getBookingTimesConsultantDoctors() {
	}

	@Test
	void getDoctorsCalendarBookingTimesConsultant() throws TeleCareException {
		DoctorsCalendarBookingTimesConsultantDTO dataParams = new DoctorsCalendarBookingTimesConsultantDTO();
		dataParams.setCalendarDate(1601612859000L);
		dataParams.setDuration(30);
		dataParams.setDoctorId(1);
		dataParams.setHoursStart(9);
		dataParams.setMinuteStart(30);
		dataParams.setHoursEnd(11);
		dataParams.setMinuteEnd(0);
		dataParams.setMaxNumberBookingDoctor(10);

		List<DoctorsCalendarBookingTimesConsultantDTO> doctorsCalendarBookingTimesConsultantList = Arrays.asList(dataParams);
		Mockito.when(bookingTimesConsultantDoctorsRepository.getDoctorsCalendarBookingTimesConsultants(dataParams)).thenReturn(doctorsCalendarBookingTimesConsultantList);

		Optional<DoctorsCalendarBookingTimesConsultantDTO> maxNumberBookingByDoctorOpt = Optional.ofNullable(dataParams);
		Mockito.when(bookingTimesConsultantDoctorsRepository.getMaxNumberBookingDoctorByDoctorId(dataParams)).thenReturn(maxNumberBookingByDoctorOpt);

		Mockito.when(bookingTimesConsultantDoctorsRepository.countBookingInfo(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(1);

		ResultSelectEntity result = bookingTimesConsultantDoctorsService.getDoctorsCalendarBookingTimesConsultant(dataParams);

		MatcherAssert.assertThat(result, Matchers.notNullValue());
		MatcherAssert.assertThat(result.getListData().size(), Matchers.greaterThan(0));
	}
}
