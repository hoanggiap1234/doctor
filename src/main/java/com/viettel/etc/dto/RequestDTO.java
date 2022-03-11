package com.viettel.etc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestDTO {

    Boolean enabled;

    Boolean temporary;

    String type;

    @NotNull
    String value;
}
