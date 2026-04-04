package com.yenosoft.cheezapp.service.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {

    private Long appointmentId;
    private Long serviceProviderId;
    private String serviceProviderName;
    private String serviceProviderProfession;
    private LocalDate appointmentDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
    private String createdAt;
}
