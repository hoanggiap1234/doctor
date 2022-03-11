package com.viettel.etc.controllers;

import com.viettel.etc.dto.RateConsultantDoctorDTO;
import com.viettel.etc.services.RateConsultantDoctorService;
import com.viettel.etc.xlibrary.core.constants.FunctionCommon;
import com.viettel.etc.xlibrary.core.entities.UserSystemEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.viettel.etc.utils.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.Optional;

/**
 * Autogen class:
 *
 * @author ToolGen
 * @date Tue Sep 15 23:10:41 ICT 2020
 */
@RestController
public class RateConsultantDoctorController {
    private static final Logger LOGGER = Logger.getLogger(RateConsultantDoctorController.class);

    @Autowired
    private RateConsultantDoctorService rateConsultantDoctorService;


    /**
     * Danh gia tu van
     *
     * @param authentication: thong tin nguoi dung
     * @param dataParams      params client
     * @return
     */
    @PostMapping(value = "/{patientId}/rate-consultant-doctor", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> rateConsultantDoctor(@AuthenticationPrincipal Authentication authentication,
                                                       @PathVariable Integer patientId,
                                                       @RequestBody RateConsultantDoctorDTO dataParams,
                                                       @RequestHeader Optional<String>lang) {
        /*
        ==========================================================
        authenEntity: user info and role
        dataParams: danh sach bien client co the truyen len
        ==========================================================
        */
        dataParams.setPatientId(patientId);
        Object resultObj = null;
        try {
            resultObj = rateConsultantDoctorService.rateConsultantDoctor(dataParams, authentication);
        } catch (TeleCareException e) {
            LOGGER.info(e);
            return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(FunctionCommon.responseToClient(resultObj), HttpStatus.OK);
    }
}