package com.viettel.etc.controllers;

import com.viettel.etc.dto.BookingDaysConsultantDoctorsDTO;
import com.viettel.etc.services.BookingDaysConsultantDoctorsService;
import com.viettel.etc.xlibrary.core.constants.FunctionCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Autogen class:
 *
 * @author ToolGen
 * @date Mon Sep 14 10:35:03 ICT 2020
 */
@RestController
public class BookingDaysConsultantDoctorsController {

    @Autowired
    private BookingDaysConsultantDoctorsService bookingDaysConsultantDoctorsService;


    /**
     * Danh sach cac ngay co the dat tu van voi bac si
     *
     * @param authentication: thong tin nguoi dung
     * @param dataParams      params client
     * @return
     */
    @GetMapping(value = "booking-days-consultant-doctors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getBookingDaysConsultantDoctors(@AuthenticationPrincipal Authentication authentication,
                                              @Valid BookingDaysConsultantDoctorsDTO dataParams) {
        /*
        ==========================================================
        authenEntity: user info and role
        dataParams: danh sach bien client co the truyen len
        ==========================================================
        */
        Object resultObj = bookingDaysConsultantDoctorsService.getBookingDaysConsultantDoctors(dataParams);
        return new ResponseEntity<>(FunctionCommon.responseToClient(resultObj), HttpStatus.OK);
    }
}
