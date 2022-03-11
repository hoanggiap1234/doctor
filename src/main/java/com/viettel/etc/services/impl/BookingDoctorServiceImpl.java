package com.viettel.etc.services.impl;

import com.viettel.etc.dto.BookingDoctorRequestDTO;
import com.viettel.etc.dto.BookingDoctorResponseDTO;
import com.viettel.etc.dto.HealthfacilitiesCalendarTimeSlotDTO;
import com.viettel.etc.repositories.BookingDoctorRepository;
import com.viettel.etc.services.BookingDoctorService;
import com.viettel.etc.services.MessageService;
import com.viettel.etc.services.tables.BookingInformationsServiceJPA;
import com.viettel.etc.utils.Constants;
import com.viettel.etc.utils.ErrorApp;
import com.viettel.etc.utils.FnCommon;
import com.viettel.etc.utils.TeleCareException;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingDoctorServiceImpl implements BookingDoctorService {

    @Autowired
    private BookingDoctorRepository doctorBookingRepository;

    @Autowired
    private BookingInformationsServiceJPA bookingInformationServiceJPA;

    @Autowired
    private MessageService messageService;

    @Override
    public ResultSelectEntity getBookingDaysDoctors(BookingDoctorRequestDTO dataParams) {
        ResultSelectEntity resultSelectEntity = doctorBookingRepository.getBookingDaysDoctors(dataParams);
        return resultSelectEntity;
    }

    @Override
    public ResultSelectEntity getBookingHoursDoctors(BookingDoctorRequestDTO dataParams) throws TeleCareException, ParseException {
        if (Objects.nonNull(dataParams.getDay()) && !FnCommon.isDate(dataParams.getDay())) {
            throw new TeleCareException(messageService.getMessage(Constants.ERROR_INPUTPARAMS, dataParams.getLanguage()));
        }

        List<HealthfacilitiesCalendarTimeSlotDTO> healthFacilityCalendarTimeSlotDTOList = doctorBookingRepository.getBookingHoursDoctor(dataParams);
        List<BookingDoctorResponseDTO> bookingDoctorResponseDTOList = new ArrayList<>();
        for (HealthfacilitiesCalendarTimeSlotDTO dto : healthFacilityCalendarTimeSlotDTOList) {
            int countNumberBooking = bookingInformationServiceJPA.countNumberBooking(LocalTime.of(dto.getHourStart(), dto.getMinuteStart()),
                    new Date(dataParams.getDay()), dataParams.getDoctorId(), dataParams.getHealthfacilitiesCode());
            LocalTime afternoonStartTime = LocalTime.of(12, 0);
            LocalTime time = LocalTime.of(dto.getHourEnd(), dto.getMinuteEnd());
            boolean isMax = false; // so luong dat kham toi da
            boolean isCurrentDay = FnCommon.isCurrentDay(FnCommon.convertToLocalDate(dataParams.getDay()));
            if (countNumberBooking >= dto.getMaxNumberBookingService()) {
                isMax = true;
            }
            if (time.isBefore(afternoonStartTime) || time.equals(afternoonStartTime)) {
                // add time slot AM
                List<LocalTime> timeAmList = this.calculatorLocalTimes(dto.getTimeFrame(), dto.getHourStart(), dto.getMinuteStart(), dto.getHourEnd(), dto.getMinuteEnd());
                List<BookingDoctorResponseDTO> resultTimeAm = this.getBookingDoctorResponseDTO(timeAmList, true, isMax, dto.getHealthfacilitiesCode(), dto.getHealthfacilitiesName(), isCurrentDay);
                bookingDoctorResponseDTOList.addAll(resultTimeAm);
            }
            else {
                // add time slot PM
                List<LocalTime> timePmList = this.calculatorLocalTimes(dto.getTimeFrame(), dto.getHourStart(), dto.getMinuteStart(), dto.getHourEnd(), dto.getMinuteEnd());
                List<BookingDoctorResponseDTO> resultTimePm = this.getBookingDoctorResponseDTO(timePmList, false, isMax, dto.getHealthfacilitiesCode(), dto.getHealthfacilitiesName(), isCurrentDay);
                bookingDoctorResponseDTOList.addAll(resultTimePm);
            }
        }
        List<BookingDoctorResponseDTO> resultData = bookingDoctorResponseDTOList.stream().filter(FnCommon.distinctByKey(BookingDoctorResponseDTO::getValue)).collect(Collectors.toList());
        resultData.sort(Comparator.comparing((BookingDoctorResponseDTO b) -> LocalTime.parse(b.getValue())));
        ResultSelectEntity resultSelectEntity = FnCommon.getResultSelectEntity(dataParams.getStartrecord(), dataParams.getPagesize(), resultData);
        return resultSelectEntity;
    }

    private List<BookingDoctorResponseDTO> getBookingDoctorResponseDTO(List<LocalTime> localTimes, boolean isAm, boolean isMax, String healthfacilitiesCode, String healthfacilitiesName, boolean isCurrentDay) {
        List<BookingDoctorResponseDTO> bookingTimeResultList = new ArrayList<>();
        int sizeLocalTimes = localTimes.size();
        if (sizeLocalTimes >= 2) {
            for (int i = 0; i < sizeLocalTimes - 1; i++) {
                boolean canBook = true;
                // neu da vuot qua so luong dat kham toi da hoac thoi gian hien tai da qua khung gio dat kham thi canbook = false;
                if (isMax || (isCurrentDay && FnCommon.formatLocalTime(LocalTime.now()).isAfter(localTimes.get(i)))){
                    canBook = false;
                }
                BookingDoctorResponseDTO resultDTO = new BookingDoctorResponseDTO();
                resultDTO.setValue(FnCommon.formatTimeslot(localTimes.get(i).getHour(), localTimes.get(i).getMinute()));
                resultDTO.setPeriod(FnCommon.formatTimeslot(localTimes.get(i).getHour(), localTimes.get(i).getMinute(),
                        localTimes.get(i + 1).getHour(), localTimes.get(i + 1).getMinute()));
                resultDTO.setIsAm(isAm);
                resultDTO.setCanBook(canBook);
                resultDTO.setHealthfacilitiesCode(healthfacilitiesCode);
                resultDTO.setHealthfacilitiesName(healthfacilitiesName);
                bookingTimeResultList.add(resultDTO);
            }
        }
        return bookingTimeResultList;
    }

    /**
     * get list LocalTime with time start to time end
     *
     * @param duration duration
     * @return list LocalTime with time start to time end
     */
    private List<LocalTime> calculatorLocalTimes(Integer duration, Integer hoursStart, Integer minuteStart, Integer hoursEnd, Integer minuteEnd) {
        List<LocalTime> localTimes = new ArrayList<>();
        if (Objects.isNull(duration) || Integer.valueOf(0).compareTo(duration) >= 0) {
            return localTimes;
        }

        LocalTime bookingTime = LocalTime.of(Objects.isNull(hoursStart) ? 0 : hoursStart, Objects.isNull(minuteStart) ? 0 : minuteStart);

        LocalTime endBookingTime = LocalTime.of(Objects.isNull(hoursEnd) ? 0 : hoursEnd, Objects.isNull(minuteEnd) ? 0 : minuteEnd);

        while (bookingTime.compareTo(endBookingTime) <= 0) {
            LocalTime localTimeResult = LocalTime.of(bookingTime.getHour(), bookingTime.getMinute());
            localTimes.add(localTimeResult);
            // plus duration
            bookingTime = bookingTime.plusMinutes(duration);
        }

        return localTimes;
    }
}
