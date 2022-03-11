package com.viettel.etc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimeSlotDTO {

	Integer calendarId;

	Integer doctorId;

	Integer timeslotId;

	String timeslotValue;

	Integer status;

	@JsonFormat(pattern = "dd/MM/yyyy", timezone = "Asia/Bangkok")
	Date date;

	Integer hoursStart;

	Integer minuteStart;

	Integer hoursEnd;

	Integer minuteEnd;

	String healthfacilitiesCode;

	String healthfacilitiesName;

	Integer calendarType;

}
