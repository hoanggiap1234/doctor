package com.viettel.etc.repositories;

import com.viettel.etc.dto.DoctorPriceDTO;
import com.viettel.etc.utils.TeleCareException;
import com.viettel.etc.utils.TelecareUserEntity;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;

/**
 * Autogen class Repository Interface:
 *
 * @author toolGen
 * @date Mon Sep 07 14:45:01 ICT 2020
 */
public interface DoctorPriceRepository {

	ResultSelectEntity getDoctorPrices(DoctorPriceDTO itemParamsEntity, TelecareUserEntity userEntity) throws TeleCareException;

	ResultSelectEntity getDoctorPricesByPhaseIdAndDoctorId(DoctorPriceDTO itemParamsEntity);
}
