package com.viettel.etc.services.tables;

import com.viettel.etc.repositories.tables.entities.CatsMessagesEntity;
import com.viettel.etc.repositories.tables.CatsMessagesRepositoryJPA;
import java.util.List;

import com.viettel.etc.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Autogen class: Create Service For Table Name Cats_messages
 *
 * @author ToolGen
 * @date Mon Dec 07 17:22:03 ICT 2020
 */
@Service
public class CatsMessagesServiceJPA {

    @Autowired
    CatsMessagesRepositoryJPA cats_messages;

    public List<CatsMessagesEntity> getAllMessages() {
        return cats_messages.findAllByIsDelete(Constants.IS_NOT_DELETE_BOOLEAN);
    }

}