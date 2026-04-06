package com.yenosoft.cheezapp.service.dto;

import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklyScheduleRequest {

    private List<DaySchedule> days;

    @Getter
    @Setter
    public static class DaySchedule {
        private DayOfWeek dayOfWeek;

        private LocalTime startTime;

        private LocalTime endTime;
    }
}
