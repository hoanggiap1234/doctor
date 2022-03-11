package com.viettel.etc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.viettel.etc.utils.Base64Util;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

/**
 * Autogen class DTO:
 *
 * @author ToolGen
 * @date Mon Sep 07 16:47:06 ICT 2020
 */
@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class WorkingSchedulesDTO {

	Integer calendarId;

	Integer doctorId;

	String fullName;

	String avatar;

	Integer genderId;

	String genderName;

	String phoneNumber;

	@JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Bangkok")
	Date birthday;

	Integer calendarType;

	String healthfacilitiesCode;

	String healthfacilitiesName;

	String healthfacilitiesCodeList;

	Integer timeslotId;

	Date calendarDate;

	Integer status;

	Integer startrecord;

	Integer pagesize;

	Boolean resultSqlEx;

	String queryString;

	@JsonProperty("timeslots")
	List<TimeSlotDTO> timeSlots;

	List<WorkingSchedulesDTO> workingSchedule;

	Date createDate;

	Boolean isDelete;

	Long fromTime;

	Long toTime;

	@JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Bangkok")
	Date date;

	public String getAvatar() {
		return (avatar == null || Base64Util.isBase64String(avatar)) ? avatar
				: Base64Util.toBase64(avatar);
	}

	String language;
}
