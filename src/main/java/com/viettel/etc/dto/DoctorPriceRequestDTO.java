package com.viettel.etc.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class DoctorPriceRequestDTO {
    @NotNull
    private Integer doctorId;

    @NotNull
    private String healthfacilitiesCode;

    private String language;
}
