package com.viettel.etc.repositories;

import com.viettel.etc.dto.TimeSlotDTO;
import com.viettel.etc.dto.WorkingSchedulesDTO;
import com.viettel.etc.utils.TeleCareException;
import com.viettel.etc.utils.TelecareUserEntity;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;

import java.util.List;

/**
 * Autogen class Repository Interface:
 *
 * @author toolGen
 * @date Mon Sep 07 16:47:06 ICT 2020
 */
public interface WorkingSchedulesRepository {

	ResultSelectEntity getWorkingSchedules(WorkingSchedulesDTO itemParamsEntity, TelecareUserEntity loginUser) throws TeleCareException;

	List<WorkingSchedulesDTO> updateWorkingSchedules(WorkingSchedulesDTO itemParamsEntity);

	void deleteWorkingSchedules(WorkingSchedulesDTO itemParamsEntity);

	List<TimeSlotDTO> getWorkingScheduleByDoctorId(Integer doctorIds, List<String> healthfacilitiesCodeList, WorkingSchedulesDTO itemParamsEntity);
}
