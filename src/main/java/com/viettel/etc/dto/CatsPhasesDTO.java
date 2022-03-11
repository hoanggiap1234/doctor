package com.viettel.etc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Autogen class DTO:
 *
 * @author ToolGen
 * @date Mon Sep 14 15:27:30 ICT 2020
 */
@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class CatsPhasesDTO {

    Integer phaseId;

    String healthfacilitiesCode;

    String code;

    String name;

    Date fromDate;

    Date toDate;

    String description;

    Boolean isDelete;

    Boolean isActive;

    Integer createUserId;

    Date createDate;

    Integer updateUserId;

    Date updateDate;

    Integer startrecord;

    Integer pagesize;

    Boolean resultSqlEx;
}