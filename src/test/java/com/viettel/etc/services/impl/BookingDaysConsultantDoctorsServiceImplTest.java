package com.viettel.etc.services.impl;

import com.viettel.etc.dto.BookingDaysConsultantDoctorsDTO;
import com.viettel.etc.repositories.BookingDaysConsultantDoctorsRepository;
import com.viettel.etc.services.BookingDaysConsultantDoctorsService;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

class BookingDaysConsultantDoctorsServiceImplTest {

    @Mock
    private BookingDaysConsultantDoctorsRepository bookingDaysConsultantDoctorsRepository;

    @InjectMocks
    private BookingDaysConsultantDoctorsService bookingDaysConsultantDoctorsService;

    @BeforeEach
    void setUp() {
        bookingDaysConsultantDoctorsService = new BookingDaysConsultantDoctorsServiceImpl();
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getBookingDaysConsultantDoctors_01() {
        ResultSelectEntity resultSelectEntityMock = new ResultSelectEntity();
        resultSelectEntityMock.setListData(Arrays.asList(new BookingDaysConsultantDoctorsDTO()));
        resultSelectEntityMock.setCount(1);
        Mockito.when(bookingDaysConsultantDoctorsRepository.getBookingDaysConsultantDoctors(Mockito.any(BookingDaysConsultantDoctorsDTO.class)))
                .thenReturn(resultSelectEntityMock);
        ResultSelectEntity resultSelectEntity = bookingDaysConsultantDoctorsService.getBookingDaysConsultantDoctors(new BookingDaysConsultantDoctorsDTO());
        MatcherAssert.assertThat(resultSelectEntity, Matchers.notNullValue());
        MatcherAssert.assertThat(resultSelectEntity.getListData().get(0), Matchers.isA(BookingDaysConsultantDoctorsDTO.class));
    }
}
