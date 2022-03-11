package com.viettel.etc.services;

import com.viettel.etc.dto.WorkingSchedulesDTO;
import com.viettel.etc.utils.TeleCareException;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
import org.springframework.security.core.Authentication;

/**
 * Autogen class:
 *
 * @author ToolGen
 * @date Mon Sep 07 16:47:06 ICT 2020
 */
public interface WorkingSchedulesService {

	ResultSelectEntity getWorkingSchedules(WorkingSchedulesDTO itemParamsEntity, Authentication authentication) throws TeleCareException;

	Object updateWorkingSchedules(WorkingSchedulesDTO itemParamsEntity, Authentication authentication) throws TeleCareException;

	void deleteWorkingSchedules(WorkingSchedulesDTO itemParamsEntity) throws TeleCareException;
}
