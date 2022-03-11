package com.viettel.etc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CovidPatientDetailResponse {
    CovidPatientDTO data;

    @JsonIgnoreProperties
    Object mess;
}
