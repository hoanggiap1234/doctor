package com.viettel.etc.repositories.tables;import com.viettel.etc.repositories.tables.entities.DoctorsCommentsEntity;import org.springframework.data.jpa.repository.JpaRepository;import org.springframework.stereotype.Repository;/** * Autogen class Repository Interface: Create Repository For Table Name Doctors_comments *  * @author ToolGen * @date Wed Dec 23 14:47:05 ICT 2020 */@Repositorypublic interface DoctorsCommentsRepositoryJPA extends JpaRepository<DoctorsCommentsEntity, Long> {     int countByDoctorIdAndIsActiveAndIsDelete(int doctorId, boolean isActive, boolean isDelete);}