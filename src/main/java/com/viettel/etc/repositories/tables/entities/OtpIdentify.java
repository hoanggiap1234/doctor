package com.viettel.etc.repositories.tables.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpIdentify implements Serializable {

    public static final int DOCTOR_REGISTER = 1;
    public static final int PATIENT_REGISTER = 2;

    String phone;

    Integer confirmType;
}
