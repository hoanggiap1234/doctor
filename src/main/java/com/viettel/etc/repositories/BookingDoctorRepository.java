package com.viettel.etc.repositories;

import com.viettel.etc.dto.BookingDoctorRequestDTO;
import com.viettel.etc.dto.HealthfacilitiesCalendarTimeSlotDTO;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;

import java.text.ParseException;
import java.util.List;

public interface BookingDoctorRepository {

    ResultSelectEntity getBookingDaysDoctors(BookingDoctorRequestDTO dataParams);

    List<HealthfacilitiesCalendarTimeSlotDTO> getBookingHoursDoctors(BookingDoctorRequestDTO dataParams);

    List<HealthfacilitiesCalendarTimeSlotDTO> getBookingHoursDoctor(BookingDoctorRequestDTO dataParams) throws ParseException;
}
