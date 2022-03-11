package com.viettel.etc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestChangePasswordDTO {

    @NotNull
    String newPassword;

    @NotNull
    String oldPassword;

    String language;

    String userName;

    String fullname;
}
