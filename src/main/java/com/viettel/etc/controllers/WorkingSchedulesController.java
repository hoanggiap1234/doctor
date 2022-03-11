package com.viettel.etc.controllers;

import com.viettel.etc.dto.WorkingSchedulesDTO;
import com.viettel.etc.dto.WorkingSchedulesRequestCreateDTO;
import com.viettel.etc.services.WorkingSchedulesService;
import com.viettel.etc.services.tables.DoctorsCalendarsServiceJPA;
import com.viettel.etc.utils.Constants;
import com.viettel.etc.utils.FnCommon;
import com.viettel.etc.utils.TeleCareException;
import com.viettel.etc.xlibrary.core.constants.FunctionCommon;
import com.viettel.etc.xlibrary.core.entities.CoreErrorApp;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Autogen class:
 *
 * @author ToolGen
 * @date Mon Sep 07 16:47:06 ICT 2020
 */
@RestController
public class WorkingSchedulesController {

	@Autowired
	private WorkingSchedulesService workingSchedulesService;

	@Autowired
	private DoctorsCalendarsServiceJPA doctorsCalendarsServiceJPA;

	/**
	 * Lich lam viec cua cac bac si
	 *
	 * @param authentication: thong tin nguoi dung
	 * @param dataParams      params client
	 * @return
	 */
	@GetMapping(value = "working-schedules", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getWorkingSchedules(@AuthenticationPrincipal Authentication authentication,
													  WorkingSchedulesDTO dataParams,
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
			Object resultObj = workingSchedulesService.getWorkingSchedules(dataParams, authentication);
			return new ResponseEntity<>(FnCommon.responseToClient(resultObj), HttpStatus.OK);
		} catch (TeleCareException e) {
			return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.OK);
		}
	}

	@PostMapping(value = "working-schedules", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> createWorkingSchedules(@AuthenticationPrincipal Authentication authentication,
														 @RequestBody @Valid WorkingSchedulesRequestCreateDTO dataParams,
														 @RequestHeader("lang")Optional<String>lang) {
		String language = lang.isPresent() ? lang.get() : Constants.VIETNAM_CODE;
		dataParams.setLanguage(language);
		try {
			doctorsCalendarsServiceJPA.save(dataParams, authentication);
		} catch (TeleCareException e) {
			return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(FnCommon.responseToClient(CoreErrorApp.SUCCESS, null), HttpStatus.OK);
	}


	/**
	 * Lich lam viec cua cac bac si
	 *
	 * @param authentication: thong tin nguoi dung
	 * @param dataParams      params client
	 * @return
	 */
	@GetMapping(value = "{doctorId}/detail-working-schedule", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getDetailWorkingSchedules(@AuthenticationPrincipal Authentication authentication,
															WorkingSchedulesDTO dataParams, @PathVariable Integer doctorId,
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
			dataParams.setDoctorId(doctorId);
			ResultSelectEntity resultData = workingSchedulesService.getWorkingSchedules(dataParams, authentication);
			if (!CollectionUtils.isEmpty(resultData.getListData())) {
				return new ResponseEntity<>(FunctionCommon.responseToClient(resultData.getListData().get(0)), HttpStatus.OK);
			}
			return new ResponseEntity<>(FunctionCommon.responseToClient(CoreErrorApp.SUCCESS, null), HttpStatus.OK);
		} catch (TeleCareException e) {
			return new ResponseEntity<>(FunctionCommon.responseToClient(e), HttpStatus.OK);
		}
	}

	/**
	 * Lich lam viec cua cac bac si
	 *
	 * @param authentication: thong tin nguoi dung
	 * @param dataParams      params client
	 * @return
	 */
	@PostMapping(value = "{doctorId}/detail-working-schedule", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateWorkingSchedules(@AuthenticationPrincipal Authentication authentication,
														 @RequestBody @Valid WorkingSchedulesDTO dataParams,
														 @PathVariable Integer doctorId, @RequestHeader("lang")Optional<String>lang) {
		dataParams.setDoctorId(doctorId);
		String language = lang.isPresent() ? lang.get() : Constants.VIETNAM_CODE;
		dataParams.setLanguage(language);
        /*
        ==========================================================
        authenEntity: user info and role
        dataParams: danh sach bien client co the truyen len
        ==========================================================
        */
		try {
			Object resultObj = workingSchedulesService.updateWorkingSchedules(dataParams, authentication);
			return new ResponseEntity<>(FnCommon.responseToClient(resultObj), HttpStatus.OK);
		} catch (TeleCareException e) {
			return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.OK);
		}
	}

	/**
	 * Lich lam viec cua cac bac si
	 *
	 * @param authentication : thong tin nguoi dung
	 * @param dataParams     params client
	 * @return
	 */
	@PostMapping(value = "delete-working-schedule", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> deleteWorkingSchedules(@AuthenticationPrincipal Authentication authentication,
														 @RequestBody @Valid WorkingSchedulesDTO dataParams,@RequestHeader("lang")Optional<String>lang) {
        /*
        ==========================================================
        authenEntity: user info and role
        dataParams: danh sach bien client co the truyen len
        ==========================================================
        */
		String language = lang.isPresent() ? lang.get() : Constants.VIETNAM_CODE;
		dataParams.setLanguage(language);
		try {
			workingSchedulesService.deleteWorkingSchedules(dataParams);
		} catch (TeleCareException e) {
			return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.OK);
		}

		return new ResponseEntity<>(FnCommon.responseToClient(CoreErrorApp.SUCCESS, null), HttpStatus.OK);
	}
}
