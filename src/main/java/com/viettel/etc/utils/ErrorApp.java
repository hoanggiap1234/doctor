package com.viettel.etc.utils;

public enum ErrorApp {

	ERROR_INPUTPARAMS(400, FnCommon.getValueFileMess("err.data")),
	ERROR_RANGE_TIME_TOO_LONG(400, FnCommon.getValueFileMess("err.error_range_time_too_long")),
	ERROR_CONFIG_PRICE(400, FnCommon.getValueFileMess("err.data.config.price")),
	ERR_JSON_PARSE(400, FnCommon.getValueFileMess("err.json.parse")),
	ERR_DATA_DOCTOR_NOT_EXIST(404, FnCommon.getValueFileMess("err.data.doctor.not.exist")),
	ERR_USER_NOT_EXIST(404, FnCommon.getValueFileMess("err.user.not.exist")),
	ERROR_DATA_PHONE_NUMBER_EXIST(400, FnCommon.getValueFileMess("err.validate.phone.number.exist")),
	ERROR_DATA_LOCATION_INVALID(400, FnCommon.getValueFileMess("err.validate.location.invalid")),
	ERROR_DATA_OTP_EXIST(400, FnCommon.getValueFileMess("err.validate.otp.exist")),
	ERROR_DATA_OTP_INVALID(400, FnCommon.getValueFileMess("err.validate.otp.invalid")),
	ERROR_DATA_EMAIL_EXIST(400, FnCommon.getValueFileMess("err.validate.email.exist")),
	ERROR_EMPTY_ANY_PRICE(400, FnCommon.getValueFileMess("err.empty.any.prices")),
	ERR_DATA_PATIENT_EMAIL_EXIST(400, FnCommon.getValueFileMess("validate.patient.email.empty")),
	ERR_HEALTHFACILITIE_NOT_FOUND(400, FnCommon.getValueFileMess("err.validate.healthfacilitie.not_found")),
	ERR_SPECIAL_NOT_FOUND(400, FnCommon.getValueFileMess("err.validate.special.not_found")),
	MESAGE_DELETE_SUCCESS(1, FnCommon.getValueFileMess("msg.delete.success")),
	GET_KEYCLOCK_FALSE(200, FnCommon.getValueFileMess("msg.get_keycloak.err")),
	ERR_DATA_DOCTOR_CALENDAR_INFO(400, FnCommon.getValueFileMess("validate.doctor_calendar.empty")),
	ERR_IMAGE_FILE_INVALID(400, FnCommon.getValueFileMess("err.data.image.invalid")),
	ERR_DATA_BOOKING_TIME_INFO(400, FnCommon.getValueFileMess("validate.booking_time.empty")),
	ERR_UNKNOW(500, FnCommon.getValueFileMess("err.unknow")),
	ERR_ADDRESS(400, FnCommon.getValueFileMess("err.address")),
	ERR_PHONE_NUMBER_INVALID(400, FnCommon.getValueFileMess("err.phone_number.invalid")),
	ERR_PASSWORD_INVALID(400, FnCommon.getValueFileMess("err.password.invalid")),
	ERR_EMAIL_INVALID(400, FnCommon.getValueFileMess("err.email.invalid")),
	ERROR_UNIQUE_CALENDAR_DATE_AND_TIMESLOT(400, FnCommon.getValueFileMess("err.data.unique.working.schedule")),
	ERROR_EXIST_CALENDAR_DATE_AND_TIMESLOT(400, FnCommon.getValueFileMess("err.data.exits.working.schedule.unique")),
	REGISTER_VIDEO_CALL_DEVICES_FALSE(400, FnCommon.getValueFileMess("err.register_video_call_devices_false")),
	ERR_USER_PERMISSION(403, FnCommon.getValueFileMess("message.user.permission.denied")),
	TIME_CONSULTANT_INVALID(400, FnCommon.getValueFileMess("time.consultant.invalid")),
	ERR_DATA_CALENDAR_NOT_EXIST(404, FnCommon.getValueFileMess("err.calendar_not_exists")),
	ERR_CANCEL_CALENDAR_BEFORE(400, FnCommon.getValueFileMess("err.cancel_calendar_before")),
	ERR_CANCEL_CALENDAR_BOOKING_EXISTS(400, FnCommon.getValueFileMess("err.cancel_calendar_booking_exists")),
	ERR_CANCEL_CALENDAR_STATUS(400, FnCommon.getValueFileMess("err.cancel_calendar_status")),
	ERR_DATA_CALENDAR_TYPE_EXIST(400, FnCommon.getValueFileMess("err.calendar_type.exist")),
	ERR_NEW_PASS_EQUAL_OLD_PASS(400, FnCommon.getValueFileMess("err.new_pass.equal.old_pass")),
	ERR_WRONG_OLD_PASS(400, FnCommon.getValueFileMess("err.wrong.password")),
	CHANGE_PASSWORD_FALSE(400, FnCommon.getValueFileMess("change.password.false")),
	CHANGE_PASSWORD_SUCCESS(1, FnCommon.getValueFileMess("change.password.success")),
	ERROR_PATIENT_RELATIONSHIP_NOT_EXIST(400, FnCommon.getValueFileMess("err.patient.relationship.not.exist")),
	REQUEST_FAIL(504, "err.unknow"),
	GET_INFO_RESIDENT_FAIL(400, FnCommon.getValueFileMess("get.info.resident.fail")),
	CREATED_COVID_PATIENT_ALREADY(400, ""),
	CREATED_IMMUNIZATION_RESULT_ALREADY(400, ""),
	ERR_CREATED_IMMUNIZATION_RESULT_FAIL(400, ""),
	UNKNOW_ERROR(400, FnCommon.getValueFileMess("err.unknow")),
	REQUEST_TO_HSSK_FAIL(504, ""),
	SUCCESS_CREATE_COVID_IMMUNIZATION_RESULT(1, ""),
	;

	private int code;
	private String description;

	private ErrorApp(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
//	public String toStringJson() {
//		Map<String, Object> objectMap = new HashMap<>();
//		objectMap.put("code", code);
//		objectMap.put("description", description);
//		return FnCommon.convertObjectToStringJson(objectMap);
//	}
}
