package com.viettel.etc.services.tables;

import com.viettel.etc.repositories.tables.OtpRepositoryJPA;
import com.viettel.etc.utils.TeleCareException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class OtpServiceJPATest {
    @Mock
    OtpRepositoryJPA otpRepositoryJPA;
    @InjectMocks
    OtpServiceJPA otpServiceJPA;

    @BeforeEach
    void setUp() {
        otpServiceJPA = new OtpServiceJPA();
        MockitoAnnotations.initMocks(this);
    }
    @Test
    void validateOtp() throws TeleCareException {
        String phoneNumber = "0123456789";
        String otp = "123456";
        String language = "vi";
        Integer confirmType = 1;
        Mockito.when(otpRepositoryJPA.existsByPhoneAndOtpAndConfirmType(phoneNumber, otp, confirmType)).thenReturn(true);
        otpServiceJPA.validateOtp(phoneNumber, otp, language);
    }
}