package com.viettel.etc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

/**
 * Autogen class DTO:
 *
 * @author ToolGen
 * @date Mon Sep 14 17:53:51 ICT 2020
 */
@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class DoctorsCalendarBookingTimesConsultantDTO {

    Integer timeslotId;

    Integer hoursStart;

    Integer minuteStart;

    Integer hoursEnd;

    Integer minuteEnd;

    Integer doctorId;

    Long calendarDate;

    Date calendar;

    Integer duration;

    String value;

    String period;

    String healthfacilitiesCode;

    String healthfacilitiesName;

    List<String> timeList;

    Boolean canBook;

    Integer maxNumberBookingDoctor;

    Integer startrecord;

    Integer pagesize;

    Boolean resultSqlEx;

    String language;
}
