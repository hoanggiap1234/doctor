package com.viettel.etc.repositories.impl;

import com.viettel.etc.dto.DoctorsCalendarsDTO;
import com.viettel.etc.dto.TimeSlotDTO;
import com.viettel.etc.dto.WorkingSchedulesDTO;
import com.viettel.etc.kafka.multipleconsumer.CreateCinicAccountConsumer;
import com.viettel.etc.repositories.WorkingSchedulesRepository;
import com.viettel.etc.repositories.tables.SysUsersRepositoryJPA;
import com.viettel.etc.utils.Constants;
import com.viettel.etc.utils.FnCommon;
import com.viettel.etc.utils.TeleCareException;
import com.viettel.etc.utils.TelecareUserEntity;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
import com.viettel.etc.xlibrary.core.repositories.CommonDataBaseRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.Date;
import java.util.*;

/**
 * Autogen class Repository Impl:
 *
 * @author ToolGen
 * @date Mon Sep 07 16:47:06 ICT 2020
 */
@Repository
public class WorkingSchedulesRepositoryImpl extends CommonDataBaseRepository implements WorkingSchedulesRepository {
	@Autowired
	SysUsersRepositoryJPA sysUsersRepositoryJPA;

//	private static final Logger LOGGER = Logger.getLogger(WorkingSchedulesRepositoryImpl.class);
	/**
	 * Lich lam viec cua cac bac si
	 *
	 * @param itemParamsEntity: params client truyen len
	 * @return
	 */
	@Override
	public ResultSelectEntity getWorkingSchedules(WorkingSchedulesDTO itemParamsEntity, TelecareUserEntity loginUser) throws TeleCareException {
		StringBuilder sql = new StringBuilder();
		HashMap<String, Object> hmapParams = new HashMap<>();
		sql.append("select * from (");
		sql.append(" SELECT  ");
		sql.append(" GROUP_CONCAT(distinct doctors_healthfacilitie.healthfacilities_code SEPARATOR ',' ) AS healthfacilitiesCodeList, ");
		sql.append(" doctor.doctor_id as doctorId, ");
		sql.append(" doctor.fullname as fullName, ");
		sql.append(" doctor.avatar as avatar, ");
		sql.append(" doctor.gender_id as genderId, ");
		sql.append(" cats_gender.name AS genderName, ");
		sql.append(" doctor.phone_number as phoneNumber, ");
		sql.append(" doctor.birthday as birthday, ");
		sql.append(" doctors_healthfacilitie.healthfacilities_code as healthfacilitiesCode, ");
		sql.append(" cats_healthfacilitie.name AS healthfacilitiesName ");
		sql.append(" FROM doctors doctor ");
		sql.append(" LEFT JOIN doctors_healthfacilities AS doctors_healthfacilitie ON doctors_healthfacilitie.doctor_id = doctor.doctor_id ");
		sql.append(" AND doctors_healthfacilitie.is_delete = 0 ");
		sql.append(" AND doctors_healthfacilitie.is_active = 1 ");
		sql.append(" LEFT JOIN cats_healthfacilities AS cats_healthfacilitie ON cats_healthfacilitie.healthfacilities_code = doctors_healthfacilitie.healthfacilities_code ");
		sql.append(" AND cats_healthfacilitie.is_delete = 0 ");
		sql.append(" AND cats_healthfacilitie.is_active = 1 ");
		sql.append(" LEFT JOIN cats_genders AS cats_gender ON cats_gender.gender_id = doctor.gender_id ");
		sql.append(" AND cats_gender.is_active = 1  ");
		sql.append(" AND cats_gender.is_delete = 0 ");
		sql.append(" LEFT JOIN doctors_calendars AS doctors_calendar ON doctors_calendar.doctor_id = doctor.doctor_id ");
		sql.append(" AND doctors_calendar.is_delete = 0 ");
		sql.append(" AND doctors_calendar.is_active = 1 ");
		sql.append(" left join cats_timeslots as cats_timeslot on cats_timeslot.timeslot_id = doctors_calendar.timeslot_id ");
		sql.append(" and cats_timeslot.is_active = 1 and cats_timeslot.is_delete = 0 ");

		if (loginUser.isDoctor()) {
			sql.append(" AND doctor.doctor_id = :loginUserId ");
			hmapParams.put("loginUserId", loginUser.getTelecareUserId());
		} else if (loginUser.isClinic()) {
			String codes = sysUsersRepositoryJPA.getHealthfacilitiesCodes(loginUser.getKeycloakUserId());
			if (codes == null) {
				sql.append("and 1=2 ");
			} else {
				sql.append("AND doctors_healthfacilitie.healthfacilities_code in (").append(codes).append(") ");
			}
		}
		if (itemParamsEntity != null && itemParamsEntity.getFromTime() != null) {
			sql.append(" AND doctors_calendar.calendar_date >= :fromTime");
			hmapParams.put("fromTime", Date.valueOf(FnCommon.convertToLocalDate(itemParamsEntity.getFromTime())));
		}

		if (itemParamsEntity != null && itemParamsEntity.getToTime() != null) {
			sql.append(" AND doctors_calendar.calendar_date <= :toTime");
			hmapParams.put("toTime", Date.valueOf(FnCommon.convertToLocalDate(itemParamsEntity.getToTime())));
		}

		if (itemParamsEntity != null && itemParamsEntity.getCalendarType() != null) {
			sql.append(" AND doctors_calendar.calendar_type = :calendarType");
			hmapParams.put("calendarType", itemParamsEntity.getCalendarType());
		}

		if (itemParamsEntity != null && itemParamsEntity.getStatus() != null) {
			sql.append(" AND doctors_calendar.status = :status");
			hmapParams.put("status", itemParamsEntity.getStatus());
		}
		sql.append(" WHERE  ");
		sql.append(" doctor.is_active = 1  ");
		sql.append(" AND doctor.is_delete = 0  ");
		sql.append(" and cats_timeslot.hours_start is not null and cats_timeslot.hours_end is not null ");

		if (itemParamsEntity != null && itemParamsEntity.getDoctorId() != null) {
			sql.append(" AND doctor.doctor_id = :doctorId ");
			hmapParams.put("doctorId", itemParamsEntity.getDoctorId());
		}

		if (itemParamsEntity != null && itemParamsEntity.getQueryString() != null) {
			sql.append(" AND ((doctor.fullname like CONCAT('%', :queryString, '%')) OR (doctor.phone_number like CONCAT('%', :queryString, '%'))) ");
			hmapParams.put("queryString", itemParamsEntity.getQueryString());
		}

		if (itemParamsEntity != null && itemParamsEntity.getHealthfacilitiesCode() != null) {
			sql.append(" AND doctors_healthfacilitie.healthfacilities_code = :healthfacilitiesCode");
			hmapParams.put("healthfacilitiesCode", itemParamsEntity.getHealthfacilitiesCode());
		}

		sql.append(" group by " +
				"doctors_calendar.healthfacilities_code, " +
				"doctors_calendar.calendar_type, " +
				"doctors_calendar.calendar_date, " +
				"doctors_calendar.timeslot_id, " +
				"doctors_calendar.status ");
		sql.append(" ORDER BY doctor.fullname ASC ) result group by result.doctorId ");

		Integer start = Objects.nonNull(itemParamsEntity.getStartrecord()) ? itemParamsEntity.getStartrecord() : Constants.START_RECORD_DEFAULT;
		Integer pageSize = Objects.nonNull(itemParamsEntity.getPagesize()) ? itemParamsEntity.getPagesize() : Constants.PAGE_SIZE_DEFAULT;

		ResultSelectEntity resultSelectEntity = getListDataAndCount(sql, hmapParams, start, pageSize, WorkingSchedulesDTO.class);

		return resultSelectEntity;
	}

	private List<WorkingSchedulesDTO> getData(List<WorkingSchedulesDTO> listData, WorkingSchedulesDTO itemParamsEntity, boolean flag, boolean checkUpdate) {
		if (CollectionUtils.isEmpty(listData)) {
			return Collections.EMPTY_LIST;
		}
		listData.forEach(workingSchedulesDTO -> {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT dc.calendar_id as calendarId, dc.timeslot_id as timeslotId, dc.`status` as status, dc.calendar_date as calendarDate, ");
			sql.append(" cats_timeslot.hours_start AS hoursStart, ");
			sql.append(" cats_timeslot.minute_start AS minuteStart, ");
			sql.append(" cats_timeslot.hours_end AS hoursEnd, ");
			sql.append(" cats_timeslot.minute_end AS minuteEnd ");
			sql.append(" FROM doctors_calendars AS dc");
			sql.append(" LEFT JOIN cats_timeslots AS cats_timeslot ON cats_timeslot.timeslot_id = dc.timeslot_id ");
			sql.append("   AND cats_timeslot.is_active = 1 ");
			sql.append("   AND cats_timeslot.is_delete = 0 ");
			sql.append(" WHERE dc.is_active = 1 AND dc.is_delete = 0 ");

			HashMap<String, Object> hmapParams = new HashMap<>();

			if (workingSchedulesDTO != null && workingSchedulesDTO.getDoctorId() != null) {
				sql.append(" AND dc.doctor_id = :doctorId");
				hmapParams.put("doctorId", workingSchedulesDTO.getDoctorId());
			}

			if (workingSchedulesDTO != null && workingSchedulesDTO.getHealthfacilitiesCode() != null) {
				sql.append(" AND dc.healthfacilities_code = :healthfacilitiesCode");
				hmapParams.put("healthfacilitiesCode", workingSchedulesDTO.getHealthfacilitiesCode());
			}

			if (workingSchedulesDTO != null && workingSchedulesDTO.getCalendarDate() != null) {
				sql.append(" AND dc.calendar_date = :calendarDate");
				hmapParams.put("calendarDate", workingSchedulesDTO.getCalendarDate());
			}

			if (workingSchedulesDTO != null && workingSchedulesDTO.getCalendarType() != null) {
				sql.append(" AND dc.calendar_type = :calendarType");
				hmapParams.put("calendarType", workingSchedulesDTO.getCalendarType());
			}

			if (itemParamsEntity != null && itemParamsEntity.getStatus() != null) {
				sql.append(" AND dc.`status` = :status");
				hmapParams.put("status", itemParamsEntity.getStatus());
			}

			Integer start = Constants.START_RECORD_DEFAULT;
			if (itemParamsEntity != null && itemParamsEntity.getStartrecord() != null) {
				start = itemParamsEntity.getStartrecord();
			}
			Integer pageSize = Constants.PAGE_SIZE_DEFAULT;
			if (itemParamsEntity != null && itemParamsEntity.getPagesize() != null) {
				pageSize = itemParamsEntity.getPagesize();
			}

			List<DoctorsCalendarsDTO> data = (List<DoctorsCalendarsDTO>) getListData(sql, hmapParams, start, pageSize, DoctorsCalendarsDTO.class);
			List<TimeSlotDTO> timeSlotDTO = new ArrayList<>();
			data.forEach(doctorsCalendarsEntity -> {
				TimeSlotDTO timeSlotDTO1 = new TimeSlotDTO();
				timeSlotDTO1.setTimeslotId(doctorsCalendarsEntity.getTimeslotId());
				timeSlotDTO1.setStatus(doctorsCalendarsEntity.getStatus());
				timeSlotDTO1.setTimeslotValue(FnCommon.formatTimeslot(doctorsCalendarsEntity.getHoursStart(), doctorsCalendarsEntity.getMinuteStart(),
						doctorsCalendarsEntity.getHoursEnd(), doctorsCalendarsEntity.getMinuteEnd()));
				if (checkUpdate) {
					timeSlotDTO1.setCalendarId(doctorsCalendarsEntity.getCalendarId());
				}
				timeSlotDTO.add(timeSlotDTO1);
			});
			WorkingSchedulesDTO workingSchedules = new WorkingSchedulesDTO();
			workingSchedules.setTimeSlots(timeSlotDTO);
			workingSchedules.setDate(CollectionUtils.isEmpty(data) ? null : data.get(0).getCalendarDate());
			List<WorkingSchedulesDTO> dto = new ArrayList<>();
			dto.add(workingSchedules);
			workingSchedulesDTO.setWorkingSchedule(dto);
			workingSchedulesDTO.setTimeslotId(null);
			workingSchedulesDTO.setCalendarDate(null);
			if (flag) {
				workingSchedulesDTO.setCalendarType(null);
				workingSchedulesDTO.setHealthfacilitiesCode(null);
			}
		});
		return listData;
	}

	@Override
	public List<WorkingSchedulesDTO> updateWorkingSchedules(WorkingSchedulesDTO itemParamsEntity) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT dc.calendar_id, dc.doctor_id as doctorId, dc.calendar_type as calendarType, dc.healthfacilities_code as healthfacilitiesCode, dc.timeslot_id as timeslotId, dc.calendar_date as calendarDate");
		sql.append(" FROM doctors_calendars AS dc");
		sql.append(" WHERE dc.is_active = 1 AND dc.is_delete = 0");

		HashMap<String, Object> hmapParams = new HashMap<>();

		if (itemParamsEntity != null && itemParamsEntity.getDoctorId() != null) {
			sql.append(" AND dc.doctor_id = :doctorId");
			hmapParams.put("doctorId", itemParamsEntity.getDoctorId());
		}

		if (itemParamsEntity != null && itemParamsEntity.getCalendarType() != null) {
			sql.append(" AND dc.calendar_type = :calendarType");
			hmapParams.put("calendarType", itemParamsEntity.getCalendarType());
		}

		Integer start = Constants.START_RECORD_DEFAULT;
		if (itemParamsEntity != null && itemParamsEntity.getStartrecord() != null) {
			start = itemParamsEntity.getStartrecord();
		}
		Integer pageSize = Constants.PAGE_SIZE_DEFAULT;
		if (itemParamsEntity != null && itemParamsEntity.getPagesize() != null) {
			pageSize = itemParamsEntity.getPagesize();
		}

		List<WorkingSchedulesDTO> listData = (List<WorkingSchedulesDTO>) getListData(sql, hmapParams, start, pageSize, WorkingSchedulesDTO.class);
		return getData(listData, itemParamsEntity, false, true);
	}

	@Override
	public void deleteWorkingSchedules(WorkingSchedulesDTO itemParamsEntity) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE doctors_calendars AS dc");
		sql.append(" SET dc.is_delete = 1");
		sql.append(" WHERE dc.is_active = 1 and dc.status = 1 ");

		HashMap<String, Object> hmapParams = new HashMap<>();

		if (itemParamsEntity != null && itemParamsEntity.getDoctorId() != null) {
			sql.append(" AND dc.doctor_id = :doctorId");
			hmapParams.put("doctorId", itemParamsEntity.getDoctorId());
		}

		if (itemParamsEntity != null && itemParamsEntity.getHealthfacilitiesCode() != null) {
			sql.append(" AND dc.healthfacilities_code = :healthfacilitiesCode");
			hmapParams.put("healthfacilitiesCode", itemParamsEntity.getHealthfacilitiesCode());
		}

		if (itemParamsEntity != null && itemParamsEntity.getToTime() != null) {
			sql.append(" AND dc.calendar_date <= :toTime");
			hmapParams.put("toTime", Date.valueOf(FnCommon.convertToLocalDate(itemParamsEntity.getToTime())));
		}

		if (itemParamsEntity != null && itemParamsEntity.getFromTime() != null) {
			sql.append(" AND dc.calendar_date >= :fromTime");
			hmapParams.put("fromTime", Date.valueOf(FnCommon.convertToLocalDate(itemParamsEntity.getFromTime())));
		}

		if (itemParamsEntity != null && itemParamsEntity.getCalendarType() != null) {
			sql.append(" AND dc.calendar_type = :calendarType");
			hmapParams.put("calendarType", itemParamsEntity.getCalendarType());
		}
		Integer start = Constants.START_RECORD_DEFAULT;
		Integer pageSize = Constants.PAGE_SIZE_DEFAULT;
		getListData(sql, hmapParams, start, pageSize, WorkingSchedulesDTO.class);
	}

	@Override
	public List<TimeSlotDTO> getWorkingScheduleByDoctorId(Integer doctorId, List<String> healthfacilitiesCodeList, WorkingSchedulesDTO itemParamsEntity) {
		if (CollectionUtils.isEmpty(healthfacilitiesCodeList)) {
			return Collections.EMPTY_LIST;
		}

		StringBuilder sql = new StringBuilder();
		HashMap<String, Object> hmapParams = new HashMap<>();
		sql.append("SELECT dc.calendar_id as calendarId, ");
		sql.append(" dc.doctor_id as doctorId, ");
		sql.append(" dc.timeslot_id as timeslotId, ");
		sql.append(" dc.status as status, ");
		sql.append(" dc.calendar_date as date, ");
		sql.append(" dc.calendar_type as calendarType, ");
		sql.append(" dc.healthfacilities_code as healthfacilitiesCode, ");
		sql.append(" ch.name as healthfacilitiesName, ");
		sql.append(" cats_timeslot.hours_start AS hoursStart, ");
		sql.append(" cats_timeslot.minute_start AS minuteStart, ");
		sql.append(" cats_timeslot.hours_end AS hoursEnd, ");
		sql.append(" cats_timeslot.minute_end AS minuteEnd ");
		sql.append(" FROM doctors_calendars AS dc");
		sql.append(" LEFT JOIN cats_healthfacilities AS ch ON ch.healthfacilities_code = dc.healthfacilities_code AND ch.is_active = 1 AND ch.is_delete = 0 ");
		sql.append(" INNER JOIN cats_timeslots AS cats_timeslot ON cats_timeslot.timeslot_id = dc.timeslot_id ");
		sql.append("   AND cats_timeslot.is_active = 1 ");
		sql.append("   AND cats_timeslot.is_delete = 0 ");
		sql.append(" WHERE dc.is_active = 1 AND dc.is_delete = 0 ");

		if (Objects.nonNull(doctorId)) {
			sql.append("  AND dc.doctor_id = :doctorId ");
			hmapParams.put("doctorId", doctorId);
		}

		if (!CollectionUtils.isEmpty(healthfacilitiesCodeList)) {
			sql.append(" AND dc.healthfacilities_code IN :healthfacilitiesCode");
			hmapParams.put("healthfacilitiesCode", healthfacilitiesCodeList);
		}

		if (Objects.nonNull(itemParamsEntity.getFromTime())) {
			sql.append(" AND dc.calendar_date >= :fromTime");
			hmapParams.put("fromTime", Date.valueOf(FnCommon.convertToLocalDate(itemParamsEntity.getFromTime())));
		}

		if (Objects.nonNull(itemParamsEntity.getToTime())) {
			sql.append(" AND dc.calendar_date <= :toTime");
			hmapParams.put("toTime", Date.valueOf(FnCommon.convertToLocalDate(itemParamsEntity.getToTime())));
		}

		if (Objects.nonNull(itemParamsEntity.getCalendarType())) {
			sql.append(" AND dc.calendar_type = :calendarType");
			hmapParams.put("calendarType", itemParamsEntity.getCalendarType());
		}

		if (Objects.nonNull(itemParamsEntity.getStatus())) {
			sql.append(" AND dc.status = :status");
			hmapParams.put("status", itemParamsEntity.getStatus());
		}

		sql.append(" GROUP BY dc.healthfacilities_code, dc.calendar_type, dc.calendar_date, dc.timeslot_id, dc.status ");
		sql.append(" ORDER BY dc.calendar_id DESC ");

		List<TimeSlotDTO> resultData = (List<TimeSlotDTO>) getListData(sql, hmapParams, null, null, TimeSlotDTO.class);
		return resultData;
	}

}
