package com.viettel.etc.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CovidPatientIdentifierDTO {
    @NotNull(message = "họ tên không được để trống")
    private String fullname;

    @NotNull(message = "ngày sinh không được để trống")
    private Long birthday;

    @NotNull(message = "số điện thoại không được để trống")
    private String personalPhoneNumber;

    private String language;

    private String doctorHealthFacilitiesIdsString;
}
