package com.viettel.etc.repositories.tables.entities;

import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.google.code.beanmatchers.BeanMatchers.*;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void CatsHealthfacilitiesEntity() {
        MatcherAssert.assertThat(CatsHealthfacilitiesEntity.class,
                allOf(hasValidBeanConstructor(), hasValidBeanEquals(), hasValidGettersAndSetters(),
                        hasValidBeanHashCode()));
    }

    @Test
    void CatsPhasesEntity() {
        BeanMatchers.registerValueGenerator(new ValueGenerator<LocalDate>() {
            public LocalDate generate() {
                return null;  // Change to generate random instance
            }
        }, LocalDate.class);
        MatcherAssert.assertThat(CatsPhasesEntity.class,
                allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
    }

    @Test
    void DoctorsEntity() {
        MatcherAssert.assertThat(DoctorsEntity.class,
                allOf(hasValidBeanConstructor(), hasValidBeanEquals(), hasValidGettersAndSetters(),
                        hasValidBeanHashCode()));
    }

//    @Test
//    void BookingInformationsEntity() {
//        MatcherAssert.assertThat(BookingInformationsEntity.class,
//                allOf(hasValidBeanConstructor(), hasValidBeanEquals(), hasValidGettersAndSetters(),
//                        hasValidBeanHashCode()));
//    }

    @Test
    void CatsAcademicRankEntity() {
        MatcherAssert.assertThat(CatsAcademicRankEntity.class,
                allOf(hasValidBeanConstructor(), hasValidBeanEquals(), hasValidGettersAndSetters(),
                        hasValidBeanHashCode()));
    }

    @Test
    void CatsDegreeEntity() {
        MatcherAssert.assertThat(CatsDegreeEntity.class,
                allOf(hasValidBeanConstructor(), hasValidBeanEquals(), hasValidGettersAndSetters(),
                        hasValidBeanHashCode()));
    }

    @Test
    void CatsSpecialistsEntity() {
        MatcherAssert.assertThat(CatsSpecialistsEntity.class,
                allOf(hasValidBeanConstructor(), hasValidBeanEquals(), hasValidGettersAndSetters(),
                        hasValidBeanHashCode()));
    }

    @Test
    void CatsTimeslotsEntity() {
        MatcherAssert.assertThat(CatsTimeslotsEntity.class,
                allOf(hasValidBeanConstructor(), hasValidBeanEquals(), hasValidGettersAndSetters(),
                        hasValidBeanHashCode()));
    }

    @Test
    void DoctorsCalendarsEntity() {
        BeanMatchers.registerValueGenerator(new ValueGenerator<LocalTime>() {
            public LocalTime generate() {
                return null;  // Change to generate random instance
            }
        }, LocalTime.class);
        MatcherAssert.assertThat(DoctorsCalendarsEntity.class,
                allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
    }

    @Test
    void CatsGendersEntity() {
        MatcherAssert.assertThat(CatsGendersEntity.class,
                allOf(hasValidBeanConstructor(), hasValidBeanEquals(), hasValidGettersAndSetters(),
                        hasValidBeanHashCode()));
    }

    @Test
    void DoctorsSpecialistsEntity() {
        MatcherAssert.assertThat(DoctorsSpecialistsEntity.class,
                allOf(hasValidBeanConstructor(), hasValidBeanEquals(), hasValidGettersAndSetters(),
                        hasValidBeanHashCode()));
    }

    @Test
    void OtpEntity() {
        MatcherAssert.assertThat(OtpEntity.class,
                allOf(hasValidBeanConstructor(), hasValidBeanEquals(), hasValidGettersAndSetters(),
                        hasValidBeanHashCode()));
    }

    @Test
    void OtpIdentify() {
        MatcherAssert.assertThat(OtpIdentify.class,
                allOf(hasValidBeanConstructor(), hasValidBeanEquals(), hasValidGettersAndSetters(),
                        hasValidBeanHashCode()));
    }
}