package com.viettel.etc.services.tables;

import com.viettel.etc.dto.OtpDTO;
import com.viettel.etc.repositories.tables.OtpRepositoryJPA;
import com.viettel.etc.repositories.tables.entities.OtpEntity;
import com.viettel.etc.repositories.tables.entities.OtpIdentify;
import com.viettel.etc.services.MessageService;
import com.viettel.etc.utils.Constants;
import com.viettel.etc.utils.ErrorApp;
import com.viettel.etc.utils.FnCommon;
import com.viettel.etc.utils.TeleCareException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpServiceJPA {
    @Autowired
    OtpRepositoryJPA otpRepositoryJPA;

    @Autowired
    MessageService messageService;
    public Boolean existsById(OtpIdentify id) {
        return otpRepositoryJPA.existsById(id);
    }

    public OtpEntity getById(OtpIdentify id) {
        return otpRepositoryJPA.getOne(id);
    }

    public OtpEntity save(OtpEntity otp) {
        return otpRepositoryJPA.save(otp);
    }

    public void delete(OtpEntity otp) {
        otpRepositoryJPA.delete(otp);
    }

    public OtpDTO validateOtp(String phoneNumber, String otp, String language) throws TeleCareException {
        boolean valid = otpRepositoryJPA.existsByPhoneAndOtpAndConfirmType(phoneNumber, otp, Constants.CONFIRM_TYPE);
        if (!valid) {
            throw new TeleCareException(messageService.getMessage(Constants.INVALID_OTP, language));
//            FnCommon.throwsErrorAppByLang(ErrorApp.ERROR_INPUTPARAMS, messageService.getMessage(Constants.INVALID_OTP, language));
        }
        OtpDTO otpDTO = new OtpDTO();
        otpDTO.setValid(valid);
        return otpDTO;
    }
}
