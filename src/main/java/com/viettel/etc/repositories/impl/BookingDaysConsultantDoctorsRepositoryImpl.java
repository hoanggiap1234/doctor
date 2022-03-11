package com.viettel.etc.repositories.impl;

import com.viettel.etc.dto.BookingDaysConsultantDoctorsDTO;
import com.viettel.etc.repositories.BookingDaysConsultantDoctorsRepository;
import com.viettel.etc.utils.Constants;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
import com.viettel.etc.xlibrary.core.repositories.CommonDataBaseRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

/**
 * Autogen class Repository Impl:
 *
 * @author ToolGen
 * @date Mon Sep 14 10:35:04 ICT 2020
 */
@Repository
public class BookingDaysConsultantDoctorsRepositoryImpl extends CommonDataBaseRepository implements BookingDaysConsultantDoctorsRepository {

	/**
	 * Danh sach cac ngay co the dat tu van voi bac si
	 *
	 * @param itemParamsEntity: params client truyen len
	 * @return
	 */
	@Override
	public ResultSelectEntity getBookingDaysConsultantDoctors(BookingDaysConsultantDoctorsDTO itemParamsEntity) {
		StringBuilder sql = new StringBuilder();
		HashMap<String, Object> hmapParams = new HashMap<>();

		sql.append("SELECT" +
				"   DATE(doctors_calendar.calendar_date) AS day " +
				"FROM" +
				"   doctors_calendars AS doctors_calendar " +
				"   INNER JOIN" +
				"      (" +
				"         SELECT" +
				"            limit_number_days_booking," +
				"            sbk.healthfacilities_code " +
				"         FROM" +
				"            sys_configs_booking sbk " +
				"            INNER JOIN" +
				"               doctors_healthfacilities dh " +
				"               ON sbk.healthfacilities_code = dh.healthfacilities_code " +
				"         WHERE" +
				"            dh.is_delete = 0 " +
				"            AND dh.is_active = 1 " +
				"      )" +
				"      AS bcf " +
				"      ON doctors_calendar.healthfacilities_code = bcf.healthfacilities_code " +
				"WHERE" +
				"   doctors_calendar.is_active = 1 " +
				"   AND doctors_calendar.is_delete = 0 " +
				"   AND doctors_calendar.status = 2 " +
				"   AND doctors_calendar.calendar_type = 2 " +
				"   AND CURDATE() <= DATE(doctors_calendar.calendar_date) " +
				"   AND DATE(doctors_calendar.calendar_date) < ADDDATE(CURDATE(), INTERVAL bcf.limit_number_days_booking DAY ) ");
		if (itemParamsEntity != null && itemParamsEntity.getHealthfacilitiesCode() != null) {
			sql.append(" AND bcf.healthfacilities_code = :healthfacilitiesCode ");
			hmapParams.put("healthfacilitiesCode", itemParamsEntity.getHealthfacilitiesCode());
		}
		if (itemParamsEntity != null && itemParamsEntity.getDoctorId() != null) {
			sql.append(" AND doctors_calendar.doctor_id = :doctorId ");
			hmapParams.put("doctorId", itemParamsEntity.getDoctorId());
		}
		sql.append("GROUP BY" +
				"   DATE(doctors_calendar.calendar_date) " +
				"ORDER BY" +
				"   DATE(doctors_calendar.calendar_date)");

		Integer start = Constants.START_RECORD_DEFAULT;
		if (itemParamsEntity != null && itemParamsEntity.getStartrecord() != null) {
			start = itemParamsEntity.getStartrecord();
		}

		Integer pageSize = Constants.PAGE_SIZE_DEFAULT;
		if (itemParamsEntity != null && itemParamsEntity.getPagesize() != null) {
			pageSize = itemParamsEntity.getPagesize();
		}

		ResultSelectEntity resultSelectEntity = getListDataAndCount(sql, hmapParams, start, pageSize, BookingDaysConsultantDoctorsDTO.class);
		return resultSelectEntity;
	}
}
