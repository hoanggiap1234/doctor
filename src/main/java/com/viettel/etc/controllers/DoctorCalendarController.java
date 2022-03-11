package com.viettel.etc.controllers;

import com.viettel.etc.dto.DoctorsCalendarsDTO;
import com.viettel.etc.services.tables.DoctorsCalendarsServiceJPA;
import com.viettel.etc.utils.Constants;
import com.viettel.etc.utils.ErrorApp;
import com.viettel.etc.utils.FnCommon;
import com.viettel.etc.utils.TeleCareException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Comment
 *
 * @author nguyenhungbk96@gmail.com
 */
@RestController
public class DoctorCalendarController {
	@Autowired
	DoctorsCalendarsServiceJPA doctorsCalendarsServiceJPA;

	private static final Logger LOGGER = Logger.getLogger(DoctorCalendarController.class);
	/**
	 * Calcel doctor calendar
	 *
	 * @param authentication
	 * @return
	 * @throws TeleCareException
	 */
	@PutMapping(value = "cancel-working-schedules-mobile/{calendarId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> cancelWorkingScheduleMobile(@PathVariable int calendarId,
															  @AuthenticationPrincipal Authentication authentication,
															  @RequestHeader("lang")Optional<String>lang) throws TeleCareException {
        /*
        ==========================================================
        authenEntity: user info and role
        dataParams: danh sach bien client co the truyen len
        ==========================================================
        */
		String language = lang.isPresent() ? lang.get() : Constants.VIETNAM_CODE;
		try {
			doctorsCalendarsServiceJPA.cancelWorkingScheduleMobile(authentication, calendarId, language);
		} catch (TeleCareException e) {
			return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.info(e);
			e.printStackTrace();
			return new ResponseEntity<>(FnCommon.responseToClient(ErrorApp.ERR_UNKNOW), HttpStatus.OK);
		}

		return new ResponseEntity<>(FnCommon.responseToClient(true), HttpStatus.OK);
	}

	/**
	 * @param dto
	 * @param authentication
	 */
	@PutMapping(value = "confirm-working-schedules", params = {"fromDate", "toDate", "doctorIds"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> confirmWorkingSchedules(DoctorsCalendarsDTO dto, Authentication authentication,
														  @RequestParam Long fromDate, @RequestParam Long toDate, @RequestParam String doctorIds,
														  @RequestHeader("lang")Optional<String>lang) {
		String language = lang.isPresent() ? lang.get() : Constants.VIETNAM_CODE;
		dto.setLanguage(language);
		try {
			dto.setDoctorIds(doctorIds);
			doctorsCalendarsServiceJPA.confirmWorkingSchedules(dto, authentication);
		} catch (TeleCareException e) {
			return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.info(e);
			e.printStackTrace();
			return new ResponseEntity<>(FnCommon.responseToClient(ErrorApp.ERR_UNKNOW), HttpStatus.OK);
		}

		return new ResponseEntity<>(FnCommon.responseToClient(true), HttpStatus.OK);
	}

	/**
	 * Dalete doctor calendar
	 *
	 * @param authentication
	 * @return
	 * @throws TeleCareException
	 */
	@DeleteMapping(value = "delete-working-schedules-mobile/{calendarId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> deleteWorkingScheduleMobile(@PathVariable int calendarId,
															  @AuthenticationPrincipal Authentication authentication,
															  @RequestHeader("lang")Optional<String>lang) throws TeleCareException {
        /*
        ==========================================================
        authenEntity: user info and role
        dataParams: danh sach bien client co the truyen len
        ==========================================================
        */
		String language = lang.isPresent() ? lang.get() : Constants.VIETNAM_CODE;
		try {
			doctorsCalendarsServiceJPA.deleteWorkingScheduleMobile(authentication, calendarId, language);
		} catch (TeleCareException e) {
			return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.info(e);
			e.printStackTrace();
			return new ResponseEntity<>(FnCommon.responseToClient(ErrorApp.ERR_UNKNOW), HttpStatus.OK);
		}

		return new ResponseEntity<>(FnCommon.responseToClient(true), HttpStatus.OK);
	}
}
