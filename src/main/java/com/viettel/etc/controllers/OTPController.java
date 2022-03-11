package com.viettel.etc.controllers;

import com.viettel.etc.services.OtpService;
import com.viettel.etc.services.tables.OtpServiceJPA;
import com.viettel.etc.utils.Constants;
import com.viettel.etc.utils.FnCommon;
import com.viettel.etc.utils.TeleCareException;
import com.viettel.etc.xlibrary.core.constants.FunctionCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;


/**
 * Autogen class: Lop gui ma OTP
 *
 * @author ToolGen
 */
@RestController
public class OTPController {

    @Autowired
    OtpService otpService;

    @Autowired
    OtpServiceJPA otpServiceJPA;

    /**
     * @param phone so dien thoai doctor
     * @return
     */
    @GetMapping(value = "/otp/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> otpRegister(@Valid String phone, @RequestHeader("lang")Optional<String>lang) {
        String language = lang.isPresent() ? lang.get() : Constants.VIETNAM_CODE;
        Object resultObj = null;
        try {
            resultObj = otpService.requestOtpRegister(phone, language);
        } catch (TeleCareException e) {
            return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(FunctionCommon.responseToClient(resultObj), HttpStatus.OK);
    }

    /**
     * @param phone so dien thoai doctor
     * @return
     */
    @GetMapping(value = "/otp/validate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> otpValidate(@Valid String phone) {
        Object resultObj = null;
        try {
            resultObj = otpService.requestOtpValidate(phone);
        } catch (TeleCareException e) {
            return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(FunctionCommon.responseToClient(resultObj), HttpStatus.OK);
    }

    @GetMapping(value = "/validate-otp", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> validateDoctorOtp(@Valid String phoneNumber, String otp, @RequestHeader("lang") Optional<String> lang) {
        String language = lang.isPresent() ? lang.get() : "vi";
        Object resultObj = null;
        try {
            resultObj = otpServiceJPA.validateOtp(phoneNumber, otp, language);
        }
        catch (TeleCareException e) {
            return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(FunctionCommon.responseToClient(resultObj), HttpStatus.OK);
    }
}
