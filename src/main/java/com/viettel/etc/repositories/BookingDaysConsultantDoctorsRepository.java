package com.viettel.etc.repositories;

import com.viettel.etc.dto.BookingDaysConsultantDoctorsDTO;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;

/**
 * Autogen class Repository Interface:
 *
 * @author toolGen
 * @date Mon Sep 14 10:35:04 ICT 2020O
 */
public interface BookingDaysConsultantDoctorsRepository {

    ResultSelectEntity getBookingDaysConsultantDoctors(BookingDaysConsultantDoctorsDTO itemParamsEntity);

}
