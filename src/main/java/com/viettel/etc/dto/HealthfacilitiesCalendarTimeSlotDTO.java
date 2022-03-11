package com.viettel.etc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HealthfacilitiesCalendarTimeSlotDTO {

    Integer amFromHouse;

    Integer amFromMinute;

    Integer amToHouse;

    Integer amToMinute;

    Integer pmFromHouse;

    Integer pmFromMinute;

    Integer pmToHouse;

    Integer pmToMinute;

    Integer timeFrame;

    Integer maxNumberBookingService;

    Integer hourStart;

    Integer minuteStart;

    Integer hourEnd;

    Integer minuteEnd;

    String healthfacilitiesCode;

    String healthfacilitiesName;
}
