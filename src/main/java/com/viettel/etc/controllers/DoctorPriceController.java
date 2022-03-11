package com.viettel.etc.controllers;

import com.viettel.etc.dto.DoctorPriceDTO;
import com.viettel.etc.services.DoctorPriceService;
import com.viettel.etc.utils.Constants;
import com.viettel.etc.utils.ErrorApp;
import com.viettel.etc.utils.FnCommon;
import com.viettel.etc.utils.TeleCareException;
import com.viettel.etc.xlibrary.core.constants.FunctionCommon;
import com.viettel.etc.xlibrary.core.entities.CoreErrorApp;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

/**
 * Autogen class:
 *
 * @author ToolGen
 * @date Mon Sep 07 14:45:01 ICT 2020
 */
@RestController
public class DoctorPriceController {

	private static final Logger LOGGER = Logger.getLogger(DoctorPriceController.class);

	@Autowired
	private DoctorPriceService doctorPriceService;

	/**
	 * lay danh sach gia kham/ tu van cua bac si theo moc thoi gian
	 *
	 * @param authentication: thong tin nguoi dung
	 * @param dataParams      params client
	 * @return
	 */
	@GetMapping(value = "/prices/{phaseId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getDoctorPrices(@AuthenticationPrincipal Authentication authentication,
												  DoctorPriceDTO dataParams, @PathVariable Integer phaseId,
												  @RequestHeader("lang")Optional<String>lang) {
        /*
        ==========================================================
        authenEntity: user info and role
        dataParams: danh sach bien client co the truyen len
        ==========================================================
        */
		String language = lang.orElse(Constants.VIETNAM_CODE);
		dataParams.setLanguage(language);
		try {
			dataParams.setPhaseId(phaseId);
			Object resultObj = doctorPriceService.getDoctorPrices(dataParams, authentication);
			return new ResponseEntity<>(FunctionCommon.responseToClient(resultObj), HttpStatus.OK);
		} catch (TeleCareException e) {
			return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.info(e);
			e.printStackTrace();
			return new ResponseEntity<>(FnCommon.responseToClient(ErrorApp.ERR_UNKNOW), HttpStatus.OK);
		}
	}

	/**
	 * tao danh sach gia kham, tu van cua bac si
	 *
	 * @param authentication: thong tin nguoi dung
	 * @param dataParams      params client
	 * @return
	 */
	@PostMapping(value = "/prices/{phaseId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> createDoctorPrices(@AuthenticationPrincipal Authentication authentication,
													 @Valid @RequestBody List<DoctorPriceDTO> dataParams, @PathVariable Integer phaseId,
													 @RequestHeader("lang")Optional<String>lang) {
        /*
        ==========================================================
        authenEntity: user info and role
        dataParams: danh sach bien client co the truyen len
        ==========================================================
        */
		String language = lang.orElse(Constants.VIETNAM_CODE);
		try {
			doctorPriceService.createDoctorPrices(dataParams, phaseId, authentication, language);

			return new ResponseEntity<>(FnCommon.responseToClient(CoreErrorApp.SUCCESS), HttpStatus.OK);
		} catch (TeleCareException e) {
			return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOGGER.info(e);
			e.printStackTrace();
			return new ResponseEntity<>(FnCommon.responseToClient(ErrorApp.ERR_UNKNOW), HttpStatus.BAD_REQUEST);
		}
	}


	/**
	 * Danh sach Doctor Prices
	 *
	 * @param authentication: thong tin nguoi dung
	 * @param dataParams      params client
	 * @return
	 */
	@GetMapping(value = "/{doctorId}/prices/{phaseId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getDoctorPricesByPhaseIdAndDoctorId(@AuthenticationPrincipal Authentication authentication, DoctorPriceDTO dataParams,
																	  @PathVariable Integer phaseId, @PathVariable Integer doctorId,
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
			dataParams.setPhaseId(phaseId);
			dataParams.setDoctorId(doctorId);
			Object resultObj = doctorPriceService.getDoctorPricesByPhaseIdAndDoctorId(dataParams, authentication);
			return new ResponseEntity<>(FunctionCommon.responseToClient(resultObj), HttpStatus.OK);
		} catch (TeleCareException e) {
			return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.info(e);
			e.printStackTrace();
			return new ResponseEntity<>(FnCommon.responseToClient(ErrorApp.ERR_UNKNOW), HttpStatus.OK);
		}
	}

	/**
	 * tao danh sach gia kham, tu van cua bac si
	 *
	 * @param authentication: thong tin nguoi dung
	 * @param dataParams:     params client
	 * @param phaseId:        phaseId
	 * @param doctorId:       doctorId
	 * @return
	 */
	//TODO update or delete theo logic mới (1 phase có n bản ghi)
	@PostMapping(value = "/{doctorId}/prices/{phaseId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateDoctorPrices(@AuthenticationPrincipal Authentication authentication, @RequestBody DoctorPriceDTO dataParams,
													 @PathVariable Integer phaseId, @PathVariable Integer doctorId, @RequestHeader("lang")Optional<String>lang) {
        /*
        ==========================================================
        authenEntity: user info and role
        dataParams: danh sach bien client co the truyen len
        ==========================================================
        */
		LOGGER.debug(MessageFormat.format("update doctor prices with dataParams: {0} and phaseId: {1} and doctorId: {2}", dataParams, phaseId, doctorId));
		String language = lang.isPresent() ? lang.get() : Constants.VIETNAM_CODE;
		dataParams.setLanguage(language);
		dataParams.setPhaseId(phaseId);
		dataParams.setDoctorId(doctorId);
		Object resultObj;
		try {
			resultObj = doctorPriceService.updateDoctorPrices(dataParams, authentication);
			return new ResponseEntity<>(FnCommon.responseToClient(resultObj), HttpStatus.OK);
		} catch (TeleCareException e) {
			return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.info(e);
			return new ResponseEntity<>(FnCommon.responseToClient(ErrorApp.ERR_UNKNOW), HttpStatus.OK);
		}
	}

}
