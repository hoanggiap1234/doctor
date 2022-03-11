package com.viettel.etc.dto;

import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
class DTOTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void adminDTO() {
        MatcherAssert.assertThat(AdminDTO.class,
                allOf(hasValidBeanConstructor(), hasValidBeanEquals(), hasValidGettersAndSetters(),
                        hasValidBeanHashCode()));
    }

    @Test
    void ResponseDTO() {
        MatcherAssert.assertThat(ResponseDTO.class,
                allOf(hasValidBeanConstructor(), hasValidBeanEquals(), hasValidGettersAndSetters(),
                        hasValidBeanHashCode(), hasValidBeanToString()));
    }

    @Test
    void BookingDaysConsultantDoctorsDTO() {
        MatcherAssert.assertThat(BookingDaysConsultantDoctorsDTO.class,
                allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
    }

    @Test
    void BookingInformationDTO() {
        MatcherAssert.assertThat(BookingInformationDTO.class,
                allOf(hasValidBeanConstructor(), hasValidBeanEquals(), hasValidGettersAndSetters(),
                        hasValidBeanHashCode(), hasValidBeanToString()));
    }

    @Test
    void BookingTimesConsultantDoctorsDTO() {
        MatcherAssert.assertThat(BookingTimesConsultantDoctorsDTO.class,
                allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
    }

    @Test
    void CastWardDto() {
        MatcherAssert.assertThat(CastWardDto.class,
                allOf(hasValidBeanConstructor(), hasValidBeanEquals(), hasValidGettersAndSetters(),
                        hasValidBeanHashCode(), hasValidBeanToString()));
    }

    @Test
    void CatsPhasesDTO() {
        MatcherAssert.assertThat(CatsPhasesDTO.class,
                allOf(hasValidBeanConstructor(), hasValidBeanEquals(), hasValidGettersAndSetters(),
                        hasValidBeanHashCode(), hasValidBeanToString()));
    }

    @Test
    void DaysDTO() {
        MatcherAssert.assertThat(DaysDTO.class,
                allOf(hasValidBeanConstructor(), hasValidBeanEquals(), hasValidGettersAndSetters(),
                        hasValidBeanHashCode(), hasValidBeanToString()));
    }

    @Test
    void DoctorDTO() {
        MatcherAssert.assertThat(DoctorDTO.class,
                allOf(hasValidBeanConstructor(), hasValidGettersAndSettersExcluding("avatar")));
    }

    @Test
    void DoctorsCalendarBookingTimesConsultantDTO() {
        MatcherAssert.assertThat(DoctorsCalendarBookingTimesConsultantDTO.class,
                allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
    }

    @Test
    void DoctorsCalendarsDTO() {
        MatcherAssert.assertThat(DoctorsCalendarsDTO.class,
                allOf(hasValidBeanConstructor(), hasValidBeanEquals(), hasValidGettersAndSetters(),
                        hasValidBeanHashCode(), hasValidBeanToString()));
    }

    @Test
    void TimeSlotDTO() {
        MatcherAssert.assertThat(TimeSlotDTO.class,
                allOf(hasValidBeanConstructor(), hasValidBeanEquals(), hasValidGettersAndSetters(),
                        hasValidBeanHashCode(), hasValidBeanToString()));
    }

}