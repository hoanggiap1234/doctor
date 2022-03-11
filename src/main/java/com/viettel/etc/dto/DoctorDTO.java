package com.viettel.etc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.viettel.etc.utils.Base64Util;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Autogen class DTO: Lop thong tin cua bac si
 *
 * @author ToolGen
 * @date Wed Aug 19 14:21:50 ICT 2020
 */
@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class DoctorDTO {

	Integer doctorId;

	@NotNull
	String fullname;

	String description;

	String summary;

	String avatar;

	Integer academicRankId;

	String academicRankCode;

	String academicRankName;

	Integer degreeId;

	@NotNull
	Integer doctorType;

	String degreeCode;

	/* Nam sinh */
	Integer year;

	Integer medicalExaminationFee;

	String certificationCode;

	Date certificationDate;

	String degreeName;

	String wardCode;

	String wardName;

	String districtCode;

	String districtName;

//	@NotNull
	String provinceCode;

	String provinceName;

	String specialistId;

//	@NotNull
//	@NotEmpty
	List<Integer> specialistIds;

	@NotNull
	String phoneNumber;

	//	@NotNull
	String otp;

	String email;

	Date birthday;

	String specialistCode;

	String healthfacilitiesCode;

	String healthfacilitiesName;

	@NotNull
	@NotEmpty
	@JsonProperty("healthacilitiesCodes")
	List<String> healthacilitieCodes;

	String specialistName;

	Integer genderId;

	Boolean isMedicalexamination;

	Boolean isConsultant;

	Boolean isActive;

	String queryString;

	Integer bookingOrder;

	Integer bookingAdvisory;

	Integer startrecord;

	Integer pagesize;

	Boolean resultSqlEx;

	String positionCode;

	String doctorCode;

	Integer totalBooking;

	Integer limit;

	String fullName;

//	String healthfacilities;

	Integer experiences;

	String address;

	String nationCode;

	String ethnicityCode;

	String religionCode;

	String keycloakUserId;

	String nationName;

	String ethnicityName;

	String religionName;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	String password;
	List<SpecialistDTO> specialists;
	List<HealthFacilityDTO> healthfacilities;

	Double average;

	Integer totalComments;

	Integer orderBy;

	Float stars;
	public String getAvatar() {
		return (avatar == null || Base64Util.isBase64String(avatar)) ? avatar
				: Base64Util.toBase64(avatar);
	}

	String language;

	String provinceCodes;

	String districtCodes;

	String academicRankCodes;

	String degreeCodes;


	String userName;
	Integer specialistIdInt;
	String isActive1;

	// loc csyt cua danh sach bac si
	Boolean allowSearch;

	Boolean allowBooking;

	Boolean allowFilter;

	Boolean byOrderNumber;
}
