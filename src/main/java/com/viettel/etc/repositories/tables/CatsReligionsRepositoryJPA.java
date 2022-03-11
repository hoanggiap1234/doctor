package com.viettel.etc.repositories.tables;


import com.viettel.etc.repositories.tables.entities.CatsReligionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatsReligionsRepositoryJPA extends JpaRepository<CatsReligionsEntity, String> {
    CatsReligionsEntity findByName(String name);
}
