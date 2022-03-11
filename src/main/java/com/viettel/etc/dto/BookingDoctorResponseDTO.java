package com.viettel.etc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDoctorResponseDTO {

    Integer doctorId;

    String healthfacilitiesCode;

    String healthfacilitiesName;

    Date day;

    String period;

    String value;

    Boolean canBook;

    Boolean isAm;
}
