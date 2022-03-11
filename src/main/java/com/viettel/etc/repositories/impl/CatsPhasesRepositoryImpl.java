package com.viettel.etc.repositories.impl;

import com.viettel.etc.dto.CatsPhasesDTO;
import com.viettel.etc.repositories.CatsPhasesRepository;
import com.viettel.etc.xlibrary.core.repositories.CommonDataBaseRepository;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
import java.util.ArrayList;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.HashMap;

/**
 * Autogen class Repository Impl: 
 * 
 * @author ToolGen
 * @date Mon Sep 14 16:31:33 ICT 2020
 */
@Repository
public class CatsPhasesRepositoryImpl extends CommonDataBaseRepository implements CatsPhasesRepository{    

    /**
     * Danh sach cac danh muc giai doan gia
     * 
     * @param itemParamsEntity: params client truyen len
     * @return 
     */
    @Override
    public List<CatsPhasesDTO> getCatsPhases(CatsPhasesDTO itemParamsEntity){
         StringBuilder sql = new StringBuilder();
         sql.append("SELECT * FROM cats_phases cp WHERE cp.is_active = 1 AND cp.is_delete = 0");
         List<Object> arrParams = new ArrayList<>();
         //==========TODO: DEV Thuc hien bo sung params va edit query o day=====
         //Example: String sql = select * from table where column1=?,column2=?
         //         arrParams.add("value1");
         //         arrParams.add("value2");
         //==========END TODO ==================================================
         Integer start = null;
         if(itemParamsEntity!=null && itemParamsEntity.getStartrecord()!=null){
             start = itemParamsEntity.getStartrecord();
         }
         Integer pageSize = null;
         if(itemParamsEntity!=null && itemParamsEntity.getPagesize()!=null){
             pageSize = itemParamsEntity.getPagesize();
         }
         List<CatsPhasesDTO>  listData = (List<CatsPhasesDTO>) getListData(sql, arrParams, start, pageSize,CatsPhasesDTO.class);
         return listData;
    }
}