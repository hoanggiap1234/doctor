package com.viettel.etc.services;

import com.viettel.etc.dto.BookingTimesConsultantDoctorsDTO;
import com.viettel.etc.dto.DoctorsCalendarBookingTimesConsultantDTO;
import com.viettel.etc.utils.TeleCareException;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;

/**
 * Autogen class:
 *
 * @author ToolGen
 * @date Mon Sep 14 17:53:52 ICT 2020
 */
public interface BookingTimesConsultantDoctorsService {

    public Object getBookingTimesConsultantDoctors(BookingTimesConsultantDoctorsDTO itemParamsEntity) throws TeleCareException;

    ResultSelectEntity getDoctorsCalendarBookingTimesConsultant(DoctorsCalendarBookingTimesConsultantDTO dataParams) throws TeleCareException;
}
