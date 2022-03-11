package com.viettel.etc.repositories;

import com.viettel.etc.dto.DoctorDTO;
import com.viettel.etc.utils.TeleCareException;
import com.viettel.etc.utils.TelecareUserEntity;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;

/**
 * Autogen class Repository Interface: Lop thong tin cua bac si
 *
 * @author toolGen
 * @date Wed Aug 19 14:21:51 ICT 2020
 */
public interface DoctorRepository {


	public ResultSelectEntity getDoctors(DoctorDTO itemParamsEntity, TelecareUserEntity userSystemEntity) throws TeleCareException;

	public ResultSelectEntity getDoctorsExcel(DoctorDTO itemParamsEntity);


	public Object getDoctorInfo(DoctorDTO itemParamsEntity);


	public ResultSelectEntity getTopDoctors(DoctorDTO itemParamsEntity);

	public Object getDoctorByKeycloakUserId(DoctorDTO itemParamsEntity);

    public Object getPatients(Integer doctorId);
}