package com.viettel.etc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CovidPatientResultDTO {

    private Integer patientResultId;

    private Integer patientId;

    private Integer vaccineId;

    private String batchNumber;

    private String injectionPlace;

    private Integer ekipId;

    private Date injectionDate;

    private String isDelete;

    private String isActive;

    private String createUserId;

    private Date createDate;

    private String updateUserId;

    private Date updateDate;

    private Integer startrecord;

    private Integer pagesize;

    private Boolean resultSqlEx;

    private String language;

    private String personalPhoneNumber;

    private String fullname;

    private Long birthday;

    private Integer dataSource;

    private String doctorName;

}
