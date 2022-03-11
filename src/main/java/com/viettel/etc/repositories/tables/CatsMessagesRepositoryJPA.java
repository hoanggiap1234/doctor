package com.viettel.etc.repositories.tables;

import com.viettel.etc.repositories.tables.entities.CatsMessagesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Autogen class Repository Interface: Create Repository For Table Name Cats_messages
 *
 * @author ToolGen
 * @date Mon Dec 07 17:22:03 ICT 2020
 */
@Repository
public interface CatsMessagesRepositoryJPA extends JpaRepository<CatsMessagesEntity, Integer> {

    List<CatsMessagesEntity> findAllByIsDelete(boolean isDelete);
}