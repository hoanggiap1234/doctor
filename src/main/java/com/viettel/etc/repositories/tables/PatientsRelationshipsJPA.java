package com.viettel.etc.repositories.tables;

import com.viettel.etc.repositories.tables.entities.OtpEntity;
import com.viettel.etc.repositories.tables.entities.OtpIdentify;
import com.viettel.etc.repositories.tables.entities.PatientsRelationshipsEntity;
import com.viettel.etc.utils.Constants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientsRelationshipsJPA extends JpaRepository<PatientsRelationshipsEntity, Integer> {

    Boolean existsByPatientIdAndPatientParentIdAndIsActiveAndIsDelete(Integer patientId, Integer currentPatientId, boolean isActive, boolean isDelete);

}
