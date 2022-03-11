package com.viettel.etc.repositories.tables;


import com.viettel.etc.repositories.tables.entities.ImportHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportHistoryRepositoryJPA extends JpaRepository<ImportHistoryEntity, Integer> {
}
