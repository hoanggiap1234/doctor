package com.viettel.etc.repositories.impl;

import com.viettel.etc.dto.BookingDoctorRequestDTO;
import com.viettel.etc.dto.BookingDoctorResponseDTO;
import com.viettel.etc.dto.HealthfacilitiesCalendarTimeSlotDTO;
import com.viettel.etc.repositories.BookingDoctorRepository;
import com.viettel.etc.utils.Constants;
import com.viettel.etc.utils.FnCommon;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
import com.viettel.etc.xlibrary.core.repositories.CommonDataBaseRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Repository
public class DoctorBookingRepositoryImpl extends CommonDataBaseRepository implements BookingDoctorRepository {

	@Override
	public ResultSelectEntity getBookingDaysDoctors(BookingDoctorRequestDTO dataParams) {

		StringBuilder sql = new StringBuilder();
		HashMap<String, Object> hashMapParams = new HashMap<>();

		sql.append("SELECT " +
				"   DATE(doctors_calendar.calendar_date) AS DAY, " +
				"   doctors_calendar.calendar_id, " +
				"   cats_healthfacilities_holidaie.from_date, " +
				"   cats_healthfacilities_holidaie.to_date, " +
				"   sys_configs_booking.limit_number_days_booking  " +
				"FROM " +
				" (SELECT * FROM doctors_calendars WHERE is_active = 1 AND is_delete = 0 AND STATUS = 2 AND calendar_type = 1 AND calendar_date>= CURDATE() ");
		if (Objects.nonNull(dataParams.getDoctorId())) {
			sql.append("    AND doctor_id = :doctorId");
			hashMapParams.put("doctorId", dataParams.getDoctorId());
		}
		if (Objects.nonNull(dataParams.getHealthfacilitiesCode())) {
			sql.append("    AND healthfacilities_code = :healthfacilitiesCode");
			hashMapParams.put("healthfacilitiesCode", dataParams.getHealthfacilitiesCode());
		}
		sql.append(" ) AS doctors_calendar");
		sql.append("   JOIN " +
				"      doctors_healthfacilities AS doctors_healthfacilitie  " +
				"      ON doctors_calendar.doctor_id = doctors_healthfacilitie.doctor_id  " +
				"      AND doctors_healthfacilitie.is_delete = 0  " +
				"      AND doctors_healthfacilitie.is_active = 1  " +
				"   join " +
				"      cats_healthfacilities_calendars chc  " +
				"      on chc.healthfacilities_code = doctors_healthfacilitie.healthfacilities_code  " +
				"      and  " +
				"      ( " +
				"         WEEKDAY(doctors_calendar.calendar_date) + 2 " +
				"      ) " +
				"      = chc.day_code  " +
				"      and chc.is_active = 1  " +
				"      and chc.is_delete = 0  " +
				"   LEFT JOIN " +
				"      sys_configs_booking  " +
				"      ON doctors_healthfacilitie.healthfacilities_code = sys_configs_booking.healthfacilities_code  " +
				"      AND sys_configs_booking.is_delete = 0  " +
				"      AND sys_configs_booking.is_active = 1  " +
				"   LEFT JOIN " +
				"      cats_healthfacilities_holidaies AS cats_healthfacilities_holidaie  " +
				"      ON cats_healthfacilities_holidaie.healthfacilities_code = doctors_healthfacilitie.healthfacilities_code  " +
				"      AND cats_healthfacilities_holidaie.is_delete = 0  " +
				"      AND cats_healthfacilities_holidaie.is_active = 1  " +
				"WHERE " +
				"( " +
				"      case " +
				"         when " +
				"            cats_healthfacilities_holidaie.from_date is null  " +
				"         then " +
				"            true  " +
				"         else " +
				"            DATE(doctors_calendar.calendar_date) < DATE(cats_healthfacilities_holidaie.from_date)  " +
				"      end " +
				")  " +
				"      or  " +
				"      ( " +
				"         case " +
				"            when " +
				"               cats_healthfacilities_holidaie.to_date is null  " +
				"            then " +
				"               true  " +
				"            else " +
				"               DATE(doctors_calendar.calendar_date) > DATE(cats_healthfacilities_holidaie.to_date)  " +
				"         end " +
				"      ) " +
				"   AND CURDATE() <= DATE(doctors_calendar.calendar_date)  " +
				"   AND DATE(doctors_calendar.calendar_date) < ADDDATE(CURDATE(), INTERVAL sys_configs_booking.limit_number_days_booking DAY) ");

		sql.append(" GROUP BY doctors_calendar.calendar_date ");
		sql.append(" ORDER BY doctors_calendar.calendar_date ");

		Integer start = Objects.nonNull(dataParams.getStartrecord()) ? dataParams.getStartrecord() : Constants.START_RECORD_DEFAULT;
		Integer pageSize = Objects.nonNull(dataParams.getPagesize()) ? dataParams.getPagesize() : Constants.PAGE_SIZE_DEFAULT;

		ResultSelectEntity resultSelectEntity = getListDataAndCount(sql, hashMapParams, start, pageSize, BookingDoctorResponseDTO.class);
		return resultSelectEntity;
	}

	@Override
	public List<HealthfacilitiesCalendarTimeSlotDTO> getBookingHoursDoctors(BookingDoctorRequestDTO dataParams) {

		StringBuilder sql = new StringBuilder();
		HashMap<String, Object> hashMapParams = new HashMap<>();

		sql.append(" SELECT  ");
		sql.append(" CAST(DATE_FORMAT(cats_healthfacilities_calendar.am_from, '%H') AS INT) AS amFromHouse, ");
		sql.append(" CAST(DATE_FORMAT(cats_healthfacilities_calendar.am_from, '%i') AS INT) AS amFromMinute, ");
		sql.append(" CAST(DATE_FORMAT(cats_healthfacilities_calendar.am_to, '%H') AS INT) AS amToHouse, ");
		sql.append(" CAST(DATE_FORMAT(cats_healthfacilities_calendar.am_to, '%i') AS INT) AS amToMinute, ");
		sql.append(" CAST(DATE_FORMAT(cats_healthfacilities_calendar.pm_from, '%H') AS INT) AS pmFromHouse, ");
		sql.append(" CAST(DATE_FORMAT(cats_healthfacilities_calendar.pm_from, '%i') AS INT) AS pmFromMinute, ");
		sql.append(" CAST(DATE_FORMAT(cats_healthfacilities_calendar.pm_to, '%H') AS INT) AS pmToHouse, ");
		sql.append(" CAST(DATE_FORMAT(cats_healthfacilities_calendar.pm_to, '%i') AS INT) AS pmToMinute, ");
		sql.append(" sys_configs_booking.time_frame AS timeFrame, ");
		sql.append(" sys_configs_booking.max_number_booking_service AS maxNumberBookingService ");
		sql.append(" FROM cats_healthfacilities_calendars AS cats_healthfacilities_calendar ");
		sql.append(" LEFT JOIN doctors_calendars AS doctors_calendar ON doctors_calendar.healthfacilities_code = cats_healthfacilities_calendar.healthfacilities_code ");
		sql.append(" AND doctors_calendar.is_delete = 0 ");
		sql.append(" AND doctors_calendar.is_active = 1 ");
		sql.append(" LEFT JOIN doctors_healthfacilities AS doctors_healthfacilitie ON doctors_healthfacilitie.doctor_id = doctors_calendar.doctor_id ");
		sql.append(" AND doctors_healthfacilitie.is_delete = 0 ");
		sql.append(" AND doctors_healthfacilitie.is_active = 1 ");
		sql.append(" LEFT JOIN sys_configs_booking ON sys_configs_booking.healthfacilities_code = doctors_healthfacilitie.healthfacilities_code ");
		sql.append(" AND sys_configs_booking.is_delete = 0 ");
		sql.append(" AND sys_configs_booking.is_active = 1 ");
		sql.append(" WHERE cats_healthfacilities_calendar.is_delete = 0 ");
		sql.append(" AND cats_healthfacilities_calendar.is_active = 1 ");

		if (Objects.nonNull(dataParams.getHealthfacilitiesCode())) {
			sql.append(" AND cats_healthfacilities_calendar.healthfacilities_code = :healthfacilitiesCode ");
			hashMapParams.put("healthfacilitiesCode", dataParams.getHealthfacilitiesCode());
		}

		if (Objects.nonNull(dataParams.getDay())) {
			sql.append(" AND DATE(doctors_calendar.calendar_date) = :calendarDate ");
			hashMapParams.put("calendarDate", Date.valueOf(FnCommon.convertToLocalDate(dataParams.getDay())));
		}

		if (Objects.nonNull(dataParams.getDoctorId())) {
			sql.append(" AND doctors_calendar.doctor_id = :doctorId ");
			hashMapParams.put("doctorId", dataParams.getDoctorId());
		}

		sql.append(" GROUP BY cats_healthfacilities_calendar.calendar_id ");

		List<HealthfacilitiesCalendarTimeSlotDTO> resultData = (List<HealthfacilitiesCalendarTimeSlotDTO>) getListData(sql, hashMapParams, null, null, HealthfacilitiesCalendarTimeSlotDTO.class);
		return resultData;
	}

	@Override
	public List<HealthfacilitiesCalendarTimeSlotDTO> getBookingHoursDoctor(BookingDoctorRequestDTO dataParams) throws ParseException {

		StringBuilder sql = new StringBuilder();
		HashMap<String, Object> hashMapParams = new HashMap<>();

		sql.append(" select dc.healthfacilities_code as  healthfacilitiesCode, ch.name as healthfacilitiesName, ");
		sql.append(" ct.hours_start as hourStart, ct.minute_start as minuteStart, ct.hours_end as hourEnd, ct.minute_end as minuteEnd, scb.time_frame as timeFrame, scb.max_number_booking_doctor as maxNumberBookingService ");
		sql.append(" from (SELECT * FROM doctors_calendars where is_active = 1 and is_delete = 0 and calendar_type = 1 ");
		if (Objects.nonNull(dataParams.getHealthfacilitiesCode())) {
			sql.append(" and healthfacilities_code = :healthfacilitiesCode");
			hashMapParams.put("healthfacilitiesCode", dataParams.getHealthfacilitiesCode());
		}

		if (Objects.nonNull(dataParams.getDay())) {
			sql.append(" and calendar_date = :day");
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			hashMapParams.put("day", formatter.parse(formatter.format(new java.util.Date(dataParams.getDay()))));
		}

		if (Objects.nonNull(dataParams.getDoctorId())) {
			sql.append(" and doctor_id = :doctorId");
			hashMapParams.put("doctorId", dataParams.getDoctorId());
		}
		sql.append(") dc");
		sql.append(" left join (select  healthfacilities_code, name from cats_healthfacilities where is_active = 1 and is_delete = 0) ch on dc.healthfacilities_code = ch.healthfacilities_code ");
		sql.append(" left join (select timeslot_id, hours_start, minute_start, hours_end, minute_end, healthfacilities_code from  cats_timeslots where is_active = 1 and is_delete = 0) ct on ");
		sql.append(" ct.timeslot_id = dc.timeslot_id and ct.healthfacilities_code = dc.healthfacilities_code ");
		sql.append(" join (select healthfacilities_code, day_code, am_from, am_to, pm_from, pm_to from cats_healthfacilities_calendars where is_active = 1 and is_delete = 0) chc on ");
		sql.append(" chc.healthfacilities_code = ct.healthfacilities_code and DAYOFWEEK(:day) = chc.day_code ");
		sql.append(" and ((convert(CONCAT(if(hours_start >= 10, hours_start, CONCAT(0, hours_start)), \":\", if(minute_start >= 10, minute_start, CONCAT(0, minute_start)), \":00\"), TIME) >= chc.am_from ");
		sql.append(" and convert(CONCAT(if(hours_end >= 10, hours_end, CONCAT(0, hours_end)), \":\", if(minute_end >= 10, minute_end, CONCAT(0, minute_end)), \":00\"), TIME) <= chc.am_to ) ");
		sql.append(" or (convert(CONCAT(if(hours_start >= 10, hours_start, CONCAT(0, hours_start)), \":\", if(minute_start >= 10, minute_start, CONCAT(0, minute_start)), \":00\"), TIME) >= chc.pm_from ");
		sql.append(" and convert(CONCAT(if(hours_end >= 10, hours_end, CONCAT(0, hours_end)), \":\", if(minute_end >= 10, minute_end, CONCAT(0, minute_end)), \":00\"), TIME) <= chc.pm_to)) ");
		sql.append(" left join (select healthfacilities_code, time_frame, max_number_booking_doctor from sys_configs_booking where is_active = 1 and is_delete = 0 GROUP BY healthfacilities_code) scb on ");
		sql.append(" scb.healthfacilities_code = dc.healthfacilities_code ");

		List<HealthfacilitiesCalendarTimeSlotDTO> resultData = (List<HealthfacilitiesCalendarTimeSlotDTO>) getListData(sql, hashMapParams, null, null, HealthfacilitiesCalendarTimeSlotDTO.class);
		return resultData;
	}


}
