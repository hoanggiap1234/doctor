package com.viettel.etc.controllers;

import com.viettel.etc.dto.BookingTimesConsultantDoctorsDTO;
import com.viettel.etc.dto.DoctorsCalendarBookingTimesConsultantDTO;
import com.viettel.etc.services.BookingTimesConsultantDoctorsService;
import com.viettel.etc.utils.Constants;
import com.viettel.etc.utils.FnCommon;
import com.viettel.etc.utils.TeleCareException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.apache.log4j.Logger;

import java.util.Optional;

/**
 * Autogen class:
 *
 * @author ToolGen
 * @date Mon Sep 14 17:53:51 ICT 2020
 */
@RestController
public class BookingTimesConsultantDoctorsController {
	@Autowired
	private BookingTimesConsultantDoctorsService bookingTimesConsultantDoctorsService;

	private static final Logger LOGGER = Logger.getLogger(BookingTimesConsultantDoctorsController.class);

	/**
	 * KHONG DUNG NUA, booking-times-consultant-doctors/doctor-calendar
	 * Danh sach thoi gian co the dat tu van cua bac si
	 *
	 * @param authentication: thong tin nguoi dung
	 * @param dataParams      params client
	 * @return
	 */
	@GetMapping(value = "booking-times-consultant-doctors", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getBookingTimesConsultantDoctors(@AuthenticationPrincipal Authentication authentication,
																   BookingTimesConsultantDoctorsDTO dataParams,
																   @RequestHeader("lang")Optional<String>lang) {
        /*
        ==========================================================
        authenEntity: user info and role
        dataParams: danh sach bien client co the truyen len
        ==========================================================
        */
		String language = lang.isPresent() ? lang.get() : Constants.VIETNAM_CODE;
		dataParams.setLanguage(language);
		Object resultObj = null;
		try {
			resultObj = bookingTimesConsultantDoctorsService.getBookingTimesConsultantDoctors(dataParams);
		} catch (TeleCareException e) {
			LOGGER.info(e);
			e.printStackTrace();
		}
		return new ResponseEntity<>(FnCommon.responseToClient(resultObj), HttpStatus.OK);
	}

	/**
	 * Danh sach thoi gian co the dat tu van cua bac si
	 *
	 * @param authentication: thong tin nguoi dung
	 * @param dataParams      params client
	 * @return
	 */
	@GetMapping(value = "booking-times-consultant-doctors/doctor-calendar", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getBookingTimesConsultantDoctors(@AuthenticationPrincipal Authentication authentication,
																   DoctorsCalendarBookingTimesConsultantDTO dataParams,
																   @RequestHeader("lang")Optional<String>lang) {
        /*
        ==========================================================
        authenEntity: user info and role
        dataParams: danh sach bien client co the truyen len
        ==========================================================
        */
		String language = lang.isPresent() ? lang.get() : Constants.VIETNAM_CODE;
		dataParams.setLanguage(language);
		try {
			Object resultObj = bookingTimesConsultantDoctorsService.getDoctorsCalendarBookingTimesConsultant(dataParams);
			return new ResponseEntity<>(FnCommon.responseToClient(resultObj), HttpStatus.OK);
		} catch (TeleCareException e) {
			return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.OK);
		}
	}

}
