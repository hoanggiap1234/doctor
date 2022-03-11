package com.viettel.etc.repositories.impl;

import com.viettel.etc.dto.DoctorsCalendarsDTO;
import com.viettel.etc.repositories.DoctorsCalendarsRepository;
import com.viettel.etc.utils.FnCommon;
import com.viettel.etc.utils.TeleCareException;
import com.viettel.etc.xlibrary.core.repositories.CommonDataBaseRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Comment
 */
@Repository
public class DoctorsCalendarsRepositoryImpl extends CommonDataBaseRepository implements DoctorsCalendarsRepository {
	@Override
	@Transactional
	public void confirmWorkingSchedules(DoctorsCalendarsDTO dto) throws TeleCareException {
		HashMap<String, Object> mapParams = new HashMap<>();
		StringBuilder sql = new StringBuilder();
		sql.append("update doctors_calendars set status=2 where status=1 and is_delete=0 and is_active=1 ");

		if (dto.getToDate() != null) {
			sql.append("and calendar_date <= :toDate ");
			mapParams.put("toDate", Date.valueOf(FnCommon.convertToLocalDate(dto.getToDate())));
		}

		if (dto.getFromDate() != null) {
			sql.append("and calendar_date >= :fromDate ");
			mapParams.put("fromDate", Date.valueOf(FnCommon.convertToLocalDate(dto.getFromDate())));
		}

		if (dto.getDoctorIds() != null) {
			int[] doctorIds = FnCommon.stringToIntArr(dto.getDoctorIds());
			if (doctorIds.length > 0) {
				String arrString = Arrays.toString(doctorIds).replace("[", "").replace("]", "");
				sql.append("and doctor_id IN(").append(arrString).append(") ");
			}
		}

		if (dto.getHealthfacilitiesCode() != null) {
			sql.append("and healthfacilities_code = :healthfacilitiesCode ");
			mapParams.put("healthfacilitiesCode", dto.getHealthfacilitiesCode());
		}

		excuteSqlDatabase(sql, mapParams);
	}
}
