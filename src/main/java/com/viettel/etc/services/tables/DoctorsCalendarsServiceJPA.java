package com.viettel.etc.services.tables;

import com.viettel.etc.dto.DoctorsCalendarsDTO;
import com.viettel.etc.dto.TimeSlotDTO;
import com.viettel.etc.dto.WorkingSchedulesDTO;
import com.viettel.etc.dto.WorkingSchedulesRequestCreateDTO;
import com.viettel.etc.repositories.DoctorsCalendarsRepository;
import com.viettel.etc.repositories.tables.CatsHealthfacilitiesCalendarsRepositoryJPA;
import com.viettel.etc.repositories.tables.DoctorsCalendarsRepositoryJPA;
import com.viettel.etc.repositories.tables.SysConfigsBookingRepositoryJPA;
import com.viettel.etc.repositories.tables.SysUsersRepositoryJPA;
import com.viettel.etc.repositories.tables.entities.DoctorsCalendarsEntity;
import com.viettel.etc.repositories.tables.entities.SysConfigsBookingEntity;
import com.viettel.etc.services.MessageService;
import com.viettel.etc.utils.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Autogen class: Create Service For Table Name Doctors_calendars
 *
 * @author ToolGen
 * @date Mon Sep 07 16:47:07 ICT 2020
 */
@Service
public class DoctorsCalendarsServiceJPA {

    @Autowired
    DoctorsCalendarsRepositoryJPA doctorsCalendarsRepositoryJPA;
    @Autowired
    SysConfigsBookingRepositoryJPA sysConfigsBookingRepositoryJPA;
    @Autowired
    SysUsersRepositoryJPA sysUsersRepositoryJPA;

    @Autowired
    BookingInformationsServiceJPA bookingInformationsServiceJPA;

    @Autowired
    DoctorsCalendarsRepository doctorsCalendarsRepository;

    @Autowired
    private CatsHealthfacilitiesCalendarsRepositoryJPA catsHealthfacilitiesCalendarsRepositoryJPA;

    @Autowired
    MessageService messageService;
    private static final Logger LOGGER = Logger.getLogger(FnCommon.class);

    public List<DoctorsCalendarsEntity> findAll() {
        return this.doctorsCalendarsRepositoryJPA.findAll();
    }

    public Object save(WorkingSchedulesRequestCreateDTO inputParam, Authentication authentication) throws TeleCareException {

        if (inputParam == null
                || inputParam.getHealthfacilitiesCode() == null
                || inputParam.getDoctorId() == null
                || CollectionUtils.isEmpty(inputParam.getWorkingSchedule())) {
            throw new TeleCareException(messageService.getMessage(Constants.ERR_DATA_DOCTOR_CALENDAR_INFO, Constants.VIETNAM_CODE), ErrorApp.ERR_DATA_DOCTOR_CALENDAR_INFO);
        }
        TelecareUserEntity loginUser = FnCommon.getTelecareUserInfo(authentication);
        if (loginUser == null || loginUser.isPatient()) {
            throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, inputParam.getLanguage()), ErrorApp.ERR_USER_PERMISSION);
        }
        if (loginUser.isClinic()) {
            List<String> codes = sysUsersRepositoryJPA.getHealthfacilitiesCodeArr(loginUser.getKeycloakUserId());
            if (!codes.contains(inputParam.getHealthfacilitiesCode())) {
                throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, inputParam.getLanguage()), ErrorApp.ERR_USER_PERMISSION);
            }
        }

        DoctorsCalendarsEntity entityOrigin = (DoctorsCalendarsEntity) FnCommon.convertObjectToObject(inputParam, DoctorsCalendarsEntity.class);

        List<DoctorsCalendarsEntity> entitiesTemp = inputParam.getWorkingSchedule().stream().filter(workingSchedule -> !CollectionUtils.isEmpty(workingSchedule.getTimeslots()))
                .map(workingSchedule -> {
                    final Date calendarDate = Date.valueOf(LocalDate.parse(workingSchedule.getDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    return workingSchedule.getTimeslots().stream().map(timeslot -> {
                        DoctorsCalendarsEntity entity = new DoctorsCalendarsEntity();
                        FnCommon.copyProperties(entityOrigin, entity);
                        entity.setCalendarDate(calendarDate);
                        entity.setTimeslotId(timeslot.getTimeslotId());
                        entity.setStatus(Constants.STATUS_WAITING);
                        entity.setCreateDate(Date.valueOf(LocalDate.now()));
                        return entity;
                    }).collect(Collectors.toList());
                }).flatMap(Collection::stream).collect(Collectors.toList());
        List<DoctorsCalendarsEntity> entities = entitiesTemp.stream().filter(entity -> !doctorsCalendarsRepositoryJPA
                .existsByDoctorIdAndCalendarDateAndTimeslotIdAndStatus(entity.getDoctorId(), //khong cho tao vao nhung khung gio da duoc confirm
                        entity.getCalendarDate(), entity.getTimeslotId(), Constants.STATUS_CONFIRM)).collect(Collectors.toList());
        if(entities.isEmpty()) {
            throw new TeleCareException(messageService.getMessage(Constants.ERROR_ALL_SELECTED_TIMESLOTS_ARE_CONFIRMED, inputParam.getLanguage()));
        }
        Function<DoctorsCalendarsEntity, String> createKeyCompare = doctorCalendar -> String.join(":",
                String.valueOf(doctorCalendar.getDoctorId()),
                String.valueOf(doctorCalendar.getCalendarDate().getTime()),
                String.valueOf(doctorCalendar.getTimeslotId()));
        List<Integer> healthFacilityWorkingDay = catsHealthfacilitiesCalendarsRepositoryJPA.getDayCodeByHealthfacilitiesCode(inputParam.getHealthfacilitiesCode());
        Map<Integer, List<DoctorsCalendarsEntity>> hmapWeekDayEntities = createHmapWeekDayDoctorCalendar(healthFacilityWorkingDay);
        entities.forEach(entity -> {
            Integer dayCode = FnCommon.convertDateToDayCode(entity.getCalendarDate());
            if(hmapWeekDayEntities.get(dayCode)!=null) hmapWeekDayEntities.get(dayCode).add(entity);
         });

        // check unique list input, trong 1 ngày chỉ được tồn tại duy nhất một khung giờ khám
        if (!entities.parallelStream().map(createKeyCompare).allMatch(new HashSet<>()::add)) {
            throw new TeleCareException(messageService.getMessage(Constants.ERROR_UNIQUE_CALENDAR_DATE_AND_TIMESLOT, inputParam.getLanguage()), ErrorApp.ERROR_UNIQUE_CALENDAR_DATE_AND_TIMESLOT);
        }

        // check time slot and health facilities calendar by weekday
        for (Map.Entry<Integer, List<DoctorsCalendarsEntity>> entry : hmapWeekDayEntities.entrySet()) {
            Integer dayCode = entry.getKey();
            List<DoctorsCalendarsEntity> entitiesByDayCode = entry.getValue();
            if (entitiesByDayCode.size() > 0 && Constants.CALENDAR_TYPE_EXAM.equals(inputParam.getCalendarType())) {
                HashSet<Integer> listTimeSlotId = entitiesByDayCode.stream().map(DoctorsCalendarsEntity::getTimeslotId).collect(Collectors.toCollection(HashSet::new));
                List<Integer> timeslotInvalids = doctorsCalendarsRepositoryJPA.timeslotInvalid(inputParam.getHealthfacilitiesCode(), listTimeSlotId, dayCode);
                List<DoctorsCalendarsEntity> invalidEntities = entitiesByDayCode.stream().filter(entity -> timeslotInvalids.contains(entity.getTimeslotId())).collect(Collectors.toList());
                entitiesByDayCode.removeAll(invalidEntities);

            }
        }
        List<DoctorsCalendarsEntity> entityValid = new ArrayList<>();
        hmapWeekDayEntities.forEach((k, v) -> entityValid.addAll(v));
        if (entityValid.size() == 0) {
            throw new TeleCareException(messageService.getMessage(Constants.ERR_TIMESLOT_CALENDAR_CONFLICT_HEALTHFACILITIES_CALENDAR, inputParam.getLanguage()));
        }


        // check unique DataBase trong 1 khung giờ khám của 1 ngày, chỉ tồn tại hoặc lịch tư vấn hoặc lịch khám
        List<DoctorsCalendarsEntity> doctorsCalendarsEntityByDoctorIds = doctorsCalendarsRepositoryJPA.findByDoctorIdAndIsDeleteFalseAndIsActiveTrue(inputParam.getDoctorId());
        Set<String> customCompares = doctorsCalendarsEntityByDoctorIds.stream().map(createKeyCompare).collect(Collectors.toSet());
        entityValid.removeIf(entity -> customCompares.contains(createKeyCompare.apply(entity)));
        if (entityValid.isEmpty()) {
            throw new TeleCareException(messageService.getMessage(Constants.ERROR_EXIST_CALENDAR_DATE_AND_TIMESLOT, inputParam.getLanguage()), ErrorApp.ERROR_EXIST_CALENDAR_DATE_AND_TIMESLOT);
        }

        // save
        doctorsCalendarsRepositoryJPA.saveAll(entityValid);
        return entityValid;
    }

    public HashMap<Integer, List<DoctorsCalendarsEntity>> createHmapWeekDayDoctorCalendar(List<Integer> workingDays) {
        HashMap<Integer, List<DoctorsCalendarsEntity>> hmap = new HashMap<>();
        for(Integer workingDay : workingDays) {
            List<DoctorsCalendarsEntity> entities = new ArrayList<>();
            hmap.put(workingDay, entities);
        }
        return hmap;
    }

    public void cancelWorkingScheduleMobile(Authentication authentication, int calendarId, String language) throws
            TeleCareException {
        TelecareUserEntity loginUser = FnCommon.getTelecareUserInfo(authentication);
        if (loginUser.isPatient()) {
            throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, language), ErrorApp.ERR_USER_PERMISSION);
        }

        DoctorsCalendarsEntity calendarsEntity = doctorsCalendarsRepositoryJPA.findByCalendarId(calendarId);
        if (calendarsEntity == null) {
            throw new TeleCareException(messageService.getMessage(Constants.ERR_DATA_CALENDAR_NOT_EXIST, language), ErrorApp.ERR_DATA_CALENDAR_NOT_EXIST);
        }
        if (loginUser.isDoctor()) {
            if (!loginUser.getTelecareUserId().equals(calendarsEntity.getDoctorId())) {
                throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, language), ErrorApp.ERR_USER_PERMISSION);
            }
        }
        if (loginUser.isClinic()) {
            List<String> healthfacilityCodes = sysUsersRepositoryJPA.getHealthfacilitiesCodeArr(loginUser.getKeycloakUserId());
            if (!healthfacilityCodes.contains(calendarsEntity.getHealthfacilitiesCode())) {
                throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, language), ErrorApp.ERR_USER_PERMISSION);
            }
        }
        if (calendarsEntity.getStatus() != DoctorsCalendarsEntity.Status.CONFIRMED.val()) {
            throw new TeleCareException(messageService.getMessage(Constants.ERR_CANCEL_CALENDAR_STATUS, language), ErrorApp.ERR_CANCEL_CALENDAR_STATUS);
        }
        SysConfigsBookingEntity configsBooking = sysConfigsBookingRepositoryJPA.findByHealthfacilitiesCode(
                calendarsEntity.getHealthfacilitiesCode());
        if (configsBooking != null && calendarsEntity.getCalendarDate().compareTo(new Date(System.currentTimeMillis())) > 0) {
            throw new TeleCareException(messageService.getMessage(Constants.ERR_CANCEL_CALENDAR_BEFORE, language), ErrorApp.ERR_CANCEL_CALENDAR_BEFORE);
        }
        if (bookingInformationsServiceJPA.existsByMedicalexaminationDateAndDoctorId(calendarsEntity.getCalendarDate(), calendarsEntity.getDoctorId())) {
            throw new TeleCareException(messageService.getMessage(Constants.ERR_CANCEL_CALENDAR_BOOKING_EXISTS, language), ErrorApp.ERR_CANCEL_CALENDAR_BOOKING_EXISTS);
        }

        calendarsEntity.setStatus(DoctorsCalendarsEntity.Status.CALCELED.val());
        calendarsEntity.setUpdateUserId(loginUser.getTelecareUserId());

        doctorsCalendarsRepositoryJPA.save(calendarsEntity);
    }

    public void confirmWorkingSchedules(DoctorsCalendarsDTO dto, Authentication authentication) throws
            TeleCareException {
        TelecareUserEntity sys_users = FnCommon.getTelecareUserInfo(authentication);
        if (sys_users.isPatient() || sys_users.isDoctor()) {
            throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, dto.getLanguage()), ErrorApp.ERR_USER_PERMISSION);
        }
        if (sys_users.isClinic()) {
            List<String> healthfacilitiesCodeArr = sysUsersRepositoryJPA.getHealthfacilitiesCodeArr(sys_users.getKeycloakUserId());
            if (healthfacilitiesCodeArr.isEmpty() || !healthfacilitiesCodeArr.contains(dto.getHealthfacilitiesCode())) {
                throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, dto.getLanguage()), ErrorApp.ERR_USER_PERMISSION);
            }
        }

        doctorsCalendarsRepository.confirmWorkingSchedules(dto);
    }


    public void deleteWorkingScheduleMobile(Authentication authentication, int calendarId, String language) throws
            TeleCareException {
        TelecareUserEntity loginUser = FnCommon.getTelecareUserInfo(authentication);
        if (loginUser.isPatient()) {
            throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, language), ErrorApp.ERR_USER_PERMISSION);
        }
        DoctorsCalendarsEntity calendarsEntity = doctorsCalendarsRepositoryJPA.findByCalendarId(calendarId);
        if (calendarsEntity == null) {
            throw new TeleCareException(messageService.getMessage(Constants.ERR_DATA_CALENDAR_NOT_EXIST, language), ErrorApp.ERR_DATA_CALENDAR_NOT_EXIST);
        }
        if (loginUser.isDoctor()) {
            if (!loginUser.getTelecareUserId().equals(calendarsEntity.getDoctorId())) {
                throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, language), ErrorApp.ERR_USER_PERMISSION);
            }
        }
        if (loginUser.isClinic()) {
            List<String> healthfacilityCodes = sysUsersRepositoryJPA.getHealthfacilitiesCodeArr(loginUser.getKeycloakUserId());
            if (!healthfacilityCodes.contains(calendarsEntity.getHealthfacilitiesCode())) {
                throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, language), ErrorApp.ERR_USER_PERMISSION);
            }
        }

        if (calendarsEntity.getStatus() != DoctorsCalendarsEntity.Status.PENDING.val()) {
            throw new TeleCareException(messageService.getMessage(Constants.ERR_CANCEL_CALENDAR_STATUS, language), ErrorApp.ERR_CANCEL_CALENDAR_STATUS);
        }
        SysConfigsBookingEntity configsBooking = sysConfigsBookingRepositoryJPA.findByHealthfacilitiesCode(calendarsEntity.getHealthfacilitiesCode());
        if (configsBooking != null && calendarsEntity.getCalendarDate().compareTo(new Date(System.currentTimeMillis())) < 0) {
            throw new TeleCareException(messageService.getMessage(Constants.ERR_CANCEL_CALENDAR_BEFORE, language), ErrorApp.ERR_CANCEL_CALENDAR_BEFORE);
        }
        if (bookingInformationsServiceJPA.existsByMedicalexaminationDateAndDoctorId(calendarsEntity.getCalendarDate(), calendarsEntity.getDoctorId())) {
            throw new TeleCareException(messageService.getMessage(Constants.ERR_CANCEL_CALENDAR_BOOKING_EXISTS, language), ErrorApp.ERR_CANCEL_CALENDAR_BOOKING_EXISTS);
        }

        calendarsEntity.setUpdateUserId(loginUser.getTelecareUserId());
        calendarsEntity.setIsDelete(Constants.IS_DELETE);
        calendarsEntity.setIsActive(Constants.IS_NOT_ACTIVE);
        doctorsCalendarsRepositoryJPA.save(calendarsEntity);

    }

    public List<WorkingSchedulesDTO> update(List<WorkingSchedulesDTO> listData, WorkingSchedulesDTO item) throws
            TeleCareException {
        for (WorkingSchedulesDTO data : listData) {
            FnCommon.copyProperties(item, data);
            for (WorkingSchedulesDTO workingSchedulesDTO : data.getWorkingSchedule()) {
                for (TimeSlotDTO timeSlotDTO : workingSchedulesDTO.getTimeSlots()) {
                    DoctorsCalendarsEntity entity = doctorsCalendarsRepositoryJPA.findByCalendarId(timeSlotDTO.getCalendarId());
                    List<DoctorsCalendarsEntity> list = doctorsCalendarsRepositoryJPA.findByDoctorIdAndCalendarDateAndTimeslotIdAndCalendarType(workingSchedulesDTO.getDoctorId(), workingSchedulesDTO.getCalendarDate(), workingSchedulesDTO.getTimeslotId(), workingSchedulesDTO.getCalendarType());
                    if (list != null && list.size() > 0) {
                        throw new TeleCareException(messageService.getMessage(Constants.ERR_DATA_CALENDAR_TYPE_EXIST, workingSchedulesDTO.getLanguage()), ErrorApp.ERR_DATA_CALENDAR_TYPE_EXIST);
                    }
                    entity.setTimeslotId(timeSlotDTO.getTimeslotId());
                    entity.setStatus(timeSlotDTO.getStatus());
                    doctorsCalendarsRepositoryJPA.save(entity);
                }
            }
        }

//		listData.forEach(workingSchedulesDTO -> {
//			FnCommon.copyProperties(item, workingSchedulesDTO);
//			workingSchedulesDTO.getWorkingSchedule().forEach(dto -> {
//				dto.getTimeSlots().forEach(timeSlotDTO -> {
//					DoctorsCalendarsEntity entity = doctorsCalendarsRepositoryJPA.findByCalendarId(timeSlotDTO.getCalendarId());
//					List<DoctorsCalendarsEntity> list = doctorsCalendarsRepositoryJPA.findByDoctorIdAndCalendarDateAndTimeslotIdAndCalendarType(workingSchedulesDTO.getDoctorId(), workingSchedulesDTO.getCalendarDate(), workingSchedulesDTO.getTimeslotId(), workingSchedulesDTO.getCalendarType());
//					if (list != null && list.size() > 0) {
//						try {
//							throw new TeleCareException(ERR_DATA_CALENDAR_TYPE_EXIST);
//						} catch (TeleCareException e) {
//							LOGGER.info(e);
//						}
//					}
//					entity.setTimeslotId(timeSlotDTO.getTimeslotId());
//					entity.setStatus(timeSlotDTO.getStatus());
//					doctorsCalendarsRepositoryJPA.save(entity);
//				});
//			});
//		});
        return listData;
    }
}
