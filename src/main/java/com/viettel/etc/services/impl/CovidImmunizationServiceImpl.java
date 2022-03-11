package com.viettel.etc.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.viettel.etc.dto.*;
import com.viettel.etc.repositories.tables.DoctorsHealthfacilitiesRepositoryJPA;
import com.viettel.etc.repositories.tables.DoctorsRepositoryJPA;
import com.viettel.etc.services.CovidImmunizationService;
import com.viettel.etc.services.JedisCacheService;
import com.viettel.etc.services.MessageService;
import com.viettel.etc.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class CovidImmunizationServiceImpl implements CovidImmunizationService {
    @Autowired
    private MessageService messageService;

    @Autowired
    private JedisCacheService jedisCacheService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DoctorsHealthfacilitiesRepositoryJPA doctorsHealthfacilitiesRepositoryJPA;

    @Override
    public Object getDoctorByPosition(PlanPositionDTO dataParams, Authentication authentication) throws TeleCareException, IOException, ParseException {
        TelecareUserEntity telecareUser = FnCommon.getTelecareUserInfo(authentication);
        if(telecareUser == null || !telecareUser.isDoctor()) {
            throw new TeleCareException(messageService.getMessage(ErrorApp.ERR_USER_PERMISSION.name(), dataParams.getLanguage()));
        }
        if(jedisCacheService.hget(dataParams.getPlanId()+"|"+dataParams.getPosition(), "plan_position") != null) {
            return objectMapper.readValue(jedisCacheService.hget(dataParams.getPlanId()+"|"+dataParams.getPosition(), "plan_position").toString(), PlanPositionResponseDTO.PlanPositionDataDTO.class);
        }
        String token = FnCommon.getTokenHSSK();
        String url = System.getenv("HSSK_VACCINES_URL") + "/api/v1/immunization/covid-patient/plan-positions" + FnCommon.toQueryString(dataParams);
//        String url = "http://localhost:9006/covid-patient/plan-positions" + FnCommon.toQueryString(dataParams);
        Response response = FnCommon.doGetRequest(url, token);
        if (response == null || !response.isSuccessful()) {
            throw new TeleCareException(ErrorApp.ERROR_INPUTPARAMS, messageService.getMessage(Constants.GET_DOCTOR_POSITION_PLAN_FAIL, dataParams.getLanguage()));
        }
        try (ResponseBody responseBody = response.body()) {
            PlanPositionResponseDTO result = new Gson().fromJson(responseBody.string(), PlanPositionResponseDTO.class);
            jedisCacheService.hsetExpire(dataParams.getPlanId()+"|"+dataParams.getPosition(), "plan_position", FnCommon.toStringJson(result.getData()), 2 * 60);
            return result.getData();
        }
    }

    @Override
    public Object getCovidPatientDetails(CovidPatientIdentifierDTO dataParams, Authentication authentication) throws TeleCareException, IOException, ParseException {
        TelecareUserEntity telecareUser = FnCommon.getTelecareUserInfo(authentication);
        if(telecareUser == null || !telecareUser.isDoctor()) {
            throw new TeleCareException(messageService.getMessage(ErrorApp.ERR_USER_PERMISSION.name(), dataParams.getLanguage()));
        }
        List<String> doctorHealthFacilitiesIds = this.getHealthFacilitiesByDoctorId(telecareUser);
        String doctorHealthFacilitiesIdsString = String.join(",", doctorHealthFacilitiesIds);
        dataParams.setDoctorHealthFacilitiesIdsString(doctorHealthFacilitiesIdsString);

        String keyCache = dataParams.getPersonalPhoneNumber() + dataParams.getFullname().replaceAll("\\s","") + dataParams.getBirthday();
        if(jedisCacheService.hget(keyCache, "patient_planId") != null) {
            return objectMapper.readValue(jedisCacheService.hget(keyCache, "patient_planId").toString(), CovidPatientDTO.class);
        }
        String token = FnCommon.getTokenHSSK();
        String url = System.getenv("HSSK_VACCINES_URL") + "/api/v1/immunization/covid-patient/detail" + FnCommon.toQueryString(dataParams);
//        String url = "http://localhost:9006/covid-patient/detail" + FnCommon.toQueryString(dataParams);
        Response response = FnCommon.doGetRequest(url, token);
        if (response == null || !response.isSuccessful()) {
            throw new TeleCareException(messageService.getMessage(ErrorApp.GET_INFO_RESIDENT_FAIL.name(), dataParams.getLanguage()));
        }
        String responseBodyStr = response.body().string();
        CovidPatientDetailResponse result = objectMapper.readValue(responseBodyStr, CovidPatientDetailResponse.class);
        response.body().close();
        if(result.getData() == null) {
            throw new TeleCareException(messageService.getMessage(ErrorApp.GET_INFO_RESIDENT_FAIL.name(), dataParams.getLanguage()));
        }

        jedisCacheService.hsetExpire(keyCache, "patient_planId", FnCommon.toStringJson(result.getData()), 60 * 2);
        return result.getData();
    }

    @Override
    public void confirmReception(CovidReceptionDTO dataParams, Authentication authentication) throws TeleCareException, ParseException, JsonProcessingException {
        TelecareUserEntity telecareUser = FnCommon.getTelecareUserInfo(authentication);
        if(telecareUser == null || !telecareUser.isDoctor()) {
            throw new TeleCareException(messageService.getMessage(ErrorApp.ERR_USER_PERMISSION.name(), dataParams.getLanguage()));
        }
        dataParams.setPatientId(dataParams.getHsskId());
        dataParams.setReceptionDay(new Date());
        RequestBody requestBody = RequestBody.create(Constants.JSON, FnCommon.toStringJson(dataParams));
        String token = FnCommon.getTokenHSSK();
//        String url = "http://localhost:9006/covid-patient/reception";
        String url = System.getenv("HSSK_VACCINES_URL") + "/api/v1/immunization/covid-patient/reception" ;
        Response response = FnCommon.doPostRequest(url, token, requestBody);
        if (response == null || !response.isSuccessful()) {
            throw new TeleCareException(ErrorApp.ERROR_INPUTPARAMS, messageService.getMessage(Constants.ERR_CONFIRM_RECEPTION_FAILED, dataParams.getLanguage()));
        }
        String keyCache = dataParams.getPersonalPhoneNumber() + dataParams.getFullname().replaceAll("\\s","") + dataParams.getBirthday();
        if(jedisCacheService.hget(keyCache, "patient_planId") != null) {
            CovidPatientDTO patientInfo = objectMapper.readValue(jedisCacheService.hget(keyCache, "patient_planId").toString(), CovidPatientDTO.class);
            patientInfo.setStepsNumber(Constants.RECEPTION_DONE_STEP_NUMBER);
            patientInfo.setNumberVaccine(patientInfo.getNumberVaccine() + 1);
            jedisCacheService.hsetExpire(keyCache, "patient_planId", FnCommon.toStringJson(patientInfo), 60 * 2);
        }
    }

    @Override
    public void saveImmunizationResult(CovidPatientResultDTO dataParams, Authentication authentication) throws TeleCareException, ParseException, JsonProcessingException {
        TelecareUserEntity telecareUser = FnCommon.getTelecareUserInfo(authentication);
        if(telecareUser == null || !telecareUser.isDoctor()) {
            throw new TeleCareException(messageService.getMessage(ErrorApp.ERR_USER_PERMISSION.name(), dataParams.getLanguage()));
        }

        CovidPatientDTO patientInfo = null;
        String keyCache = dataParams.getPersonalPhoneNumber() + dataParams.getFullname().replaceAll("\\s","") + dataParams.getBirthday();
        if(jedisCacheService.hget(keyCache, "patient_planId") != null) {
            patientInfo = objectMapper.readValue(jedisCacheService.hget(keyCache, "patient_planId").toString(), CovidPatientDTO.class);
            if(Objects.equals(patientInfo.getStepsNumber(), Constants.DONE_IMMUNIZATION_STEP_NUMBER)) {
                throw new TeleCareException(messageService.getMessage(ErrorApp.CREATED_IMMUNIZATION_RESULT_ALREADY.name(), dataParams.getLanguage()));
            }
        }
        if(patientInfo == null) {
            patientInfo = new CovidPatientDTO();
            FnCommon.copyProperties(dataParams, patientInfo);
        }
        patientInfo.setInjectionDate(new Date());
        patientInfo.setPatientId(patientInfo.getHsskId());
        if(patientInfo.getDoctorName() == null)
            patientInfo.setDoctorName(dataParams.getDoctorName());
        if(patientInfo.getDataSource() == null)
            patientInfo.setDataSource(dataParams.getDataSource());
        List<String> doctorHealthFacilitiesIds = this.getHealthFacilitiesByDoctorId(telecareUser);
        String doctorHealthFacilitiesIdsString = String.join(",", doctorHealthFacilitiesIds);
        patientInfo.setDoctorHealthFacilitiesIdsString(doctorHealthFacilitiesIdsString);

        RequestBody requestBody = RequestBody.create(Constants.JSON, FnCommon.toStringJson(patientInfo));
        String token = FnCommon.getTokenHSSK();
//        String url = "http://localhost:9006/covid-patient/result";
        String url = System.getenv("HSSK_VACCINES_URL") + "/api/v1/immunization/covid-patient/result" ;
        Response response = FnCommon.doPostRequest(url, token, requestBody);
        if (response == null || !response.isSuccessful()) {
            throw new TeleCareException(ErrorApp.ERROR_INPUTPARAMS, messageService.getMessage(ErrorApp.ERR_CREATED_IMMUNIZATION_RESULT_FAIL.name(), dataParams.getLanguage()));
        } else {
            patientInfo.setStepsNumber(3);
            patientInfo.setNumberVaccine(patientInfo.getNumberVaccine() + 1);
            jedisCacheService.hsetExpire(keyCache, "patient_planId", FnCommon.toStringJson(patientInfo), 60);
        }
    }

    @Override
    public void clearCachePatientDetail(CovidPatientIdentifierDTO dataParams, Authentication authentication) throws TeleCareException {
        TelecareUserEntity telecareUser = FnCommon.getTelecareUserInfo(authentication);
        if(telecareUser == null || !telecareUser.isAdmin()) {
            throw new TeleCareException(messageService.getMessage(ErrorApp.ERR_USER_PERMISSION.name(), dataParams.getLanguage()));
        }
        String keyCache = dataParams.getPersonalPhoneNumber() + dataParams.getFullname().replaceAll("\\s","") + dataParams.getBirthday();
        if(jedisCacheService.hget(keyCache, "patient_planId") != null) {
            jedisCacheService.hdelete(keyCache, "patient_planId");
        }
    }

    @Override
    public void clearCacheDoctorPosition(PlanPositionDTO dataParams, Authentication authentication) throws TeleCareException {
        TelecareUserEntity telecareUser = FnCommon.getTelecareUserInfo(authentication);
        if(telecareUser == null || !telecareUser.isAdmin()) {
            throw new TeleCareException(messageService.getMessage(ErrorApp.ERR_USER_PERMISSION.name(), dataParams.getLanguage()));
        }
        if(jedisCacheService.hget(dataParams.getPlanId()+"|"+dataParams.getPosition(), "plan_position") != null) {
            jedisCacheService.hdelete(dataParams.getPlanId()+"|"+dataParams.getPosition(), "plan_position");
        }
    }

    private List<String> getHealthFacilitiesByDoctorId(TelecareUserEntity telecareUserEntity) {
        int doctorId = telecareUserEntity.getTelecareUserId().intValue();
        //list health facilities codes of doctor logged in to a string
        return doctorsHealthfacilitiesRepositoryJPA.getHealthFacilitiesCodesByDoctorId(doctorId);
    }
}
