package com.viettel.etc.controllers;

import com.viettel.etc.dto.CatsPhasesDTO;
import com.viettel.etc.services.CatsPhasesService;
import com.viettel.etc.xlibrary.core.constants.FunctionCommon;
import com.viettel.etc.xlibrary.core.entities.UserSystemEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.viettel.etc.utils.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

/**
 * Autogen class: 
 * 
 * @author ToolGen
 * @date Mon Sep 14 16:31:32 ICT 2020
 */
@RestController
@RequestMapping(Constants.REQUEST_MAPPING_V1 + "/catsphases")
public class CatsPhasesController {
    @Autowired 
    private CatsPhasesService catsPhasesService;
    

    /**
     * Danh sach cac danh muc giai doan gia
     * 
     * @param authentication: thong tin nguoi dung
     * @param dataParams      params client
     * @return 
     */
    @GetMapping(value = "cats-phases", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getCatsPhases(@AuthenticationPrincipal Authentication authentication, 
                                              CatsPhasesDTO dataParams) {
        /*
        ==========================================================
        authenEntity: user info and role
        dataParams: danh sach bien client co the truyen len
        ==========================================================
        */
        Object resultObj = catsPhasesService.getCatsPhases(dataParams);
        return new ResponseEntity<>(FunctionCommon.responseToClient(resultObj), HttpStatus.OK);
    }
}