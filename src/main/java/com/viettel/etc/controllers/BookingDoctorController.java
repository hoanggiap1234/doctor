package com.viettel.etc.controllers;

import com.viettel.etc.dto.BookingDoctorRequestDTO;
import com.viettel.etc.services.BookingDoctorService;
import com.viettel.etc.utils.Constants;
import com.viettel.etc.utils.TeleCareException;
import com.viettel.etc.xlibrary.core.constants.FunctionCommon;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Optional;

@RestController
public class BookingDoctorController {

    private static final Logger LOGGER = Logger.getLogger(BookingDoctorController.class);

    @Autowired
    private BookingDoctorService bookingDoctorService;


    /**
     * Danh sach cac ngay co the dat kham voi bac si
     *
     * @param authentication: thong tin nguoi dung
     * @param dataParams      params client
     * @return
     */
    @GetMapping(value = "/booking-days-doctors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getBookingDaysDoctors(@AuthenticationPrincipal Authentication authentication, BookingDoctorRequestDTO dataParams) {
        ResultSelectEntity resultSelectEntity = bookingDoctorService.getBookingDaysDoctors(dataParams);
        return new ResponseEntity<>(FunctionCommon.responseToClient(resultSelectEntity), HttpStatus.OK);
    }

    /**
     * Danh sach cac khung gio co the dat kham voi bac si
     *
     * @param dataParams      params client
     * @return
     */
    @GetMapping(value = "/booking-hours-doctors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getBookingHoursDoctors(BookingDoctorRequestDTO dataParams,
                                                         @RequestHeader("lang")Optional<String>lang) throws ParseException {
        String language = lang.orElse(Constants.VIETNAM_CODE);
        dataParams.setLanguage(language);
        try {
            ResultSelectEntity resultSelectEntity = bookingDoctorService.getBookingHoursDoctors(dataParams);
            return new ResponseEntity<>(FunctionCommon.responseToClient(resultSelectEntity), HttpStatus.OK);
        } catch (TeleCareException e) {
            LOGGER.error(e.getMessage(), e);
            return new ResponseEntity<>(FunctionCommon.responseToClient(e.getErrorApp()), HttpStatus.OK);
        }
    }

}
