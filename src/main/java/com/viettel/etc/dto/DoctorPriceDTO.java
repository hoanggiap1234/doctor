package com.viettel.etc.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.viettel.etc.utils.Base64Util;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Autogen class DTO:
 *
 * @author ToolGen
 * @date Mon Sep 07 14:45:01 ICT 2020
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DoctorPriceDTO {

	@NotNull
	private Integer phaseId;

	private Integer specialistId;

	private String healthfacilitiesCode;

	private String fullname;

	private String phoneNumber;

	private Integer priceId;

	@NotNull
	private Integer doctorId;

	private String specialistName;

	private String avatar;

	private Integer medicalExaminationFee;

	private Integer medicalExaminationNumber;

	private Double consultantFeeVideocall;

	private Integer consultantTimeVideocall;

	private Double consultantFeeCall;

	private Integer consultantTimeCall;

	private Double consultantFeeChat;

	private Integer consultantChatNumber;

	@JsonIgnore
	private Integer startrecord;

	@JsonIgnore
	private Integer pagesize;

	@JsonIgnore
	private Boolean resultSqlEx;

	public String getAvatar() {
		return (avatar == null || Base64Util.isBase64String(avatar)) ? avatar
				: Base64Util.toBase64(avatar);
	}

	private String language;

	private double multiTimeCall;

	private double multiTimeVideoCall;

	private Integer phase_id;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DoctorPriceDTO that = (DoctorPriceDTO) o;
		return Objects.equals(healthfacilitiesCode, that.healthfacilitiesCode) && doctorId.equals(that.doctorId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(healthfacilitiesCode, doctorId);
	}
}
