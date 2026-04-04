package com.yenosoft.cheezapp.service.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderDashboardResponse {

    private Long serviceProviderId;
    private String businessName;
    private String profession;
    private int totalSlotsToday;
    private int upcomingBookings;
    private List<SlotResponse> availableSlots;
    private List<BookingResponse> recentBookings;
}
