package com.viettel.etc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class WorkingSchedulesRequestCreateDTO {

    Integer calendarId;

    Integer doctorId;

    Integer calendarType;

    String healthfacilitiesCode;

    List<WorkingSchedulesRequestCreateDTO> workingSchedule;

    Date createDate;

    @Pattern(regexp = "^([0-2][0-9]||3[0-1])/(0[0-9]||1[0-2])/([0-9][0-9])?[0-9][0-9]$")
    String date;

    List<TimeSlotDTO> timeslots;

    String language;
}

