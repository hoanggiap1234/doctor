package com.viettel.etc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Autogen class DTO:
 *
 * @author ToolGen
 * @date Mon Sep 14 10:35:03 ICT 2020
 */
@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class BookingDaysConsultantDoctorsDTO {

	Date calendarDate;

	Date day;

	Integer doctorId;

	Integer limitq;

//	@NotNull(message = "not null")
//	@NotEmpty(message = "not empty")
	String healthfacilitiesCode;

	Integer limitNumberDaysBooking;

	List<DaysDTO> listData;

	Integer count;

	Integer startrecord;

	Integer pagesize;

	Boolean resultSqlEx;
}
