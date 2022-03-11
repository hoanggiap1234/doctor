package com.viettel.etc.services.impl;

import com.viettel.etc.repositories.tables.entities.OtpEntity;
import com.viettel.etc.repositories.tables.entities.OtpIdentify;
import com.viettel.etc.services.MessageService;
import com.viettel.etc.services.OtpService;
import com.viettel.etc.services.tables.DoctorsServiceJPA;
import com.viettel.etc.services.tables.OtpServiceJPA;
import com.viettel.etc.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpServiceImpl implements OtpService {
	@Autowired
	OtpServiceJPA otpServiceJPA;

	@Autowired
	DoctorsServiceJPA doctorsServiceJPA;

	@Autowired
	private MessageService messageService;

	String sms = FnCommon.getPropertiesValue("sms.user.request-otp");

	@Override
	public Object requestOtpRegister(String phone, String language) throws TeleCareException {
		if (doctorsServiceJPA.existByPhone(phone)) {
//			FnCommon.throwsErrorApp(ErrorApp.ERROR_DATA_PHONE_NUMBER_EXIST);
			throw new TeleCareException(messageService.getMessage(Constants.ERROR_DATA_PHONE_NUMBER_EXIST, language), ErrorApp.ERROR_DATA_PHONE_NUMBER_EXIST);
		}
		OtpIdentify id = new OtpIdentify(phone, OtpIdentify.DOCTOR_REGISTER);
		if (otpServiceJPA.existsById(id)) {
			OtpEntity otpEntity = otpServiceJPA.getById(id);
			otpServiceJPA.delete(otpEntity);
		}
		String code = FnCommon.generationPasswordApp();
		OtpEntity data = new OtpEntity();
		data.setPhone(id.getPhone());
		data.setConfirmType(id.getConfirmType());
		data.setOtp(code);
		data.setDuration(Constants.OTP_DURATION);
		String contentSMS = String.format(sms, code);
		SendSMS.sendAsyncDev(phone, contentSMS);
		otpServiceJPA.save(data);
		return "";
	}

	@Override
	public Object requestOtpValidate(String phone) {
		OtpIdentify id = new OtpIdentify(phone, OtpIdentify.DOCTOR_REGISTER);
		if (otpServiceJPA.existsById(id)) {
			OtpEntity otpEntity = otpServiceJPA.getById(id);
			otpServiceJPA.delete(otpEntity);
		}
		String code = FnCommon.generationPasswordApp();
		OtpEntity data = new OtpEntity();
		data.setPhone(id.getPhone());
		data.setConfirmType(id.getConfirmType());
		data.setOtp(code);
		data.setDuration(Constants.OTP_DURATION);
		String contentSMS = String.format(sms, code);
		SendSMS.sendAsyncDev(phone, contentSMS);
		otpServiceJPA.save(data);
		return "";
	}

}
