package com.viettel.etc.repositories.tables;

import com.viettel.etc.repositories.tables.entities.CatsEthnicitiesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatsEthnicitiesRepositoryJPA extends JpaRepository<CatsEthnicitiesEntity, String> {
    CatsEthnicitiesEntity findByName(String name);
}
