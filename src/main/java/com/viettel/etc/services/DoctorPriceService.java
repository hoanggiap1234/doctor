package com.viettel.etc.services;

import com.viettel.etc.dto.DoctorPriceDTO;
import com.viettel.etc.utils.TeleCareException;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * Autogen class:
 *
 * @author ToolGen
 * @date Mon Sep 07 14:45:01 ICT 2020
 */
public interface DoctorPriceService {

	Object getDoctorPrices(DoctorPriceDTO itemParamsEntity, Authentication authentication) throws TeleCareException;

	void createDoctorPrices(List<DoctorPriceDTO> dtos, int phaseId, Authentication authentication, String language) throws TeleCareException;

	Object getDoctorPricesByPhaseIdAndDoctorId(DoctorPriceDTO itemParamsEntity, Authentication authentication) throws TeleCareException;

	Object updateDoctorPrices(DoctorPriceDTO dataParams, Authentication authentication) throws TeleCareException;

}
