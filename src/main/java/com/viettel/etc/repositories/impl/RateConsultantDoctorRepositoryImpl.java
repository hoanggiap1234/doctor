package com.viettel.etc.repositories.impl;

import com.viettel.etc.dto.RateConsultantDoctorDTO;
import com.viettel.etc.repositories.RateConsultantDoctorRepository;
import com.viettel.etc.xlibrary.core.repositories.CommonDataBaseRepository;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
import java.util.ArrayList;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.HashMap;

/**
 * Autogen class Repository Impl:
 *
 * @author ToolGen
 * @date Tue Sep 15 23:10:41 ICT 2020
 */
@Repository
public class RateConsultantDoctorRepositoryImpl extends CommonDataBaseRepository implements RateConsultantDoctorRepository{

    /**
     * Danh gia tu van
     *
     * @param itemParamsEntity: params client truyen len
     * @return
     */
    @Override
    public List<RateConsultantDoctorDTO> rateConsultantDoctor(RateConsultantDoctorDTO itemParamsEntity){
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO doctors_comments (doctor_id, patient_id, comments, point_evaluation) VALUES (:doctorId, :patientId, :comments, :pointEvaluation);");
        HashMap<String, Object> hmapParams = new HashMap<>();
        hmapParams.put("doctorId", itemParamsEntity.getDoctorId());
        hmapParams.put("patientId", itemParamsEntity.getPatientId());
        hmapParams.put("comments", itemParamsEntity.getComments());
        hmapParams.put("pointEvaluation", itemParamsEntity.getPointEvaluation());
        Boolean  result = excuteSqlDatabase(sql, hmapParams);
        RateConsultantDoctorDTO itemResult= new RateConsultantDoctorDTO();
        itemResult.setResultSqlEx(result);
        List<RateConsultantDoctorDTO>  listData = new ArrayList<>();
        listData.add(itemResult);
        return listData;
    }
}