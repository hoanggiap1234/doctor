package com.viettel.etc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * Autogen class DTO:
 *
 * @author ToolGen
 * @date Mon Sep 07 16:47:06 ICT 2020
 */
@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class DoctorsCalendarsDTO {

	Integer calendarId;

	String doctorIds;

	Integer doctorId;

	String healthfacilitiesCode;

	Integer timeslotId;

	Date calendarDate;

	Integer status;

	Integer calendarType;

	String reasonReject;

	Boolean isDelete;

	Boolean isActive;

	Integer createUserId;

	Date createDate;

	Integer updateUserId;

	Date updateDate;

	Integer approveUserId;

	Date approveDate;

	Integer hoursStart;

	Integer minuteStart;

	Integer hoursEnd;

	Integer minuteEnd;

	Long fromDate;

	Long toDate;

	String language;
}
