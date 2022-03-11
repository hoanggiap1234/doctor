package com.viettel.etc.repositories.tables;

import com.viettel.etc.repositories.tables.entities.DoctorsHealthfacilitiesEntity;
import com.viettel.etc.utils.Constants;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface DoctorsHealthfacilitiesRepositoryJPA extends
		JpaRepository<DoctorsHealthfacilitiesEntity, Integer> {

	@Modifying
	@Query("update DoctorsHealthfacilitiesEntity dh set dh.isDelete=?2 where dh.doctorId=?1")
	void updateDeleteStatus(int id, boolean status);

	default void deleteByDoctorId(int id) {
		updateDeleteStatus(id, Constants.IS_DELETE);
	}

//	List<DoctorsHealthfacilitiesEntity> findByDoctorId(int doctorId);

	List<DoctorsHealthfacilitiesEntity> findByDoctorIdAndIsDelete(int doctorId, boolean isDelete);

	@Query("select d.doctorId from DoctorsHealthfacilitiesEntity d where d.healthfacilitiesCode in ?1 and d.isActive=true and d.isDelete=false")
	List<Integer> getDoctorIdsByHealthficility(List<String> codes);

	@Query("select dh.healthfacilitiesCode from DoctorsHealthfacilitiesEntity dh where dh.isActive=true and dh.isDelete=false and dh.doctorId=?1")
	List<String> getHealthFacilitiesCodesByDoctorId(Integer doctorId);

}
