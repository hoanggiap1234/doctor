package com.viettel.etc.services.tables;

import com.viettel.etc.repositories.tables.SysUsersRepositoryJPA;
import com.viettel.etc.repositories.tables.entities.SysUsersEntity;
import com.viettel.etc.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUsersServiceJPA {

    @Autowired
    SysUsersRepositoryJPA sysUserRepositoryJPA;

    public SysUsersEntity getByIdAndType(Integer id, Integer type) {
        return sysUserRepositoryJPA.findByIdAndTypeAndIsActiveAndIsDelete(id, type, 1, 0);
    }
}
