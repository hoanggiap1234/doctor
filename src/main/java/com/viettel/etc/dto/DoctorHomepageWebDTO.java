package com.viettel.etc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DoctorHomepageWebDTO {
    Integer patients;

    Integer rates;

    Integer totalQuestions;

    Integer answeredQuestions;

    Integer notAnsweredQuestions;
}
