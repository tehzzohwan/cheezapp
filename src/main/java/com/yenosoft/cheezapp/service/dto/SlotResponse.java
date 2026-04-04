package com.yenosoft.cheezapp.service.dto;

import com.yenosoft.cheezapp.domain.AvailabilitySlot;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlotResponse {

    private Long slotId;
    private Long serviceProviderId;
    private String serviceProviderName;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String duration;        // e.g., "1 hour"
    private boolean booked;

    public static SlotResponse from(AvailabilitySlot slot) {
        long minutes = Duration.between(slot.getStartTime(), slot.getEndTime()).toMinutes();
        String durationStr = minutes == 60 ? "1 hour" : minutes + " minutes";

        return SlotResponse.builder()
            .slotId(slot.getId())
            .serviceProviderId(slot.getServiceProvider().getId())
            .serviceProviderName(slot.getServiceProvider().getName())
            .date(slot.getDate())
            .startTime(slot.getStartTime())
            .endTime(slot.getEndTime())
            .duration(durationStr)
            .booked(slot.isBooked())
            .build();
    }
}
