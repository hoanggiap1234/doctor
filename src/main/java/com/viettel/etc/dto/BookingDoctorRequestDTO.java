package com.viettel.etc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDoctorRequestDTO {

    Integer doctorId;

    String healthfacilitiesCode;

    String healthfacilitiesName;

    Long day;

    Integer startrecord;

    Integer pagesize;

    Boolean resultSqlEx;

    String language;
}
