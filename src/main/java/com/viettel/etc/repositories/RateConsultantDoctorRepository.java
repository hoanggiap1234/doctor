package com.viettel.etc.repositories;

import com.viettel.etc.dto.RateConsultantDoctorDTO;
import java.util.List;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;

/**
 * Autogen class Repository Interface:
 *
 * @author toolGen
 * @date Tue Sep 15 23:10:41 ICT 2020
 */
public interface RateConsultantDoctorRepository {


    public List<RateConsultantDoctorDTO> rateConsultantDoctor(RateConsultantDoctorDTO itemParamsEntity);
}