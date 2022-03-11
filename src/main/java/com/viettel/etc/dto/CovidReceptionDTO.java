package com.viettel.etc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CovidReceptionDTO {

    private Integer receptionId;
    private Integer stepsNumber;  // Bước thực hiện(1: Tiếp đón, 2: Khám sàng lọc, 3: Thực hiện tiêm, 4: Theo dõi)
    private Integer patientId;
    private Integer planId;
    private Integer planDetailId;
    private Date receptionDay;
    private String doctorName;
    private Integer doctorId;
    private String language;
    private Integer dataSource;
    private Integer isConfirm;
    private Integer planIdTC;
    private String fullname;
    private Integer hsskId;
    private String personalPhoneNumber;
    private String birthday;
}
