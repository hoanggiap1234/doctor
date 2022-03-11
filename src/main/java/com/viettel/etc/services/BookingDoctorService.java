package com.viettel.etc.services;

import com.viettel.etc.dto.BookingDoctorRequestDTO;
import com.viettel.etc.utils.TeleCareException;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;

import java.text.ParseException;

public interface BookingDoctorService {

    ResultSelectEntity getBookingDaysDoctors(BookingDoctorRequestDTO dataParams);

    ResultSelectEntity getBookingHoursDoctors(BookingDoctorRequestDTO dataParams) throws TeleCareException, ParseException;
}
