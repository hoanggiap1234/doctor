package com.viettel.etc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommunicationsResponse {
    CommunicationsResponseData data;

    @Data
    @NoArgsConstructor
    public static class CommunicationsResponseData {
        Integer totalQuestions;

        Integer answeredQuestions;

        Integer notAnsweredQuestions;
    }
}
