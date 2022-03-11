package com.viettel.etc.services;

import com.viettel.etc.dto.RateConsultantDoctorDTO;
import com.viettel.etc.utils.TeleCareException;
import org.springframework.security.core.Authentication;

/**
 * Autogen class:
 *
 * @author ToolGen
 * @date Tue Sep 15 23:10:41 ICT 2020
 */
public interface RateConsultantDoctorService {


    public Object rateConsultantDoctor(RateConsultantDoctorDTO itemParamsEntity, Authentication authentication) throws TeleCareException;
}