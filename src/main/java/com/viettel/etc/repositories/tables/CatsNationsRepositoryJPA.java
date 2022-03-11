package com.viettel.etc.repositories.tables;

import com.viettel.etc.repositories.tables.entities.CatsNationsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatsNationsRepositoryJPA extends JpaRepository<CatsNationsEntity, String> {
    CatsNationsEntity findByNationName(String name);
}
