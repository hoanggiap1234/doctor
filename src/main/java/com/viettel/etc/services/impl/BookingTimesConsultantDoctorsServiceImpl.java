package com.viettel.etc.services.impl;

import com.viettel.etc.dto.BookingDoctorResponseDTO;
import com.viettel.etc.dto.BookingInformationDTO;
import com.viettel.etc.dto.BookingTimesConsultantDoctorsDTO;
import com.viettel.etc.dto.DoctorsCalendarBookingTimesConsultantDTO;
import com.viettel.etc.repositories.BookingInformationRepository;
import com.viettel.etc.repositories.BookingTimesConsultantDoctorsRepository;
import com.viettel.etc.repositories.tables.entities.BookingInformationsEntity;
import com.viettel.etc.services.BookingTimesConsultantDoctorsService;
import com.viettel.etc.services.MessageService;
import com.viettel.etc.utils.Constants;
import com.viettel.etc.utils.ErrorApp;
import com.viettel.etc.utils.FnCommon;
import com.viettel.etc.utils.TeleCareException;
import com.viettel.etc.xlibrary.core.entities.ResultSelectEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Autogen class:
 *
 * @author ToolGen
 * @date Mon Sep 14 17:53:52 ICT 2020
 */
@Service
public class BookingTimesConsultantDoctorsServiceImpl implements BookingTimesConsultantDoctorsService {

    private static final Logger LOGGER = Logger.getLogger(FnCommon.class);

    @Autowired
    private BookingTimesConsultantDoctorsRepository bookingTimesConsultantDoctorsRepository;

    @Autowired
    private BookingInformationRepository bookingInformationRepository;

    @Autowired
    MessageService messageService;
    /**
     * Danh sach thoi gian co the dat tu van cua bac si
     *
     * @param itemParamsEntity params client
     * @return
     */
    @Override
    public ResultSelectEntity getBookingTimesConsultantDoctors(BookingTimesConsultantDoctorsDTO itemParamsEntity) throws TeleCareException {
        if (itemParamsEntity.getCalendarDate() == null || itemParamsEntity.getDuration() == null || itemParamsEntity.getDoctorId() == null) {
            throw new TeleCareException(messageService.getMessage(Constants.ERR_DATA_BOOKING_TIME_INFO, itemParamsEntity.getLanguage()), ErrorApp.ERR_DATA_BOOKING_TIME_INFO);
        }

        long convertDate = Long.parseLong(itemParamsEntity.getCalendarDate());
//        Date convertLongDate = new Date(convertDate * 1000);
        Date convertLongDate = new Date(convertDate);
        itemParamsEntity.setCalendar(convertLongDate);

        List<BookingTimesConsultantDoctorsDTO> listData = bookingTimesConsultantDoctorsRepository.getBookingTimesConsultantDoctors(itemParamsEntity);

        List<BookingTimesConsultantDoctorsDTO> dataResult = new ArrayList<>();

        listData.forEach(bookingTimesConsultantDoctorsDTO -> {
            List<String> timeList = createDurationTime(itemParamsEntity.getDuration(), itemParamsEntity.getCalendar(), bookingTimesConsultantDoctorsDTO.getHoursStart(), bookingTimesConsultantDoctorsDTO.getMinuteStart(),
                    bookingTimesConsultantDoctorsDTO.getHoursEnd(), bookingTimesConsultantDoctorsDTO.getMinuteEnd());
            for (int i = 0; i < timeList.size() - 1; i++) {
                if (i + 2 == timeList.size()) {
                    String splitMinute = timeList.get(i).substring(3);
                    String splitMinuteEnd = timeList.get(i + 1).substring(3);
                    int splitMinuteInt = Integer.parseInt(splitMinute);
                    int splitMinuteEndInt = Integer.parseInt(splitMinuteEnd);
                    if (splitMinuteEndInt - itemParamsEntity.getDuration() < splitMinuteInt) {
                        break;
                    }
                }
                BookingInformationDTO item = new BookingInformationDTO();
                item.setRegisterTimeTxt(timeList.get(i));
                item.setRegisterDate(itemParamsEntity.getCalendar());
                item.setDoctorId(itemParamsEntity.getDoctorId());

                List<BookingInformationsEntity> bookingInformationEntityList = (List<BookingInformationsEntity>) bookingInformationRepository.getDataByParam(item);

                BookingTimesConsultantDoctorsDTO getMaxNumberBookingDoctor = bookingTimesConsultantDoctorsRepository.getMaxNumberBookingDoctor(itemParamsEntity);

                if (getMaxNumberBookingDoctor == null) {
                    return;
                }
                BookingTimesConsultantDoctorsDTO dto = new BookingTimesConsultantDoctorsDTO();
                dto.setValue(timeList.get(i));
                dto.setPeriod(timeList.get(i) + "-" + timeList.get(i + 1));
                dto.setCanBook(bookingInformationEntityList.size() < getMaxNumberBookingDoctor.getMaxNumberBookingDoctor());

                dataResult.add(dto);
            }
        });
        List<BookingTimesConsultantDoctorsDTO> resultData = dataResult.stream().filter(FnCommon.distinctByKey(BookingTimesConsultantDoctorsDTO::getValue)).collect(Collectors.toList());
        resultData.sort(Comparator.comparing((BookingTimesConsultantDoctorsDTO b) -> LocalTime.parse(b.getValue())));
        ResultSelectEntity resultEntity = new ResultSelectEntity();
        resultEntity.setListData(resultData);
        resultEntity.setCount(resultData.size());

        return resultEntity;
    }

    private List<String> createDurationTime(Integer duration, Date date, Integer hoursStart, Integer minuteStart,
                                            Integer hoursEnd, Integer minuteEnd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        LocalDateTime startDate = LocalDateTime.parse(createLocalDate(date, hoursStart, minuteStart), formatter);

        LocalDateTime endDate = LocalDateTime.parse(createLocalDate(date, hoursEnd, minuteEnd), formatter);

        List<LocalDateTime> localDateTimeList = new ArrayList<>();
        while (!startDate.isAfter(endDate)) {
            localDateTimeList.add(startDate);
            startDate = startDate.plusMinutes(duration);
        }
        localDateTimeList.add(endDate);
        return localDateTimeList.stream().map(datetime -> {
            if (datetime.getHour() < 10 && datetime.getMinute() < 10) {
                return "0" + datetime.getHour() + ":0" + datetime.getMinute();
            } else if (datetime.getHour() < 10) {
                return "0" + datetime.getHour() + ":" + datetime.getMinute();
            } else if (datetime.getMinute() < 10) {
                return datetime.getHour() + ":0" + datetime.getMinute();
            }
            return datetime.getHour() + ":" + datetime.getMinute();
        }).collect(Collectors.toList());
    }


    private StringBuilder createLocalDate(Date date, Integer hour, Integer minute) {
        StringBuilder dateString = new StringBuilder();
        if (minute < 10 && hour < 10) {
            dateString.append(date).append(" 0").append(hour).append(":0").append(minute);
        } else if (minute < 10) {
            dateString.append(date).append(" ").append(hour).append(":0").append(minute);
        } else if (hour < 10) {
            dateString.append(date).append(" 0").append(hour).append(":").append(minute);
        } else {
            dateString.append(date).append(" ").append(hour).append(":").append(minute);
        }
        return dateString;
    }

    @Override
    public ResultSelectEntity getDoctorsCalendarBookingTimesConsultant(DoctorsCalendarBookingTimesConsultantDTO dataParams) throws TeleCareException {
        if (Arrays.asList(dataParams.getCalendarDate(), dataParams.getDuration(), dataParams.getDoctorId()).stream().anyMatch(Objects::isNull)
                || Integer.valueOf(0).compareTo(dataParams.getDuration()) >= 0
                || !FnCommon.isDate(dataParams.getCalendarDate())) {
            throw new TeleCareException(messageService.getMessage(Constants.ERR_DATA_BOOKING_TIME_INFO, dataParams.getLanguage()), ErrorApp.ERR_DATA_BOOKING_TIME_INFO);
        }

        List<DoctorsCalendarBookingTimesConsultantDTO> doctorsCalendarBookingTimesConsultantList = bookingTimesConsultantDoctorsRepository.getDoctorsCalendarBookingTimesConsultants(dataParams);
        Optional<DoctorsCalendarBookingTimesConsultantDTO> maxNumberBookingByDoctorOpt = bookingTimesConsultantDoctorsRepository.getMaxNumberBookingDoctorByDoctorId(dataParams);
        final int maxNumberBookingByDoctor = maxNumberBookingByDoctorOpt.isPresent() ? maxNumberBookingByDoctorOpt.get().getMaxNumberBookingDoctor() : 0;

        List<DoctorsCalendarBookingTimesConsultantDTO> bookingTimeResultList = new ArrayList<>();
        for (DoctorsCalendarBookingTimesConsultantDTO doctorsCalendarBookingTime : doctorsCalendarBookingTimesConsultantList) {
            List<LocalTime> localTimes = this.calculatorLocalTimes(dataParams.getDuration(), doctorsCalendarBookingTime);
            bookingTimeResultList.addAll(getDoctorsCalendarBookingTimesConsultantsDTO(localTimes, doctorsCalendarBookingTime.getHealthfacilitiesCode(), doctorsCalendarBookingTime.getHealthfacilitiesName()));
        }

        // set paging and can book
        final java.sql.Date calendarDate = java.sql.Date.valueOf(FnCommon.convertToLocalDate(dataParams.getCalendarDate()));
        final Integer doctorId = dataParams.getDoctorId();
        List<DoctorsCalendarBookingTimesConsultantDTO>  resultData = bookingTimeResultList.stream().filter(FnCommon.distinctByKey(DoctorsCalendarBookingTimesConsultantDTO::getValue)).collect(Collectors.toList());
        resultData.sort(Comparator.comparing((DoctorsCalendarBookingTimesConsultantDTO b) -> LocalTime.parse(b.getValue())));
        ResultSelectEntity resultSelectEntity = FnCommon.getResultSelectEntity(dataParams.getStartrecord(), dataParams.getPagesize(), resultData);
        resultSelectEntity.getListData().forEach(result -> {
            DoctorsCalendarBookingTimesConsultantDTO resultDTO = (DoctorsCalendarBookingTimesConsultantDTO) result;
            String timeStr = ((DoctorsCalendarBookingTimesConsultantDTO) result).getValue();
            int countBookingInfo = bookingTimesConsultantDoctorsRepository.countBookingInfo(doctorId, resultDTO.getValue(), calendarDate);
            boolean canBook = true;
            boolean isCurrentDay = FnCommon.isCurrentDay(FnCommon.convertToLocalDate(dataParams.getCalendarDate()));
            DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm");
            LocalTime time = LocalTime.parse(timeStr, df);
            if (countBookingInfo >= maxNumberBookingByDoctor
                    || (isCurrentDay && FnCommon.formatLocalTime(LocalTime.now()).isAfter(time))) {
                canBook = false;
            }
            resultDTO.setCanBook(canBook);
        });

        return resultSelectEntity;
    }

    private List<DoctorsCalendarBookingTimesConsultantDTO> getDoctorsCalendarBookingTimesConsultantsDTO(List<LocalTime> localTimes, String healthfacilitiesCode, String healthfacilitiesName) {
        List<DoctorsCalendarBookingTimesConsultantDTO> bookingTimeResultList = new ArrayList<>();
        int sizeLocalTimes = localTimes.size();
        if (sizeLocalTimes >= 2) {
            for (int i = 0; i < sizeLocalTimes - 1; i++) {
                DoctorsCalendarBookingTimesConsultantDTO resultDTO = new DoctorsCalendarBookingTimesConsultantDTO();
                resultDTO.setValue(localTimes.get(i).format(DateTimeFormatter.ofPattern("HH:mm")));
                resultDTO.setPeriod(String.join("-",
                        localTimes.get(i).format(DateTimeFormatter.ofPattern("HH:mm")),
                        localTimes.get(i + 1).format(DateTimeFormatter.ofPattern("HH:mm"))));
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
     * @param duration       duration
     * @param bookingTimeDTO DoctorsCalendarBookingTimesConsultantDTO
     * @return list LocalTime with time start to time end
     */
    private List<LocalTime> calculatorLocalTimes(Integer duration, DoctorsCalendarBookingTimesConsultantDTO bookingTimeDTO) {
        List<LocalTime> localTimes = new ArrayList<>();
        LocalTime bookingTime = LocalTime.of(Objects.isNull(bookingTimeDTO.getHoursStart()) ? 0 : bookingTimeDTO.getHoursStart(),
                Objects.isNull(bookingTimeDTO.getMinuteStart()) ? 0 : bookingTimeDTO.getMinuteStart());

        LocalTime endBookingTime = LocalTime.of(Objects.isNull(bookingTimeDTO.getHoursEnd()) ? 0 : bookingTimeDTO.getHoursEnd(),
                Objects.isNull(bookingTimeDTO.getMinuteEnd()) ? 0 : bookingTimeDTO.getMinuteEnd());

        while (bookingTime.isBefore(endBookingTime)) {
            LocalTime localTimeResult = LocalTime.of(bookingTime.getHour(), bookingTime.getMinute());
            localTimes.add(localTimeResult);
            // plus duration
            bookingTime = bookingTime.plusMinutes(duration);
        }

        return localTimes;
    }

}

