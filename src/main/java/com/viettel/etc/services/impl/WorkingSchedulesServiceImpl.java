package com.viettel.etc.services.impl;

import com.viettel.etc.dto.TimeSlotDTO;
import com.viettel.etc.dto.WorkingSchedulesDTO;
import com.viettel.etc.repositories.WorkingSchedulesRepository;
import com.viettel.etc.repositories.tables.DoctorsCalendarsRepositoryJPA;
import com.viettel.etc.services.KeycloakService;
import com.viettel.etc.services.MessageService;
import com.viettel.etc.services.WorkingSchedulesService;
import com.viettel.etc.services.tables.DoctorsCalendarsServiceJPA;
import com.viettel.etc.utils.*;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Autogen class:
 *
 * @author ToolGen
 * @date Mon Sep 07 16:47:06 ICT 2020
 */
@Service
public class WorkingSchedulesServiceImpl implements WorkingSchedulesService {

	@Autowired
	private WorkingSchedulesRepository workingSchedulesRepository;

	@Autowired
	private DoctorsCalendarsServiceJPA doctorsCalendarsServiceJPA;

	@Autowired
	private DoctorsCalendarsRepositoryJPA doctorsCalendarsRepositoryJPA;

	@Autowired
	private MessageService messageService;
//	private static final Logger LOGGER = Logger.getLogger(WorkingSchedulesServiceImpl.class);
	/**
	 * Like Collectors.groupingBy, but accepts null keys.
	 *
	 * @param classifier
	 * @param <T>
	 * @param <A>
	 * @return
	 */
	public static <T, A> Collector<T, ?, Map<A, List<T>>> groupingByWithNullKeys(Function<? super T, ? extends A> classifier) {
		return Collectors.toMap(classifier, Collections::singletonList,
				(List<T> oldList, List<T> newEl) -> {
					List<T> newList = new ArrayList<>(oldList.size() + 1);
					newList.addAll(newEl);
					newList.addAll(oldList);
					return newList;
				});
	}

	/**
	 * Lich lam viec cua cac bac si
	 *
	 * @param dto params client
	 * @return
	 */
	@Override
	public ResultSelectEntity getWorkingSchedules(WorkingSchedulesDTO dto, Authentication authentication) throws TeleCareException {
		// validate input param from time
		if (Objects.nonNull(dto.getFromTime()) && !FnCommon.isDate(dto.getFromTime())) {
			throw new TeleCareException(messageService.getMessage(Constants.ERROR_INPUTPARAMS, dto.getLanguage()));
		}
		// validate input param to time
		if (Objects.nonNull(dto.getToTime()) && !FnCommon.isDate(dto.getToTime())) {
			throw new TeleCareException(messageService.getMessage(Constants.ERROR_INPUTPARAMS, dto.getLanguage()));
		}
		//check 30 ngay
		if (dto.getFromTime() != null && dto.getToTime() != null &&
				(dto.getToTime().longValue() - dto.getFromTime().longValue()) / (1000 * 60 * 60 * 24) > 30) {
			throw new TeleCareException(messageService.getMessage(Constants.ERROR_RANGE_TIME_TOO_LONG, dto.getLanguage()), ErrorApp.ERROR_RANGE_TIME_TOO_LONG);
		}

		TelecareUserEntity loginUser = FnCommon.getTelecareUserInfo(authentication);
		if (loginUser.isPatient()) {
//			FnCommon.throwsErrorApp(ErrorApp.ERR_USER_PERMISSION);
			throw new TeleCareException(messageService.getMessage(Constants.ERR_USER_PERMISSION, dto.getLanguage()), ErrorApp.ERR_USER_PERMISSION);
		}

		// get list doctor
		ResultSelectEntity resultSelectEntity = workingSchedulesRepository.getWorkingSchedules(dto, loginUser);

		if (!CollectionUtils.isEmpty(resultSelectEntity.getListData())) {
			List<WorkingSchedulesDTO> doctorWorkingSchedulesDTOs = (List<WorkingSchedulesDTO>) resultSelectEntity.getListData();

			// convert String to list String health Code
			Function<String, List<String>> convertStringHealthFacilitiesCodeToList = strHealthFacilitiesCode -> {
				if (Objects.isNull(strHealthFacilitiesCode)) {
					return Collections.EMPTY_LIST;
				}
				List<String> resultData = new ArrayList<>();

				for (String healthCode : strHealthFacilitiesCode.split(",")) {
					if (!StringUtils.isEmpty(healthCode) && !"null".equalsIgnoreCase(healthCode)) {
						resultData.add(healthCode);
					}
				}
				return resultData;
			};

			// set workingSchedule to list doctor
			for (WorkingSchedulesDTO doctorSchedule : doctorWorkingSchedulesDTOs) {
				List<String> healthFacilitiesCodeList = convertStringHealthFacilitiesCodeToList.apply(doctorSchedule.getHealthfacilitiesCodeList());
				List<TimeSlotDTO> timeSlotList = workingSchedulesRepository.getWorkingScheduleByDoctorId(doctorSchedule.getDoctorId(), healthFacilitiesCodeList, dto);
				Map<Date, List<TimeSlotDTO>> timeSlots = timeSlotList.stream().collect(groupingByWithNullKeys(TimeSlotDTO::getDate));

				// create list workingSchedule
				List<WorkingSchedulesDTO> workingSchedules = timeSlots.entrySet().stream().map(entry -> {
					WorkingSchedulesDTO workingSchedule = new WorkingSchedulesDTO();
					workingSchedule.setDate(entry.getKey());

					workingSchedule.setTimeSlots(entry.getValue());
					workingSchedule.getTimeSlots().forEach(time -> time.setTimeslotValue(FnCommon.formatTimeslot(time.getHoursStart(), time.getMinuteStart(), time.getHoursEnd(), time.getMinuteEnd())));
					return workingSchedule;
				}).sorted(Comparator.nullsFirst(Comparator.comparing(WorkingSchedulesDTO::getDate, Comparator.nullsFirst(Comparator.naturalOrder())))).collect(Collectors.toList());

				// set workingSchedule to list doctor
				doctorSchedule.setWorkingSchedule(workingSchedules);
			}
		}
		return resultSelectEntity;
	}

	@Override
	public Object updateWorkingSchedules(WorkingSchedulesDTO itemParamsEntity, Authentication authentication) throws TeleCareException {
		// check time slot and health facilities calendar
		List<Integer> listTimeSlotId =  itemParamsEntity.getTimeSlots().stream().map(n -> n.getTimeslotId()).collect(Collectors.toList());
		int timeValid = doctorsCalendarsRepositoryJPA.timeValid(itemParamsEntity.getHealthfacilitiesCode(), listTimeSlotId);
		if (timeValid != 1) {
			throw new TeleCareException(messageService.getMessage(Constants.ERR_TIMESLOT_CALENDAR_CONFLICT_HEALTHFACILITIES_CALENDAR, itemParamsEntity.getLanguage()));
		}
		List<WorkingSchedulesDTO> listData = workingSchedulesRepository.updateWorkingSchedules(itemParamsEntity);
		List<WorkingSchedulesDTO> result = doctorsCalendarsServiceJPA.update(listData, itemParamsEntity);
		return result;
	}

	@Override
	public void deleteWorkingSchedules(WorkingSchedulesDTO itemParamsEntity) throws TeleCareException {
		if (Objects.nonNull(itemParamsEntity.getFromTime()) && !FnCommon.isDate(itemParamsEntity.getFromTime())) {
			throw new TeleCareException(messageService.getMessage(Constants.ERROR_INPUTPARAMS, itemParamsEntity.getLanguage()));
		}
		if (Objects.nonNull(itemParamsEntity.getToTime()) && !FnCommon.isDate(itemParamsEntity.getToTime())) {
			throw new TeleCareException(messageService.getMessage(Constants.ERROR_INPUTPARAMS, itemParamsEntity.getLanguage()));
		}
		workingSchedulesRepository.deleteWorkingSchedules(itemParamsEntity);
	}

}
