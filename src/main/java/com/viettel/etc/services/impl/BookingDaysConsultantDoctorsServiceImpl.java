package com.viettel.etc.services.impl;

import com.viettel.etc.dto.BookingDaysConsultantDoctorsDTO;
import com.viettel.etc.repositories.BookingDaysConsultantDoctorsRepository;
import com.viettel.etc.services.BookingDaysConsultantDoctorsService;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Autogen class:
 *
 * @author ToolGen
 * @date Mon Sep 14 10:35:04 ICT 2020
 */
@Service
public class BookingDaysConsultantDoctorsServiceImpl implements BookingDaysConsultantDoctorsService {

    @Autowired
    private BookingDaysConsultantDoctorsRepository bookingDaysConsultantDoctorsRepository;


    /**
     * Danh sach cac ngay co the dat tu van voi bac si
     *
     * @param itemParamsEntity params client
     * @return
     */
    @Override
    public ResultSelectEntity getBookingDaysConsultantDoctors(BookingDaysConsultantDoctorsDTO itemParamsEntity) {
        /*
        ==========================================================
        itemParamsEntity: params nguoi dung truyen len
        ==========================================================
        */
        ResultSelectEntity resultSelectEntity = bookingDaysConsultantDoctorsRepository.getBookingDaysConsultantDoctors(itemParamsEntity);
        return resultSelectEntity;
    }
}
