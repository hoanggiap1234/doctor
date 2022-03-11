package com.viettel.etc.services.impl;

import com.viettel.etc.dto.DoctorPriceDTO;
import com.viettel.etc.repositories.BookingDurationsConsultantDoctorsRepository;
import com.viettel.etc.repositories.tables.CatsPhasesRepositoryJPA;
import com.viettel.etc.repositories.tables.entities.CatsPhasesEntity;
import com.viettel.etc.services.BookingDurationsConsultantDoctorsService;
import com.viettel.etc.utils.TeleCareException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

class BookingDurationsConsultantDoctorsServiceImplTest {

	@Mock
	private BookingDurationsConsultantDoctorsRepository bookingDurationsConsultantDoctorsRepository;

	@Mock
	private CatsPhasesRepositoryJPA catsPhasesRepositoryJPA;

	@InjectMocks
	private BookingDurationsConsultantDoctorsService bookingDurationsConsultantDoctorsService;

	@BeforeEach
	void setUp() {
		bookingDurationsConsultantDoctorsService = new BookingDurationsConsultantDoctorsServiceImpl();
		MockitoAnnotations.initMocks(this);
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	void getBookingDaysConsultantDoctors_normal_01() throws TeleCareException {
		List<DoctorPriceDTO> dataResultMock = new ArrayList<>();

		DoctorPriceDTO dataResultMockZeroRecord = new DoctorPriceDTO();
		dataResultMockZeroRecord.setDoctorId(0);
		dataResultMockZeroRecord.setPhaseId(0);
		dataResultMock.add(dataResultMockZeroRecord);

		DoctorPriceDTO dataResultMockFirstRecord = new DoctorPriceDTO();
		dataResultMockFirstRecord.setDoctorId(1);
		dataResultMockFirstRecord.setPhaseId(1);
		dataResultMock.add(dataResultMockFirstRecord);

		DoctorPriceDTO dataResultMockSecondRecord = new DoctorPriceDTO();
		dataResultMockSecondRecord.setDoctorId(2);
		dataResultMockSecondRecord.setPhaseId(2);
		dataResultMock.add(dataResultMockSecondRecord);

		Mockito.when(bookingDurationsConsultantDoctorsRepository.getBookingDurationsConsultantDoctors(Mockito.any(DoctorPriceDTO.class))).thenReturn(dataResultMock);

		List<CatsPhasesEntity> catsPhasesEntityZeroMockRecordList = new ArrayList<>();
		CatsPhasesEntity catsPhasesEntityZeroMockRecord = new CatsPhasesEntity();
		catsPhasesEntityZeroMockRecord.setPhaseId(0);
		catsPhasesEntityZeroMockRecordList.add(catsPhasesEntityZeroMockRecord);
		Mockito.when(catsPhasesRepositoryJPA.findByPhaseIdAndFromDateIsLessThanAndToDateIsGreaterThan(
				Mockito.eq(dataResultMockZeroRecord.getPhaseId()),
				Mockito.any(Date.class))).thenReturn(catsPhasesEntityZeroMockRecordList);

		List<CatsPhasesEntity> catsPhasesEntityFirstMockRecordList = new ArrayList<>();
		Mockito.when(catsPhasesRepositoryJPA.findByPhaseIdAndFromDateIsLessThanAndToDateIsGreaterThan(
				Mockito.eq(dataResultMockFirstRecord.getPhaseId()),
				Mockito.any(Date.class))).thenReturn(catsPhasesEntityFirstMockRecordList);


		List<CatsPhasesEntity> catsPhasesEntitySecondMockRecordList = new ArrayList<>();
		Mockito.when(catsPhasesRepositoryJPA.findByPhaseIdAndFromDateIsLessThanAndToDateIsGreaterThan(
				Mockito.eq(dataResultMockSecondRecord.getPhaseId()),
				Mockito.any(Date.class))).thenReturn(catsPhasesEntitySecondMockRecordList);

		DoctorPriceDTO result = bookingDurationsConsultantDoctorsService.getBookingDurationsConsultantDoctors(new DoctorPriceDTO());
		MatcherAssert.assertThat(result, Matchers.notNullValue());
		MatcherAssert.assertThat(result.getDoctorId(), Matchers.is(0));
	}

	@Test
	void getBookingDaysConsultantDoctors_normal_02() throws TeleCareException {
		List<DoctorPriceDTO> dataResultMock = new ArrayList<>();

		DoctorPriceDTO dataResultMockZeroRecord = new DoctorPriceDTO();
		dataResultMockZeroRecord.setDoctorId(0);
		dataResultMockZeroRecord.setPhaseId(0);
		dataResultMock.add(dataResultMockZeroRecord);

		Mockito.when(bookingDurationsConsultantDoctorsRepository.getBookingDurationsConsultantDoctors(Mockito.any(DoctorPriceDTO.class))).thenReturn(dataResultMock);

		DoctorPriceDTO result = bookingDurationsConsultantDoctorsService.getBookingDurationsConsultantDoctors(new DoctorPriceDTO());
		MatcherAssert.assertThat(result, Matchers.notNullValue());
		MatcherAssert.assertThat(result.getDoctorId(), Matchers.is(0));
	}

	@Test
	void getBookingDaysConsultantDoctors_normal_03() throws TeleCareException {
		List<DoctorPriceDTO> dataResultMock = new ArrayList<>();

		DoctorPriceDTO dataResultMockZeroRecord = new DoctorPriceDTO();
		dataResultMockZeroRecord.setDoctorId(0);
		dataResultMockZeroRecord.setPhaseId(0);
		dataResultMock.add(dataResultMockZeroRecord);

		DoctorPriceDTO dataResultMockFirstRecord = new DoctorPriceDTO();
		dataResultMockFirstRecord.setDoctorId(1);
		dataResultMockFirstRecord.setPhaseId(1);
		dataResultMock.add(dataResultMockFirstRecord);

		DoctorPriceDTO dataResultMockSecondRecord = new DoctorPriceDTO();
		dataResultMockSecondRecord.setDoctorId(2);
		dataResultMockSecondRecord.setPhaseId(2);
		dataResultMock.add(dataResultMockSecondRecord);

		Mockito.when(bookingDurationsConsultantDoctorsRepository.getBookingDurationsConsultantDoctors(Mockito.any(DoctorPriceDTO.class))).thenReturn(dataResultMock);

		List<CatsPhasesEntity> catsPhasesEntityZeroMockRecordList = new ArrayList<>();
		CatsPhasesEntity catsPhasesEntityZeroMockRecord = new CatsPhasesEntity();
		catsPhasesEntityZeroMockRecord.setPhaseId(0);
		catsPhasesEntityZeroMockRecordList.add(catsPhasesEntityZeroMockRecord);
		Mockito.when(catsPhasesRepositoryJPA.findByPhaseIdAndFromDateIsLessThanAndToDateIsGreaterThan(
				Mockito.eq(dataResultMockZeroRecord.getPhaseId()),
				Mockito.any(Date.class))).thenReturn(catsPhasesEntityZeroMockRecordList);

		List<CatsPhasesEntity> catsPhasesEntityFirstMockRecordList = new ArrayList<>();
		Mockito.when(catsPhasesRepositoryJPA.findByPhaseIdAndFromDateIsLessThanAndToDateIsGreaterThan(
				Mockito.eq(dataResultMockFirstRecord.getPhaseId()),
				Mockito.any(Date.class))).thenReturn(catsPhasesEntityFirstMockRecordList);


		List<CatsPhasesEntity> catsPhasesEntitySecondMockRecordList = new ArrayList<>();
		CatsPhasesEntity catsPhasesEntitySecondMockRecord = new CatsPhasesEntity();
		catsPhasesEntitySecondMockRecord.setPhaseId(2);
		catsPhasesEntitySecondMockRecordList.add(catsPhasesEntitySecondMockRecord);
		Mockito.when(catsPhasesRepositoryJPA.findByPhaseIdAndFromDateIsLessThanAndToDateIsGreaterThan(
				Mockito.eq(dataResultMockSecondRecord.getPhaseId()),
				Mockito.any(Date.class))).thenReturn(catsPhasesEntitySecondMockRecordList);

		DoctorPriceDTO result = bookingDurationsConsultantDoctorsService.getBookingDurationsConsultantDoctors(new DoctorPriceDTO());
		MatcherAssert.assertThat(result, Matchers.notNullValue());
		MatcherAssert.assertThat(result.getDoctorId(), Matchers.is(2));
	}

	@Test
	void getBookingDaysConsultantDoctors_empty_bookingDurationsConsultantDoctors() {
/*        List<DoctorPriceDTO> dataResultMock = new ArrayList<>();
        Mockito.when(bookingDurationsConsultantDoctorsRepository.getBookingDurationsConsultantDoctors(Mockito.any(DoctorPriceDTO.class))).thenReturn(dataResultMock);

        try {
            bookingDurationsConsultantDoctorsService.getBookingDurationsConsultantDoctors(new DoctorPriceDTO());
            Assertions.fail();
        } catch (TeleCareException e) {
            MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERROR_INPUTPARAMS));
        }*/
	}

	@Test
	void getBookingDaysConsultantDoctors_none_phase_id_is_zero() {
/*        List<DoctorPriceDTO> dataResultMock = new ArrayList<>();

        DoctorPriceDTO dataResultMockFirstRecord = new DoctorPriceDTO();
        dataResultMockFirstRecord.setDoctorId(1);
        dataResultMockFirstRecord.setPhaseId(1);
        dataResultMock.add(dataResultMockFirstRecord);

        DoctorPriceDTO dataResultMockSecondRecord = new DoctorPriceDTO();
        dataResultMockSecondRecord.setDoctorId(2);
        dataResultMockSecondRecord.setPhaseId(2);
        dataResultMock.add(dataResultMockSecondRecord);

        Mockito.when(bookingDurationsConsultantDoctorsRepository.getBookingDurationsConsultantDoctors(Mockito.any(DoctorPriceDTO.class))).thenReturn(dataResultMock);

        try {
            bookingDurationsConsultantDoctorsService.getBookingDurationsConsultantDoctors(new DoctorPriceDTO());
            Assertions.fail();
        } catch (TeleCareException e) {
            MatcherAssert.assertThat(e.getErrorApp(), Matchers.is(ErrorApp.ERROR_INPUTPARAMS));
        }*/
	}

}
