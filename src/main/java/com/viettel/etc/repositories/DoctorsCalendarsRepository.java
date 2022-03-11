package com.viettel.etc.repositories;

import com.viettel.etc.dto.DoctorsCalendarsDTO;
import com.viettel.etc.utils.TeleCareException;

/**
 * Comment
 **/
public interface DoctorsCalendarsRepository {
	public void confirmWorkingSchedules(DoctorsCalendarsDTO dto) throws TeleCareException;
}
