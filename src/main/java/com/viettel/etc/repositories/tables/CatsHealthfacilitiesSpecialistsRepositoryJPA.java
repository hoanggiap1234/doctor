package com.viettel.etc.repositories.tables;


import com.viettel.etc.repositories.tables.entities.CatsHealthfacilitiesSpecialistsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatsHealthfacilitiesSpecialistsRepositoryJPA extends JpaRepository<CatsHealthfacilitiesSpecialistsEntity, Integer> {
    boolean existsByHealthfacilitiesCodeAndSpecialistCode(String healthfacilitiesCode, String specialistCode);
}
