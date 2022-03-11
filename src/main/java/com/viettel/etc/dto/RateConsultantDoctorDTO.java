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
 * @date Tue Sep 15 23:10:41 ICT 2020
 */
@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class RateConsultantDoctorDTO {

    Integer doctorId;

    Integer patientId;

    String comments;

    String pointEvaluation;

    Integer startrecord;

    Integer pagesize;

    Boolean resultSqlEx;

    String language;
}