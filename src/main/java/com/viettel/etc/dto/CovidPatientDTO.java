package com.viettel.etc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CovidPatientDTO {

    Integer patientId;

    String fullname;

    Long birthday;

    Integer year;

    String genderId;

    String genderVi;

    String genderEn;

    String nation;

    String job;

    String object;

    String nationId;

    String ethnicityId;

    String religionId;

    String jobId;

    String avatar;

    String identification;

    String identificationDate;

    String identificationPlace;

    String personalPhoneNumber;

    String homePhoneNumber;

    String email;

    String residentProvinceId;

    String residentDistrictId;

    String residentWardId;

    Integer residentVillageId;

    String currentProvinceId;

    String currentDistrictId;

    String currentWardId;

    Integer currentVillageId;

    String workplace;

    String isActive;

    String isDelete;

    Integer isSync;

    String createUserId;

    String createDate;

    String updateDate;

    String updateUserId;

    Integer numberVaccine;

    String healthInsuranceNumber;

    String fromDate;

    String toDate;

    String areaCode;

    String healthfacilitiesNameVi;

    String healthfacilitiesNameEn;

    String queryString;

    Integer startrecord;

    Integer pagesize;

    Boolean resultSqlEx;

    String language;

    String vaccineName;

    String gender = genderVi;

    Integer doctorId;

    String doctorUsername;

    Integer dataSource;

    Integer objectId;

    Integer stepsNumber;
    Integer planDetailId;
    private Integer vaccineId;
    private Integer ekipId;
    private String injectionPlace;
    private Integer injectionNumber;
    private Date expiryDate;
    private Integer producerId;
    private Integer stepNumber;
    private String doctorName;
    private String batchNumber;
    private Date injectionDate;
    private Integer hsskId;
    private Integer planId;

    public String getGenderVi() {
        if(Objects.equal(genderId, "male")) return "Nam";
        else if(Objects.equal(genderId, "female")) return "Nữ";
        else return "Chưa xác định";
    }
    private String doctorHealthFacilitiesIdsString;

    private String healthfacilitiesId;
}
