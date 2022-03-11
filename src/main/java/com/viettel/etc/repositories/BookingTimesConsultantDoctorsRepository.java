package com.viettel.etc.repositories;

import com.viettel.etc.dto.BookingTimesConsultantDoctorsDTO;
import com.viettel.etc.dto.DoctorsCalendarBookingTimesConsultantDTO;

import java.util.List;
import java.util.Optional;

/**
 * Autogen class Repository Interface:
 *
 * @author toolGen
 * @date Mon Sep 14 17:53:52 ICT 2020
 */
public interface BookingTimesConsultantDoctorsRepository {

    public List<BookingTimesConsultantDoctorsDTO> getBookingTimesConsultantDoctors(BookingTimesConsultantDoctorsDTO itemParamsEntity);

    public BookingTimesConsultantDoctorsDTO getMaxNumberBookingDoctor(BookingTimesConsultantDoctorsDTO itemParamsEntity);

    List<DoctorsCalendarBookingTimesConsultantDTO> getDoctorsCalendarBookingTimesConsultants(DoctorsCalendarBookingTimesConsultantDTO dataParams);

    int countBookingInfo(Integer doctorId, String registerTime, java.sql.Date registerDate);

    Optional<DoctorsCalendarBookingTimesConsultantDTO> getMaxNumberBookingDoctorByDoctorId(DoctorsCalendarBookingTimesConsultantDTO dataParams);

}
