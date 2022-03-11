package com.viettel.etc.repositories.impl;

import com.viettel.etc.dto.BookingTimesConsultantDoctorsDTO;
import com.viettel.etc.dto.DoctorsCalendarBookingTimesConsultantDTO;
import com.viettel.etc.repositories.BookingTimesConsultantDoctorsRepository;
import com.viettel.etc.utils.Constants;
import com.viettel.etc.utils.FnCommon;
import com.viettel.etc.xlibrary.core.repositories.CommonDataBaseRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Autogen class Repository Impl:
 *
 * @author ToolGen
 * @date Mon Sep 14 17:53:52 ICT 2020
 */
@Repository
public class BookingTimesConsultantDoctorsRepositoryImpl extends CommonDataBaseRepository implements BookingTimesConsultantDoctorsRepository {

    /**
     * Danh sach thoi gian co the dat tu van cua bac si
     *
     * @param itemParamsEntity: params client truyen len
     * @return
     */
    @Override
    public List<BookingTimesConsultantDoctorsDTO> getBookingTimesConsultantDoctors(BookingTimesConsultantDoctorsDTO itemParamsEntity) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT dc.timeslot_id AS timeslotId, dc.calendar_date AS calendar, ct.hours_start AS hoursStart, ct.minute_start AS minuteStart, ct.hours_end AS hoursEnd, ct.minute_end AS minuteEnd");
        sql.append(" FROM doctors_calendars dc");
        sql.append(" LEFT JOIN cats_timeslots ct");
        sql.append(" ON dc.timeslot_id = ct.timeslot_id");
        sql.append(" WHERE dc.is_active = 1 AND dc.is_delete = 0 AND ct.is_active = 1 AND ct.is_delete = 0 AND dc.`status` = 2 AND dc.calendar_type = 2");

        HashMap<String, Object> hmapParams = new HashMap<>();

        if (itemParamsEntity != null && itemParamsEntity.getDoctorId() != null) {
            sql.append(" AND dc.doctor_id = :doctorId");
            hmapParams.put("doctorId", itemParamsEntity.getDoctorId());
        }

        if (itemParamsEntity != null && itemParamsEntity.getCalendarDate() != null) {
            sql.append(" AND dc.calendar_date = :calendar");
            hmapParams.put("calendar", itemParamsEntity.getCalendar());
        }

        Integer start = Constants.START_RECORD_DEFAULT;
        if (itemParamsEntity != null && itemParamsEntity.getStartrecord() != null) {
            start = itemParamsEntity.getStartrecord();
        }
        Integer pageSize = Constants.PAGE_SIZE_DEFAULT;
        if (itemParamsEntity != null && itemParamsEntity.getPagesize() != null) {
            pageSize = itemParamsEntity.getPagesize();
        }
        List<BookingTimesConsultantDoctorsDTO> listData = (List<BookingTimesConsultantDoctorsDTO>) getListData(sql, hmapParams, start, pageSize, BookingTimesConsultantDoctorsDTO.class);
        return listData;
    }

    public BookingTimesConsultantDoctorsDTO getMaxNumberBookingDoctor(BookingTimesConsultantDoctorsDTO itemParamsEntity) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT scb.max_number_booking_doctor AS maxNumberBookingDoctor");
        sql.append(" FROM doctors_healthfacilities dh");
        sql.append(" JOIN booking_informations bi");
        sql.append(" ON dh.doctor_id = bi.doctor_id");
        sql.append(" JOIN sys_configs_booking scb");
        sql.append(" ON scb.healthfacilities_code = dh.healthfacilities_code");
        sql.append(" WHERE dh.is_active = 1 AND dh.is_delete = 0 AND bi.is_active = 1 AND bi.is_delete = 0 AND scb.is_active = 1 AND scb.is_delete = 0");

        HashMap<String, Object> hmapParams = new HashMap<>();

        if (itemParamsEntity != null && itemParamsEntity.getDoctorId() != null) {
            sql.append(" AND dh.doctor_id = :doctorId");
            hmapParams.put("doctorId", itemParamsEntity.getDoctorId());
        }

        return (BookingTimesConsultantDoctorsDTO) getFirstData(sql, hmapParams, BookingTimesConsultantDoctorsDTO.class);
    }

    @Override
    public List<DoctorsCalendarBookingTimesConsultantDTO> getDoctorsCalendarBookingTimesConsultants(DoctorsCalendarBookingTimesConsultantDTO dataParams) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> hashMapParams = new HashMap<>();

        sql.append(" SELECT  ");
        sql.append(" doctors_calendar.healthfacilities_code as healthfacilitiesCode, ch.name as healthfacilitiesName,  ");
        sql.append(" doctors_calendar.timeslot_id AS timeslotId,  ");
        sql.append(" doctors_calendar.calendar_date AS calendar,  ");
        sql.append(" cats_timeslot.hours_start AS hoursStart, ");
        sql.append(" cats_timeslot.minute_start AS minuteStart, ");
        sql.append(" cats_timeslot.hours_end AS hoursEnd,  ");
        sql.append(" cats_timeslot.minute_end AS minuteEnd ");
        sql.append(" FROM doctors_calendars AS doctors_calendar ");
        sql.append(" LEFT JOIN (SELECT name, healthfacilities_code FROM cats_healthfacilities ) ch ON  ch.healthfacilities_code = doctors_calendar.healthfacilities_code ");
        sql.append(" JOIN cats_timeslots AS cats_timeslot ON doctors_calendar.timeslot_id = cats_timeslot.timeslot_id ");
        sql.append(" AND cats_timeslot.is_delete = 0 ");
        sql.append(" AND cats_timeslot.is_active = 1 ");
        sql.append(" WHERE doctors_calendar.is_active = 1 ");
        sql.append("   AND doctors_calendar.is_delete = 0 ");
        sql.append("   AND doctors_calendar.status = 2 ");
        sql.append("   AND doctors_calendar.calendar_type = 2");

        if (dataParams != null && dataParams.getDoctorId() != null) {
            sql.append(" AND doctors_calendar.doctor_id = :doctorId");
            hashMapParams.put("doctorId", dataParams.getDoctorId());
        }

        // Bac si co the lam o nhieu co so y te
        if (dataParams != null && dataParams.getHealthfacilitiesCode() != null) {
            sql.append(" AND doctors_calendar.healthfacilities_code = :healthfacilitiesCode");
            hashMapParams.put("healthfacilitiesCode", dataParams.getHealthfacilitiesCode());
        }

        if (dataParams != null && dataParams.getCalendarDate() != null) {
            sql.append(" AND DATE(doctors_calendar.calendar_date) = :calendar");
            hashMapParams.put("calendar", java.sql.Date.valueOf(FnCommon.convertToLocalDate(dataParams.getCalendarDate())));
        }

        sql.append("   GROUP BY doctors_calendar.calendar_id");
        sql.append("   ORDER BY DATE(doctors_calendar.calendar_date)");

        List<DoctorsCalendarBookingTimesConsultantDTO> listData = (List<DoctorsCalendarBookingTimesConsultantDTO>) getListData(sql, hashMapParams, null, null, DoctorsCalendarBookingTimesConsultantDTO.class);
        return listData;
    }

    @Override
    public int countBookingInfo(Integer doctorId, String registerTime,  java.sql.Date registerDate){
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> hmapParams = new HashMap<>();
        sql.append(" SELECT booking_information.booking_id AS bookingId ");
        sql.append(" FROM booking_informations AS booking_information ");
        sql.append(" WHERE booking_information.is_delete = 0 ");
        sql.append(" AND booking_information.is_active = 1 ");
        sql.append(" AND booking_information.doctor_id = :doctorId ");
        sql.append(" AND booking_information.register_date = :registerDate ");
        sql.append(" AND TIME_FORMAT(booking_information.register_time,'%H:%i') = :registerTime ");

        hmapParams.put("doctorId", doctorId);
        hmapParams.put("registerDate", registerDate);
        hmapParams.put("registerTime", registerTime);

        int count = getCountData(sql, hmapParams);
        return count;
    }

    @Override
    public Optional<DoctorsCalendarBookingTimesConsultantDTO> getMaxNumberBookingDoctorByDoctorId(DoctorsCalendarBookingTimesConsultantDTO dataParams) {
        StringBuilder sql = new StringBuilder();
        HashMap<String, Object> hashMapParams = new HashMap<>();

        sql.append(" SELECT sys_configs_booking.max_number_booking_doctor AS maxNumberBookingDoctor, ");
        sql.append(" doctors_healthfacilitie.doctor_id AS doctorId ");
        sql.append(" FROM sys_configs_booking AS sys_configs_booking ");
        sql.append(" JOIN doctors_healthfacilities AS doctors_healthfacilitie ON doctors_healthfacilitie.healthfacilities_code = sys_configs_booking.healthfacilities_code ");
        sql.append("   AND doctors_healthfacilitie.is_active = 1  ");
        sql.append("   AND doctors_healthfacilitie.is_delete = 0  ");
        sql.append(" WHERE sys_configs_booking.is_delete = 0 ");
        sql.append("   AND sys_configs_booking.is_active = 1 ");

        if (dataParams != null && dataParams.getDoctorId() != null) {
            sql.append(" AND doctors_healthfacilitie.doctor_id = :doctorId ");
            hashMapParams.put("doctorId", dataParams.getDoctorId());
        }

        sql.append("   GROUP BY sys_configs_booking.id");
        sql.append("   ORDER BY sys_configs_booking.max_number_booking_doctor DESC");

        DoctorsCalendarBookingTimesConsultantDTO resultData = (DoctorsCalendarBookingTimesConsultantDTO) getFirstData(sql, hashMapParams, DoctorsCalendarBookingTimesConsultantDTO.class);
        return Optional.ofNullable(resultData);
    }

}
