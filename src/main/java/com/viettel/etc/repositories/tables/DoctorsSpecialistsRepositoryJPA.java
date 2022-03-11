package com.viettel.etc.repositories.tables;import com.viettel.etc.repositories.tables.entities.DoctorsSpecialistsEntity;import com.viettel.etc.utils.Constants;import org.springframework.data.jpa.repository.JpaRepository;import org.springframework.data.jpa.repository.Modifying;import org.springframework.data.jpa.repository.Query;import org.springframework.stereotype.Repository;import java.util.List;/** * Autogen class Repository Interface: Create Repository For Table Name Doctors_specialists * * @author ToolGen * @date Wed Aug 19 21:20:46 ICT 2020 */@Repositorypublic interface DoctorsSpecialistsRepositoryJPA extends		JpaRepository<DoctorsSpecialistsEntity, Integer> {	@Modifying	@Query("update DoctorsSpecialistsEntity ds set ds.isDelete=?2 where ds.doctorId=?1")	void updateDeleteStatus(int id, boolean status);	default void deleteByDoctorId(int id) {		updateDeleteStatus(id, Constants.IS_DELETE);	}//	List<DoctorsSpecialistsEntity> findByDoctorId(int doctorId);	List<DoctorsSpecialistsEntity> findByDoctorIdAndIsDelete(int doctorId, boolean isDetele);}