package com.viettel.etc.services.tables;

import com.viettel.etc.repositories.tables.BookingInformationsRepositoryJPA;
import com.viettel.etc.utils.Constants;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class BookingInformationsServiceJPATest {

    @Mock
    BookingInformationsRepositoryJPA booking_informations;

    @Spy
    @InjectMocks
    BookingInformationsServiceJPA bookingInformationsServiceJPA = new BookingInformationsServiceJPA();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void countNumberBooking() {
        LocalTime registerTime = LocalTime.now();
        Date registerDate = new Date();
        Integer doctorId = 1;
        String healthfacilitiesCode = "01212";
        Mockito.when(booking_informations.countByRegisterTimeAndRegisterDateAndDoctorIdAndHealthfacilitiesCodeAndIsActiveAndIsDelete(
                registerTime, registerDate, doctorId, healthfacilitiesCode, Constants.IS_ACTIVE, Constants.IS_NOT_DELETE
        )).thenReturn(1);
        MatcherAssert.assertThat(bookingInformationsServiceJPA.countNumberBooking(registerTime, registerDate, doctorId, healthfacilitiesCode), Matchers.is(1));
    }

    @Test
    void existsByMedicalexaminationDateAndDoctorId() {
        Date date = new Date();
        Integer doctorId = 1;
        Mockito.when(booking_informations.countByMedicalexaminationDateAndDoctorIdAndIsDeleteAndIsActive(date, doctorId, Constants.IS_NOT_DELETE, Constants.IS_ACTIVE)).thenReturn(1);
        MatcherAssert.assertThat(bookingInformationsServiceJPA.existsByMedicalexaminationDateAndDoctorId(date, doctorId), Matchers.isA(Boolean.class) );
    }
}