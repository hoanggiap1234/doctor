package com.viettel.etc.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.viettel.etc.dto.CovidPatientIdentifierDTO;
import com.viettel.etc.dto.CovidPatientResultDTO;
import com.viettel.etc.dto.CovidReceptionDTO;
import com.viettel.etc.dto.PlanPositionDTO;
import com.viettel.etc.utils.TeleCareException;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.text.ParseException;

public interface CovidImmunizationService {
    Object getDoctorByPosition(PlanPositionDTO dataParams, Authentication authentication) throws TeleCareException, IOException, ParseException;

    Object getCovidPatientDetails(CovidPatientIdentifierDTO dataParams, Authentication authentication) throws TeleCareException, IOException, ParseException;

    void confirmReception(CovidReceptionDTO dataParams, Authentication authentication) throws TeleCareException, ParseException, JsonProcessingException;

    void saveImmunizationResult(CovidPatientResultDTO dataParams, Authentication authentication) throws TeleCareException, ParseException, JsonProcessingException;

    void clearCachePatientDetail(CovidPatientIdentifierDTO dataParams, Authentication authentication) throws TeleCareException;

    void clearCacheDoctorPosition(PlanPositionDTO dataParams, Authentication authentication) throws TeleCareException;
}
