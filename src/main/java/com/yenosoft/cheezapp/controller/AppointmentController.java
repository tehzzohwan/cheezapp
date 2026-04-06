package com.yenosoft.cheezapp.controller;

import com.yenosoft.cheezapp.entity.Appointment;
import com.yenosoft.cheezapp.entity.AvailabilitySlot;
import com.yenosoft.cheezapp.service.BookingService;
import com.yenosoft.cheezapp.service.dto.BookingResponse;
import com.yenosoft.cheezapp.service.dto.ProviderDashboardResponse;
import com.yenosoft.cheezapp.service.dto.SlotResponse;
import com.yenosoft.cheezapp.service.dto.WeeklyScheduleRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final BookingService bookingService;

    @Operation(summary = "Book an appointment using availability slot ID")
    @PostMapping("/book/{slotId}")
    public ResponseEntity<Appointment> bookAppointment(@PathVariable Long slotId) {
        Appointment appointment = bookingService.bookAppointment(slotId);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping("/my-slots")
    public ResponseEntity<List<SlotResponse>> getMyAvailableSlots(
        @RequestParam(required = false) LocalDate date) {

        return ResponseEntity.ok(bookingService.getMyAvailableSlotsResponse(date));
    }

    @Operation(summary = "Get my upcoming and past bookings (For normal users)")
    @GetMapping("/bookings")
    public ResponseEntity<List<BookingResponse>> getMyBookings() {
        return ResponseEntity.ok(bookingService.getMyBookings());
    }

    @Operation(summary = "Get Service Provider Dashboard")
    @GetMapping("/provider/dashboard")
    public ResponseEntity<ProviderDashboardResponse> getProviderDashboard() {
        return ResponseEntity.ok(bookingService.getProviderDashboard());
    }

    @Operation(summary = "Cancel an appointment")
    @PutMapping("/cancel/{appointmentId}")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long appointmentId) {
        bookingService.cancelAppointment(appointmentId);
        return ResponseEntity.ok("Appointment cancelled successfully");
    }

    @Operation(summary = "Set weekly recurring availability schedule (Service Provider only)")
    @PostMapping("/schedule")
    public ResponseEntity<String> setSchedule(@Valid @RequestBody WeeklyScheduleRequest request) {
        bookingService.setWeeklySchedule(request);
        return ResponseEntity.ok("Weekly schedule updated successfully");
    }

    @Operation(summary = "Get available slots for a service provider")
    @GetMapping("/available-slots")
    public ResponseEntity<List<AvailabilitySlot>> getAvailableSlots(
        @RequestParam Long serviceProviderId,
        @RequestParam(required = false) LocalDate date) {

        return ResponseEntity.ok(bookingService.getAvailableSlots(serviceProviderId, date));
    }
}
