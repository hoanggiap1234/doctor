package com.viettel.etc.services.impl;

import com.viettel.etc.repositories.CatsPhasesRepository;
import com.viettel.etc.dto.CatsPhasesDTO;
import com.viettel.etc.services.CatsPhasesService;
import org.springframework.beans.factory.annotation.Autowired;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Autogen class: 
 * 
 * @author ToolGen
 * @date Mon Sep 14 16:31:33 ICT 2020
 */
@Service
public class CatsPhasesServiceImpl implements CatsPhasesService{

    @Autowired 
    private CatsPhasesRepository catsPhasesRepository;
    

    /**
     * Danh sach cac danh muc giai doan gia
     * 
     * @param itemParamsEntity params client
     * @return 
     */
    @Override
    public Object getCatsPhases(CatsPhasesDTO itemParamsEntity) {
        /*
        ==========================================================
        itemParamsEntity: params nguoi dung truyen len
        ==========================================================
        */
        List<CatsPhasesDTO> dataResult = catsPhasesRepository.getCatsPhases(itemParamsEntity);
        /*
        ==========================================================
        TODO: (Code at here) Thuc hien luong nghiep vu chi tiet
        ==========================================================
        */
        return dataResult;
    }
}