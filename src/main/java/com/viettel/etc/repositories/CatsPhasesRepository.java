package com.viettel.etc.repositories;

import com.viettel.etc.dto.CatsPhasesDTO;
import java.util.List;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;

/**
 * Autogen class Repository Interface: 
 * 
 * @author toolGen
 * @date Mon Sep 14 16:31:33 ICT 2020
 */
public interface CatsPhasesRepository {


    public List<CatsPhasesDTO> getCatsPhases(CatsPhasesDTO itemParamsEntity);
}