package com.viettel.etc.repositories.tables;

import com.viettel.etc.repositories.tables.entities.DoctorsCalendarsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Autogen class Repository Interface: Create Repository For Table Name Doctors_calendars
 *
 * @author ToolGen
 * @date Mon Sep 07 16:47:07 ICT 2020
 */
@Repository
public interface DoctorsCalendarsRepositoryJPA extends JpaRepository<DoctorsCalendarsEntity, Long> {

	List<DoctorsCalendarsEntity> findByDoctorIdAndCalendarDateAndTimeslotIdAndCalendarType(Integer doctorId, Date calendarDate, Integer timeSlotId, Integer calendarType);

	Boolean existsByDoctorIdAndCalendarDateAndTimeslotIdAndStatus(Integer doctorId, Date calendarDate, Integer timeSlotId, Integer status);

	DoctorsCalendarsEntity findByCalendarId(Integer calendarId);

	DoctorsCalendarsEntity findByCalendarIdAndDoctorId(Integer calendarId, int doctorId);

	@Query("select dc from DoctorsCalendarsEntity dc where dc.doctorId=?1 and dc.isActive=true and dc.isDelete=false and dc.calendarDate>=CURRENT_DATE")
	List<DoctorsCalendarsEntity> findByDoctorIdAndIsDeleteFalseAndIsActiveTrue(Integer doctorId);


	DoctorsCalendarsEntity findByCalendarIdAndHealthfacilitiesCodeIn(long id, List<String> codes);

	@Query(value = "select count(*) = 0 from cats_timeslots ct " +
			"join (select healthfacilities_code, am_from, am_to, pm_from, pm_to from cats_healthfacilities_calendars where is_active = 1 and is_delete = 0 and healthfacilities_code = :healthfacilitiesCode) chc on " +
			"chc.healthfacilities_code = ct.healthfacilities_code " +
			"and ((convert(CONCAT(if(hours_start >= 10, hours_start, CONCAT(0, hours_start)), \":\", if(minute_start >= 10, minute_start, CONCAT(0, minute_start)), \":00\"), TIME) < chc.am_from " +
			"and convert(CONCAT(if(hours_end >= 10, hours_end, CONCAT(0, hours_end)), \":\", if(minute_end >= 10, minute_end, CONCAT(0, minute_end)), \":00\"), TIME) > chc.am_to ) " +
			"or (convert(CONCAT(if(hours_start >= 10, hours_start, CONCAT(0, hours_start)), \":\", if(minute_start >= 10, minute_start, CONCAT(0, minute_start)), \":00\"), TIME) < chc.pm_from " +
			"and convert(CONCAT(if(hours_end >= 10, hours_end, CONCAT(0, hours_end)), \":\", if(minute_end >= 10, minute_end, CONCAT(0, minute_end)), \":00\"), TIME) > chc.pm_to)) " +
			"where ct.is_active = 1 " +
			"and ct.is_delete = 0 and ct.timeslot_id IN :listTimeSlotId", nativeQuery = true)
	int timeValid(String healthfacilitiesCode, List<Integer> listTimeSlotId);

	@Query(value = "SELECT timeslot_id FROM (SELECT timeslot_id, am_from, am_to, pm_from, pm_to, CONVERT(CONCAT(if(hours_start >= 10, hours_start, CONCAT(0, hours_start)), \":\", if(minute_start >= 10, minute_start, CONCAT(0, minute_start)), \":00\"), TIME) AS start,  CONVERT(CONCAT(if(hours_end >= 10, hours_end, CONCAT(0, hours_end)), \":\", if(minute_end >= 10, minute_end, CONCAT(0, minute_end)), \":00\"), TIME) AS end\n" +
			"FROM (SELECT * from cats_timeslots WHERE is_active = 1 AND is_delete = 0 AND timeslot_id IN :listTimeSlotId) ct\n" +
			"JOIN ( \n" +
			"SELECT healthfacilities_code, am_from, am_to, pm_from, pm_to\n" +
			"FROM cats_healthfacilities_calendars\n" +
			"WHERE is_active = 1 AND is_delete = 0 AND healthfacilities_code = :healthfacilitiesCode AND day_code=:dayCode) chc ON chc.healthfacilities_code = ct.healthfacilities_code) temp \n" +
			"WHERE `end`>pm_to OR `start`<am_from OR (`start`>=am_to AND `start`<pm_from) OR (`end`>am_to AND `end` <=pm_from) ", nativeQuery = true)
	List<Integer> timeslotInvalid(String healthfacilitiesCode, HashSet<Integer> listTimeSlotId, Integer dayCode);
}
