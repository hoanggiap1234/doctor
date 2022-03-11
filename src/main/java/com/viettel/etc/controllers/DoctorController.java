package com.viettel.etc.controllers;

import com.viettel.etc.dto.*;
import com.viettel.etc.services.DoctorService;
import com.viettel.etc.services.MessageService;
import com.viettel.etc.services.tables.DoctorsServiceJPA;
import com.viettel.etc.utils.*;
import com.viettel.etc.xlibrary.core.constants.FunctionCommon;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Autogen class: Lop thong tin cua bac si
 *
 * @author ToolGen
 * @date Wed Aug 19 14:21:50 ICT 2020
 */
@RestController
public class DoctorController {

	@Autowired
	private DoctorService doctorService;

	@Autowired
	private DoctorsServiceJPA doctorServiceJPA;

	@Autowired
	private MessageService messageService;

	private static final Logger LOGGER = Logger.getLogger(DoctorController.class);

	/**
	 * Create doctor
	 *
	 * @param authentication
	 * @param dataParams
	 * @return
	 * @throws TeleCareException
	 */
	@PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> createDoctor(
			@AuthenticationPrincipal Authentication authentication,
			@Valid @RequestBody DoctorDTO dataParams, @RequestHeader("lang")Optional<String>lang) {
        /*
        ==========================================================
        authenEntity: user info and role
        dataParams: danh sach bien client co the truyen len
        ==========================================================
        */
		String language = lang.isPresent() ? lang.get() : Constants.VIETNAM_CODE;
		dataParams.setLanguage(language);
		Object resultObj;

		try {
			resultObj = doctorService.createDoctor(dataParams, authentication);
		} catch (TeleCareException e) {
			return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOGGER.info(e);
			e.printStackTrace();
			return new ResponseEntity<>(FnCommon.responseToClient(ErrorApp.ERR_UNKNOW), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(FnCommon.responseToClient(resultObj), HttpStatus.OK);
	}

	/**
	 * Create doctor
	 *
	 * @param authentication
	 * @param dataParams
	 * @return
	 * @throws TeleCareException
	 */
	@DeleteMapping(value = "/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> deleteDoctor(
			@AuthenticationPrincipal Authentication authentication,
			DoctorDTO dataParams, @PathVariable int doctorId, @RequestHeader("lang")Optional<String>lang) throws TeleCareException {
        /*
        ==========================================================
        authenEntity: user info and role
        dataParams: danh sach bien client co the truyen len
        ==========================================================
        */
		String language = lang.isPresent() ? lang.get() : Constants.VIETNAM_CODE;
		dataParams.setLanguage(language);
		dataParams.setDoctorId(doctorId);
		try {
			doctorService.deleteDoctor(dataParams, authentication);
		} catch (TeleCareException | IOException e) {
			return new ResponseEntity<>(FunctionCommon.responseToClient(e), HttpStatus.OK);
		}

		return new ResponseEntity<>(FunctionCommon.responseToClient(ErrorApp.MESAGE_DELETE_SUCCESS),
				HttpStatus.OK);
	}

	/**
	 * @param authentication
	 * @param dataParams
	 * @return
	 * @throws TeleCareException
	 */
	@PutMapping(value = "/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateDoctor(
			@AuthenticationPrincipal Authentication authentication,
			@PathVariable Integer doctorId,
			@RequestBody DoctorDTO dataParams, @RequestHeader("lang") Optional<String>lang) throws TeleCareException {
        /*
        ==========================================================
        authenEntity: user info and role
        dataParams: danh sach bien client co the truyen len
        ==========================================================
        */
		String language = lang.isPresent() ? lang.get() : Constants.VIETNAM_CODE;
		dataParams.setLanguage(language);
		dataParams.setDoctorId(doctorId);
		Object resultObj;

		try {
			resultObj = doctorService.updateDoctor(dataParams, authentication);
		} catch (TeleCareException e) {
			return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.info(e);
			return new ResponseEntity<>(FnCommon.responseToClient(ErrorApp.ERR_UNKNOW), HttpStatus.OK);
		}

		return new ResponseEntity<>(FunctionCommon.responseToClient(resultObj), HttpStatus.OK);
	}

	/**
	 * @param authentication: thong tin nguoi dung
	 * @param dataParams      params client
	 * @return
	 */
	@GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getDoctors(@AuthenticationPrincipal Authentication authentication,
											 DoctorDTO dataParams, @RequestHeader("lang")Optional<String> lang) {

        /*
        ==========================================================
        authenEntity: user info and role
        dataParams: danh sach bien client co the truyen len
        ==========================================================
        */
		String language = lang.isPresent() ? lang.get() : Constants.VIETNAM_CODE;
		dataParams.setLanguage(language);
		try {
			Object resultObj = doctorService.getDoctors(dataParams, authentication);
			return new ResponseEntity<>(FunctionCommon.responseToClient(resultObj), HttpStatus.OK);
		} catch (TeleCareException e) {
			return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.info(e);
			return new ResponseEntity<>(FnCommon.responseToClient(ErrorApp.ERR_UNKNOW), HttpStatus.OK);
		}
	}

	/**
	 * @param dataParams params client
	 * @return
	 */
	@GetMapping(value = "/public", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getDoctorsPublic(DoctorDTO dataParams, @RequestHeader("lang")Optional<String> lang) {

        /*
        ==========================================================
        authenEntity: user info and role
        dataParams: danh sach bien client co the truyen len
        ==========================================================
        */
		String language = lang.isPresent() ? lang.get() : Constants.VIETNAM_CODE;
		dataParams.setLanguage(language);
		try {
			Object resultObj = doctorService.getDoctorsPublic(dataParams);
			return new ResponseEntity<>(FunctionCommon.responseToClient(resultObj), HttpStatus.OK);
		} catch (TeleCareException e) {
			return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.OK);
		}
	}

	/**
	 * @param dataParams params client
	 * @return
	 */
	@GetMapping(value = "/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getDoctorInfo(@PathVariable Integer doctorId,
												DoctorDTO dataParams, @RequestHeader("lang")Optional<String> lang) {
		dataParams.setDoctorId(doctorId);
		Object resultObj = null;
		String language = lang.isPresent() ? lang.get() : Constants.VIETNAM_CODE;
		dataParams.setLanguage(language);
		try {
			resultObj = doctorService.getDoctorInfo(dataParams, doctorId);
		} catch (TeleCareException e) {
			return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(FunctionCommon.responseToClient(resultObj), HttpStatus.OK);
	}

	/**
	 * @param authentication: thong tin nguoi dung
	 * @param dataParams      params client
	 * @return
	 */
	@GetMapping(value = "/top-doctors", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getTopDoctors(
			@AuthenticationPrincipal Authentication authentication,
			DoctorDTO dataParams, @RequestHeader("lang")Optional<String> lang) {
        /*
        ==========================================================
        authenEntity: user info and role
        dataParams: danh sach bien client co the truyen len
        ==========================================================
        */
		String language = lang.isPresent() ? lang.get() : Constants.VIETNAM_CODE;
		dataParams.setLanguage(language);
		Object resultObj = doctorService.getTopDoctors(dataParams);
		return new ResponseEntity<>(FunctionCommon.responseToClient(resultObj), HttpStatus.OK);
	}

	/**
	 * api lay thong tin cua bac sy
	 *
	 * @param authentication: thong tin nguoi dung
	 * @param dataParams      params client
	 * @return
	 */
	@GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getDoctorInfo(
			@AuthenticationPrincipal Authentication authentication,
			DoctorDTO dataParams, @RequestHeader("lang")Optional<String> lang) {
		Object resultObj = null;
		String language = lang.orElse(Constants.VIETNAM_CODE);
		dataParams.setLanguage(language);
		try {
			resultObj = doctorService.getDoctorByKeycloakUserId(dataParams, authentication);
		} catch (TeleCareException e) {
			return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(FunctionCommon.responseToClient(resultObj), HttpStatus.OK);
	}

	/**
	 * export excel list doctor
	 *
	 * @param authentication: thong tin nguoi dung
	 * @param dataParams      params client
	 * @return
	 */
	@PostMapping(value = "/export-data/excel", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> exportBookingHistoriesByDoctor(@AuthenticationPrincipal Authentication authentication,
																 @RequestBody DoctorDTO dataParams, @RequestHeader("lang")Optional<String>lang) {
		String language = lang.isPresent() ? lang.get() : Constants.VIETNAM_CODE;
		dataParams.setLanguage(language);
		List<DoctorDTO> listDataExport;
		dataParams.setStartrecord(0);
		dataParams.setPagesize(Integer.MAX_VALUE);

		ResultSelectEntity resultSelectEntity = null;
		try {
			resultSelectEntity = (ResultSelectEntity) doctorService.getDoctorsExcel(dataParams, authentication);
		} catch (TeleCareException e) {
			return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.OK);
		}
		listDataExport = (List<DoctorDTO>) resultSelectEntity.getListData();

//		if (CollectionUtils.isEmpty(listDataExport)) {
//			return new ResponseEntity<>(FunctionCommon.responseToClient("FILE NOT FOUND"), HttpStatus.NOT_FOUND);
//		}

		Optional<Path> pathOpt = doctorService.convertListDataDoctorToExcel(listDataExport, dataParams);
		ResponseEntity<Object> response = ExcelCommon.getExportExcelResponseEntity(pathOpt);
		return response;
	}

	/**
	 * Delete doctor
	 *
	 * @param dataParams
	 * @return
	 * @throws TeleCareException
	 */
	@PutMapping(value = "/reset-password", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> resetPassword(
			@Valid @RequestBody RessetPasswordDTO dataParams, @RequestHeader("lang")Optional<String>lang) throws TeleCareException {
        /*
        ==========================================================
        authenEntity: user info and role
        dataParams: danh sach bien client co the truyen len
        ==========================================================
        */
		String language = lang.isPresent() ? lang.get() : Constants.VIETNAM_CODE;
		dataParams.setLanguage(language);
		try {
			doctorService.ressetPassword(dataParams);
			return new ResponseEntity<>(FunctionCommon.responseToClient(true), HttpStatus.OK);
		} catch (TeleCareException e) {
			return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.info(e);
			return new ResponseEntity<>(FnCommon.responseToClient(ErrorApp.ERR_UNKNOW), HttpStatus.OK);
		}
	}

	@PutMapping(value = "/{doctorId}/change-password", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> changePassword(@AuthenticationPrincipal Authentication authentication,
												 @PathVariable Integer doctorId,
												 @RequestBody RequestChangePasswordDTO dataParams,
												 @RequestHeader("lang")Optional<String>lang) {
//		Object resultObj = null;
		String language = lang.isPresent() ? lang.get() : Constants.VIETNAM_CODE;
		dataParams.setLanguage(language);
		try {
			doctorServiceJPA.changePassword(dataParams, authentication);
		} catch (TeleCareException e) {
			return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOGGER.info(e);
			return new ResponseEntity<>(FnCommon.responseToClient(ErrorApp.ERR_UNKNOW), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(FnCommon.responseToClient(ErrorApp.CHANGE_PASSWORD_SUCCESS), HttpStatus.OK);
	}

	// change password for 3rd party
	@PutMapping(value = "/change-password", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> changePassword(@AuthenticationPrincipal Authentication authentication,
												 @RequestBody RequestChangePasswordDTO dataParams,
												 @RequestHeader("lang")Optional<String>lang) {
//		Object resultObj = null;
		String language = lang.isPresent() ? lang.get() : Constants.VIETNAM_CODE;
		dataParams.setLanguage(language);
		try {
			doctorServiceJPA.changePassword3rdParty(dataParams, authentication);
		} catch (TeleCareException e) {
			return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOGGER.info(e);
			return new ResponseEntity<>(FnCommon.responseToClient(ErrorApp.ERR_UNKNOW), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(FnCommon.responseToClient(ErrorApp.CHANGE_PASSWORD_SUCCESS), HttpStatus.OK);
	}

	@GetMapping(value = "/homepage-web", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getDoctorHomepageWeb (@AuthenticationPrincipal Authentication authentication,
													 @RequestHeader("lang")Optional<String>lang) {
		String language = lang.orElse(Constants.VIETNAM_CODE);
		Object resultObj = null;
		try {
			resultObj = doctorService.getDoctorHomepageWeb(authentication, language);
		} catch (TeleCareException e) {
			return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOGGER.info(e);
			return new ResponseEntity<>(FnCommon.responseToClient(ErrorApp.ERR_UNKNOW), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(FnCommon.responseToClient(resultObj), HttpStatus.OK);
	}

	@PostMapping(value = "/import-doctor", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> importDoctor(
			@AuthenticationPrincipal Authentication authentication,
			@Valid @RequestBody List<DoctorDTO> dataParams, @RequestHeader("lang")Optional<String>lang) {
        /*
        ==========================================================
        authenEntity: user info and role
        dataParams: danh sach bien client co the truyen len
        ==========================================================
        */
//		String language = lang.orElse(Constants.VIETNAM_CODE);

		Integer totalImport = 0, totalSuccess = 0;
		List<Integer> doctorsFail = new ArrayList<>();
		DoctorImportResponseDTO result = new DoctorImportResponseDTO();
		for(DoctorDTO dataParam : dataParams)  {
			try {
				totalImport++;
				doctorService.importDoctor(dataParam, authentication);
				totalSuccess++;
			} catch (Exception e) {
				LOGGER.info(e);
				e.printStackTrace();
				doctorsFail.add(totalImport);
				System.out.println("======= Khong tao duoc tao bac si " + totalImport + " ========");
			}
		}
		result.setTotalImport(totalImport);
		result.setTotalSuccess(totalSuccess);
		result.setDoctorsFail(doctorsFail.toString());
		return new ResponseEntity<>(FunctionCommon.responseToClient(result),HttpStatus.OK);
	}


	@PostMapping(value = "/upload-file-import", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> uploadFileImport(
			@AuthenticationPrincipal Authentication authentication,
			@RequestParam("file") MultipartFile file, @RequestHeader("lang")Optional<String>lang) {

		String language = lang.isPresent() ? lang.get() : Constants.VIETNAM_CODE;
		Object resultObj = null;
		try {
			resultObj = doctorService.uploadFileImport(authentication, file, language);
			return new ResponseEntity<>(FunctionCommon.responseToClient(resultObj),HttpStatus.OK);
		} catch (TeleCareException e){
			e.printStackTrace();
			return new ResponseEntity<>(FnCommon.responseToClient(e),HttpStatus.BAD_REQUEST);
		} catch (Exception e){
			LOGGER.info(e);
			e.printStackTrace();
			return new ResponseEntity<>(FunctionCommon.responseToClient(ErrorApp.ERR_UNKNOW),HttpStatus.BAD_REQUEST);
		}
	}


	@PostMapping(value = "/import-doctor-file", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> importDoctorFile(
			@AuthenticationPrincipal Authentication authentication,
			@RequestBody FileImportDTO fileImportDTO, @RequestHeader("lang")Optional<String>lang) {

		String language = lang.isPresent() ? lang.get() : Constants.VIETNAM_CODE;
		fileImportDTO.setLanguage(language);
		Object resultObj = null;
		try {
			resultObj = doctorService.importDoctorFile(authentication, fileImportDTO);
			return new ResponseEntity<>(FnCommon.responseToClient(resultObj),HttpStatus.OK);
		} catch (TeleCareException e){
			e.printStackTrace();
			return new ResponseEntity<>(FnCommon.responseToClient(e),HttpStatus.BAD_REQUEST);
		} catch (Exception e){
			LOGGER.info(e);
			e.printStackTrace();
			return new ResponseEntity<>(FunctionCommon.responseToClient(ErrorApp.ERR_UNKNOW),HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> createDoctorAndVideoCall(
			@AuthenticationPrincipal Authentication authentication,
			@Valid @RequestBody DoctorDTO dataParams, @RequestHeader("lang")Optional<String>lang) {
		String language = lang.isPresent() ? lang.get() : Constants.VIETNAM_CODE;
		dataParams.setLanguage(language);
		Object resultObj;

		try {
			resultObj = doctorService.createDoctorAndVideoCall(dataParams, authentication);
		} catch (TeleCareException e) {
			return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOGGER.info(e);
			e.printStackTrace();
			return new ResponseEntity<>(FnCommon.responseToClient(ErrorApp.ERR_UNKNOW), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(FnCommon.responseToClient(resultObj), HttpStatus.OK);
	}
}
