package com.viettel.etc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlanPositionDTO {
    private Integer id;

    private String queryString;

    private Integer planId;

    private Integer position;

    private Integer pagesize;

    private Integer startrecord;

    private Integer doctorId;

    private String doctorName;

    private String language;
}
