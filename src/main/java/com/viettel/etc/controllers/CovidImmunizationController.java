package com.viettel.etc.controllers;

import com.viettel.etc.dto.*;
import com.viettel.etc.services.CovidImmunizationService;
import com.viettel.etc.services.MessageService;
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
import java.util.Optional;

@RestController
public class CovidImmunizationController {
    private static final Logger LOGGER = Logger.getLogger(CovidImmunizationController.class);

    @Autowired
    private MessageService messageService;
    
    @Autowired
    private CovidImmunizationService covidImmunizationService;
    
    @GetMapping(value = "/plan-positions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getPlanPositions(
            @AuthenticationPrincipal Authentication authentication, PlanPositionDTO dataParams,
            @RequestHeader Optional<String> lang) {
        String language = lang.orElse(Constants.VIETNAM_CODE);
        dataParams.setLanguage(language);
        Object result;
        try {
            result = covidImmunizationService.getDoctorByPosition(dataParams, authentication);
        } catch (TeleCareException e){
            return returnResponseEntity(language, e);
        }
        catch (Exception e) {
            e.printStackTrace();
            LOGGER.info(e);
            return new ResponseEntity<>(FnCommon.responseToClient(ErrorApp.UNKNOW_ERROR), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(FnCommon.responseToClient(result), HttpStatus.OK);
    }

    @GetMapping(value = "/covid-patient/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getCovidPatientDetail(@AuthenticationPrincipal Authentication authentication,
                                                        @Valid CovidPatientIdentifierDTO dataParams,
                                                        @RequestHeader Optional<String> lang) {
        String language = lang.orElse(Constants.VIETNAM_CODE);
        dataParams.setLanguage(language);
        Object result = null;
        try {
            result = covidImmunizationService.getCovidPatientDetails(dataParams, authentication);
        } catch (TeleCareException e){
            LOGGER.info(e);
            if (ErrorApp.REQUEST_FAIL.equals(e.getErrorApp())) {
                return new ResponseEntity<>(FnCommon.responseToClient(messageService.getMessage(ErrorApp.REQUEST_TO_HSSK_FAIL.name(), language)), HttpStatus.GATEWAY_TIMEOUT);
            } else {
                return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            LOGGER.info(e);
            e.printStackTrace();
            return new ResponseEntity<>(FnCommon.responseToClient(ErrorApp.UNKNOW_ERROR), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(FunctionCommon.responseToClient(result), HttpStatus.OK);
    }

    @PostMapping(value = "/covid-patient/reception", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> confirmReception(@AuthenticationPrincipal Authentication authentication,
                                                   @RequestBody CovidReceptionDTO dataParams,
                                                   @RequestHeader Optional<String> lang) {
        String language = lang.orElse(Constants.VIETNAM_CODE);
        dataParams.setLanguage(language);
        try {
            covidImmunizationService.confirmReception(dataParams, authentication);
        } catch (TeleCareException e){
            LOGGER.info(e);
            if (ErrorApp.REQUEST_FAIL.equals(e.getErrorApp())) {
                return new ResponseEntity<>(FnCommon.responseToClient(messageService.getMessage(ErrorApp.REQUEST_TO_HSSK_FAIL.name(), language)), HttpStatus.GATEWAY_TIMEOUT);
            } else {
                return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            LOGGER.info(e);
            e.printStackTrace();
            return new ResponseEntity<>(FnCommon.responseToClient(ErrorApp.UNKNOW_ERROR), HttpStatus.BAD_REQUEST);
        }
        MessageDTO message = messageService.getMessage(Constants.CREATE_COVID_RECEPTION_SUCCESS, language);
        message.setDescription(message.getDescription().replace("{NAME}", dataParams.getFullname()));
        return new ResponseEntity<>(FnCommon.responseToClient(message), HttpStatus.OK);
    }

    @PostMapping(value = "/covid-patient/result", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> saveImmunizationResult(@AuthenticationPrincipal Authentication authentication,
                                                         @Valid @RequestBody CovidPatientResultDTO dataParams,
                                                         @RequestHeader Optional<String> lang) {
        String language = lang.orElse(Constants.VIETNAM_CODE);
        dataParams.setLanguage(language);
        try {
            covidImmunizationService.saveImmunizationResult(dataParams, authentication);
        } catch (TeleCareException e){
            LOGGER.info(e);
            if (ErrorApp.REQUEST_FAIL.equals(e.getErrorApp())) {
                return new ResponseEntity<>(FnCommon.responseToClient(messageService.getMessage(ErrorApp.REQUEST_TO_HSSK_FAIL.name(), language)), HttpStatus.GATEWAY_TIMEOUT);
            } else {
                return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            LOGGER.info(e);
            e.printStackTrace();
            return new ResponseEntity<>(FnCommon.responseToClient(ErrorApp.UNKNOW_ERROR), HttpStatus.BAD_REQUEST);
        }
        MessageDTO successMess = messageService.getMessage(ErrorApp.SUCCESS_CREATE_COVID_IMMUNIZATION_RESULT.name(), language);
        successMess.setDescription(successMess.getDescription().replace("{NAME}", dataParams.getFullname()));
        return new ResponseEntity<>(FnCommon.responseToClient(successMess), HttpStatus.OK);
    }

    @DeleteMapping(value = "/covid-patient/clear", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> clearCachePatientDetail(@AuthenticationPrincipal Authentication authentication,
                                                          @Valid CovidPatientIdentifierDTO dataParams,
                                                          @RequestHeader Optional<String> lang) {
        String language = lang.orElse(Constants.VIETNAM_CODE);
        dataParams.setLanguage(language);
        try {
            covidImmunizationService.clearCachePatientDetail(dataParams, authentication);
        } catch (TeleCareException e){
            LOGGER.info(e);
            return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.info(e);
            e.printStackTrace();
            return new ResponseEntity<>(FnCommon.responseToClient(ErrorApp.UNKNOW_ERROR), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(FnCommon.responseToClient(CoreErrorApp.SUCCESS), HttpStatus.OK);
    }

    @DeleteMapping(value = "/plan-positions/clear", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> clearCachePatientDetail(@AuthenticationPrincipal Authentication authentication,
                                                          PlanPositionDTO dataParams,
                                                          @RequestHeader Optional<String> lang) {
        String language = lang.orElse(Constants.VIETNAM_CODE);
        dataParams.setLanguage(language);
        try {
            covidImmunizationService.clearCacheDoctorPosition(dataParams, authentication);
        } catch (TeleCareException e){
            LOGGER.info(e);
            return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.info(e);
            e.printStackTrace();
            return new ResponseEntity<>(FnCommon.responseToClient(ErrorApp.UNKNOW_ERROR), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(FnCommon.responseToClient(CoreErrorApp.SUCCESS), HttpStatus.OK);
    }

    private ResponseEntity<Object> returnResponseEntity(String language, TeleCareException e) {
        LOGGER.info(e);
        if (ErrorApp.REQUEST_FAIL.equals(e.getErrorApp())) {
            return new ResponseEntity<>(FnCommon.responseToClient(messageService.getMessage(ErrorApp.REQUEST_TO_HSSK_FAIL.name(), language)), HttpStatus.GATEWAY_TIMEOUT);
        } else {
            return new ResponseEntity<>(FnCommon.responseToClient(e), HttpStatus.BAD_REQUEST);
        }
    }
}
