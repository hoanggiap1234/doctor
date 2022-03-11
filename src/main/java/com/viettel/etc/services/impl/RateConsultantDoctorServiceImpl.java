package com.viettel.etc.services.impl;

import com.viettel.etc.repositories.RateConsultantDoctorRepository;
import com.viettel.etc.dto.RateConsultantDoctorDTO;
import com.viettel.etc.repositories.tables.PatientsRelationshipsJPA;
import com.viettel.etc.services.MessageService;
import com.viettel.etc.services.RateConsultantDoctorService;
import com.viettel.etc.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Autogen class:
 *
 * @author ToolGen
 * @date Tue Sep 15 23:10:41 ICT 2020
 */
@Service
public class RateConsultantDoctorServiceImpl implements RateConsultantDoctorService{

    @Autowired
    private RateConsultantDoctorRepository rateConsultantDoctorRepository;

    @Autowired
    private PatientsRelationshipsJPA patientsRelationshipsJPA;

    @Autowired
    MessageService messageService;

    /**
     * Danh gia tu van
     *
     * @param itemParamsEntity params client
     * @return
     */
    @Override
    public Object rateConsultantDoctor(RateConsultantDoctorDTO itemParamsEntity, Authentication authentication) throws TeleCareException {
        /*
        ==========================================================
        itemParamsEntity: params nguoi dung truyen len
        ==========================================================
        */
        String language = itemParamsEntity.getLanguage();
        //only patient is allowed
        TelecareUserEntity userSystemEntity = FnCommon.getTelecareUserInfo(authentication);
        if (userSystemEntity==null || !userSystemEntity.isPatient()) {
            throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, language), ErrorApp.ERR_USER_PERMISSION);
        }
        Integer currentPatientId = userSystemEntity.getTelecareUserId();
        Integer patientId = itemParamsEntity.getPatientId();
        boolean hasRelationship=Constants.HAS_RELATIONSHIP;
        if(!currentPatientId.equals(patientId)) {
            hasRelationship =
                    patientsRelationshipsJPA.existsByPatientIdAndPatientParentIdAndIsActiveAndIsDelete(patientId, currentPatientId, Constants.IS_ACTIVE, Constants.IS_NOT_DELETE);
        }
        if(!hasRelationship) {
            throw new TeleCareException(messageService.getMessage(Constants.ERROR_PATIENT_RELATIONSHIP_NOT_EXIST, language), ErrorApp.ERROR_PATIENT_RELATIONSHIP_NOT_EXIST);
        }

        List<RateConsultantDoctorDTO> dataResult = rateConsultantDoctorRepository.rateConsultantDoctor(itemParamsEntity);

        return dataResult;
    }
}