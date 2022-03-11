package com.viettel.etc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HealthFacilityDTO {

    private String healthfacilitiesCode;

    private String healthfacilitiesName;

    private Integer doctorId;

    private Integer medicalExaminationFee;

    private String healthfacilitiesRoute;

}
