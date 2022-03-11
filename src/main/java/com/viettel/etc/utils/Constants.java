package com.viettel.etc.utils;

import com.squareup.okhttp.MediaType;

import java.util.Arrays;
import java.util.List;

public class Constants {

	public static final String REQUEST_MAPPING_V1 = "/api/v1";
	public static final String MOBILE = "/mobile";
	public static final MediaType JSON_TOKEN = MediaType
			.parse("application/x-www-form-urlencoded; charset=utf-8");
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	public static final Integer LIMIT_DEFAULT = 5; // default limit when get list data
	public static final Integer BOOKING_GROUP_ORDER = 1; // nhom dat kham
	public static final Integer BOOKING_GROUP_ADVISORY = 2; // nhom tu van
	public static final Integer PAGE_SIZE_DEFAULT = 20;
	public static final Integer START_RECORD_DEFAULT = 0;
	public static final boolean IS_NOT_DELETE = false;
	public static final boolean IS_DELETE = true;
	public static final boolean IS_ACTIVE = true;
	public static final Boolean IS_NOT_ACTIVE = false;
	public static final boolean IS_NOT_SYNC = false;
	public static final String DOCTOR_PW_DEFAULT = "vtsTele#!kjk20202";
	public static final Integer IMAGE_UPLOAD_MAX_SIZE = 10;
	public static final String IMAGE_UPLOAD_PATH = "/images/";
	public static final Integer OTP_DURATION = 2;
	public static final Integer STATUS_ACTIVE = 2;
	public static final Integer STATUS_WAITING = 1;
	public static final Integer STATUS_CONFIRM = 2;
	public static final String EXCEL_EXPORT_PATH = "/excel/";
	public static final String PATTERN_DATE_EXCEL_EXPORT = "yyyyMMdd_HHmm";
	public static final String PATTERN_SHORT_DATE = "dd/MM/yyyy";
	public static final String PATTERN_SHORT_DATE_WITHOUT_YEAR = "dd/MM";
	public static final String MALE_GENDER = "Nam";
	public static final Integer MALE_GENDER_ID = 1;
	public static final String FEMALE_GENDER = "Nữ";
	public static final Integer FEMALE_GENDER_ID = 2;
	public static final Integer DEFAULT_GENDER_ID = 3;
	public static final String YES_STR = "Có";
	public static final String NO_STR = "Không";
	public static final String DEFAULT_PW = "vtsTele#!kjk20202";
	public static String STR_EMTY = "";
	public static String FILE_MESS = "message";
	public static final int CONFIRM_TYPE = 1;
	public static final int DOCTOR_TYPE = 1;
	public static final boolean HAS_RELATIONSHIP = true;
	public static final String IMPORT_DOCTOR_PW_DEFAULT = "Hssk@#1234";

	public static final String VIETNAM_CODE = "vi";
	public static final String ENGLISH_CODE = "en";
	public static boolean IS_NOT_DELETE_BOOLEAN = false;
	
	public static final List<Integer> LIST_TIME_CONSULTANT = Arrays.asList(15, 30, 45, 60);
	public static final double TIME_CONSULTANT_MIN = 15;
	public static final Integer CALENDAR_TYPE_EXAM = 1;
	public static final Integer CALENDAR_TYPE_CONSULTANT = 2;
	public static final String IS_MEDICALEXAMINATION = "Có";
	public static final String IS_CONSULTANT = "Có";
	public static final String IS_ACTIVE_STRING = "Hiệu lực";

	// MESSAGE_CODE
	public static final String INVALID_OTP = "INVALID_OTP";
	public static final String ERR_USER_PERMISSION = "ERR_USER_PERMISSION";
	public static final String ERROR_INPUTPARAMS = "ERROR_INPUTPARAMS";
	public static final String ERR_DATA_BOOKING_TIME_INFO = "ERR_DATA_BOOKING_TIME_INFO";
	public static final String ERR_DATA_DOCTOR_CALENDAR_INFO = "ERR_DATA_DOCTOR_CALENDAR_INFO";
	public static final String ERROR_UNIQUE_CALENDAR_DATE_AND_TIMESLOT = "ERROR_UNIQUE_CALENDAR_DATE_AND_TIMESLOT";
	public static final String ERROR_EXIST_CALENDAR_DATE_AND_TIMESLOT = "ERROR_EXIST_CALENDAR_DATE_AND_TIMESLOT";
	public static final String ERR_DATA_CALENDAR_NOT_EXIST = "ERR_DATA_CALENDAR_NOT_EXIST";
	public static final String ERR_CANCEL_CALENDAR_STATUS = "ERR_CANCEL_CALENDAR_STATUS";
	public static final String ERR_CANCEL_CALENDAR_BEFORE = "ERR_CANCEL_CALENDAR_BEFORE";
	public static final String ERR_CANCEL_CALENDAR_BOOKING_EXISTS = "ERR_CANCEL_CALENDAR_BOOKING_EXISTS";
	public static final String ERR_DATA_CALENDAR_TYPE_EXIST = "ERR_DATA_CALENDAR_TYPE_EXIST";
	public static final String ERR_PHONE_NUMBER_INVALID = "ERR_PHONE_NUMBER_INVALID";
	public static final String ERR_PSW_INVALID = "ERR_PASSWORD_INVALID";
	public static final String ERR_NEW_PSW_EQUAL_OLD_PSW = "ERR_NEW_PASS_EQUAL_OLD_PASS";
	public static final String ERR_DATA_DOCTOR_NOT_EXIST = "ERR_DATA_DOCTOR_NOT_EXIST";
	public static final String ERR_WRONG_OLD_PSW = "ERR_WRONG_OLD_PASS";
	public static final String ERROR_DATA_OTP_EXIST = "ERROR_DATA_OTP_EXIST";
	public static final String ERROR_DATA_OTP_INVALID = "ERROR_DATA_OTP_INVALID";
	public static final String ERR_EMAIL_INVALID = "ERR_EMAIL_INVALID";
	public static final String ERR_IMAGE_FILE_INVALID = "ERR_IMAGE_FILE_INVALID";
	public static final String ERROR_DATA_LOCATION_INVALID = "ERROR_DATA_LOCATION_INVALID";
	public static final String ERROR_DATA_PHONE_NUMBER_EXIST = "ERROR_DATA_PHONE_NUMBER_EXIST";
	public static final String ERR_ADDRESS = "ERR_ADDRESS";
	public static final String ERROR_DATA_EMAIL_EXIST = "ERROR_DATA_EMAIL_EXIST";
	public static final String ERR_HEALTHFACILITIE_NOT_FOUND = "ERR_HEALTHFACILITIE_NOT_FOUND";
	public static final String ERR_SPECIAL_NOT_FOUND = "ERR_SPECIAL_NOT_FOUND";
	public static final String ERROR_RANGE_TIME_TOO_LONG = "ERROR_RANGE_TIME_TOO_LONG";
	public static final String ERROR_CONFIG_PRICE = "ERROR_CONFIG_PRICE";
	public static final String ERR_USER_NOT_EXIST = "ERR_USER_NOT_EXIST";
	public static final String ERROR_EMPTY_ANY_PRICE = "ERROR_EMPTY_ANY_PRICE";
	public static final String ERR_DATA_PATIENT_EMAIL_EXIST = "ERR_DATA_PATIENT_EMAIL_EXIST";
	public static final String CHANGE_PSW_FALSE = "CHANGE_PASSWORD_FALSE";
	public static final String ERROR_PATIENT_RELATIONSHIP_NOT_EXIST = "ERROR_PATIENT_RELATIONSHIP_NOT_EXIST";
	public static final String ERR_TIMESLOT_CALENDAR_CONFLICT_HEALTHFACILITIES_CALENDAR = "ERR_TIMESLOT_CALENDAR_CONFLICT_HEALTHFACILITIES_CALENDAR";
	public static final String GET_KEYCLOCK_FALSE = "GET_KEYCLOCK_FALSE";
	public static final int IMAGE_WIDTH = 480;
	public static final int IMAGE_SIZE = 300; // 300KB
	public static final String CREATE_DOCTOR_SUCCESSFULLY = "CREATE_DOCTOR_SUCCESSFULLY";
	public static final String DOCTOR_SAVE_FAIL = "DOCTOR_SAVE_FAIL";

	public static final String TIME_CONSULTANT_INVALID = "TIME_CONSULTANT_INVALID";
	public static final String ERROR_DATA_OTP_EXPIRED = "ERROR_DATA_OTP_EXPIRED";
	public static final String REGISTER_VIDEO_CALL_DEVICES_FALSE ="REGISTER_VIDEO_CALL_DEVICES_FALSE";
	public static final String ERROR_ALL_SELECTED_TIMESLOTS_ARE_CONFIRMED = "ERROR_ALL_SELECTED_TIMESLOTS_ARE_CONFIRMED";
	public static final String ERROR_INVALID_FILE_FORMAT = "ERROR_INVALID_FILE_FORMAT";
	public static final String ERROR_EXAM_FEE_INVALID = "ERROR_EXAM_FEE_INVALID";
	public static final String ERROR_VIDEO_CALL_FEE_INVALID = "ERROR_VIDEO_CALL_FEE_INVALID";
	public static final String ERROR_CALL_FEE_INVALID = "ERROR_CALL_FEE_INVALID";
	public static final String ERROR_CHAT_FEE_INVALID = "ERROR_CHAT_FEE_INVALID";
	public static final String ERROR_ALL_PRICE_ARE_INVALID = "ERROR_ALL_PRICE_ARE_INVALID";
	public static final String ERROR_EXAM_FEE_IS_CONFIGURED = "ERROR_EXAM_FEE_IS_CONFIGURED";
	public static final String ERROR_VIDEO_CALL_FEE_IS_CONFIGURED = "ERROR_VIDEO_CAL_FEE_IS_CONFIGURED";
	public static final String ERROR_CALL_FEE_IS_CONFIGURED = "ERROR_CALL_FEE_IS_CONFIGURED";
	public static final String ERROR_CHAT_FEE_IS_CONFIGURED = "ERROR_CHAT_FEE_IS_CONFIGURED";

	// format template file excel
	public static final String TEMPLATE_FILE_IMPORT_DOCTOR = "STT, Họ và tên*, Ngày tháng năm sinh (dd/MM/yyyy), Giới tính*,Mã tỉnh/TP*, Mã quận/huyện, Mã xã phường, Địa chỉ thôn/xóm/số nhà, Số điện thoại*, Email, Số GPHN, Ngày cấp (dd/MM/yyyy), Học hàm, Học vị, Quốc tịch*, Dân tộc, Tôn giáo, Chuyên khoa *, Trình độ chuyên môn/ kinh nghiệm, Ghi chú, Nơi công tác* (mã CSYT), Đăng ký khám bệnh, Đăng ký tư vấn, Trạng thái";
	public static final Integer FILE_TYPE_IMPORT_DOCTOR = 1; // doctor type
	public static final String VALID_FILE_IMPORT = "VALID_FILE_IMPORT";
	public static final String EXTENSION_FILE = "xlsx, xlsm, xlsb, xls";
	public static final String ERROR_INVALID_COLUMN_DATA = "ERROR_INVALID_COLUMN_DATA";
	public static final Integer STATUS_IMPORT_DOING = 0;
	public static final Integer STATUS_IMPORT_SUCCESS = 1;
	public static final Integer STATUS_IMPORT_FAIL = 2;

	//immunization
	public static final Integer RECEPTION_DONE_STEP_NUMBER = 2;
	public static final Integer DONE_IMMUNIZATION_STEP_NUMBER = 3;
	public static final String GET_DOCTOR_POSITION_PLAN_FAIL = "GET_DOCTOR_POSITION_PLAN_FAIL";
	public static final String CREATE_COVID_RECEPTION_SUCCESS = "CREATE_COVID_RECEPTION_SUCCESS";
	public static final String ERR_CONFIRM_RECEPTION_FAILED = "ERR_CONFIRM_RECEPTION_FAILED";

//    /**
//     * Ma loi tra ve cua ki tren di dong
//     */
//    public interface SIGN_CODE {


//
//        //Thanh cong
//        public static final Integer SUCCESS = 1;
//        //Loi cert het han
//        public static final Integer ERR_CODE_CEREXPIRE = 2;
//        //loi cert khong ton tai
//        public static final Integer ERR_CODE_NOTFOUND = 3;
//
//    }
}
