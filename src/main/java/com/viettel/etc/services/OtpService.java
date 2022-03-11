package com.viettel.etc.services;

import com.viettel.etc.utils.TeleCareException;

public interface OtpService {
	Object requestOtpRegister(String phone, String language) throws TeleCareException;

	Object requestOtpValidate(String phone) throws TeleCareException;
}
