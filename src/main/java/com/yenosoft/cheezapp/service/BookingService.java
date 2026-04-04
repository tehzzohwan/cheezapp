package com.yenosoft.cheezapp.service;

import com.yenosoft.cheezapp.config.TenantContext;
import com.yenosoft.cheezapp.domain.*;
import com.yenosoft.cheezapp.exception.OverbookingException;
import com.yenosoft.cheezapp.exception.ResourceNotFoundException;
import com.yenosoft.cheezapp.repository.AppointmentRepository;
import com.yenosoft.cheezapp.repository.AvailabilitySlotRepository;
import com.yenosoft.cheezapp.repository.ServiceProviderRepository;
import com.yenosoft.cheezapp.repository.UserRepository;
import com.yenosoft.cheezapp.service.dto.AvailabilitySlotRequest;
import com.yenosoft.cheezapp.service.dto.BookingResponse;
import com.yenosoft.cheezapp.service.dto.ProviderDashboardResponse;
import com.yenosoft.cheezapp.service.dto.SlotResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final AvailabilitySlotRepository availabilitySlotRepository;

    private final AppointmentRepository appointmentRepository;

    private final UserRepository userRepository;

    private final ServiceProviderRepository serviceProviderRepository;

    // ====================== ADD SLOT (Service Provider) ======================
    @Transactional
    public AvailabilitySlot addAvailabilitySlot(AvailabilitySlotRequest request) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(currentUserEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.SERVICE_PROVIDER) {
            throw new RuntimeException("Only service providers can add slots");
        }

        validateSlotRequest(request);

        ServiceProvider sp = serviceProviderRepository.findByTenantIdAndActiveTrue(user.getTenant().getId())
            .stream().findFirst()
            .orElseThrow(() -> new RuntimeException("Service provider profile not found"));

        AvailabilitySlot slot = AvailabilitySlot.builder()
            .tenant(user.getTenant())
            .serviceProvider(sp)
            .date(request.getDate())
            .startTime(request.getStartTime())
            .endTime(request.getEndTime())
            .booked(false)
            .build();

        return availabilitySlotRepository.save(slot);
    }

    private void validateSlotRequest(AvailabilitySlotRequest request) {
        LocalDate today = LocalDate.now();

        if (request.getDate().isBefore(today)) {
            throw new RuntimeException("You cannot create slots for past dates");
        }

        if (request.getDate().isEqual(today)) {
            throw new RuntimeException("You cannot create slots for today. Slots must be for future dates.");
        }

        LocalTime open = LocalTime.of(9, 0);
        LocalTime close = LocalTime.of(20, 0);

        if (request.getStartTime().isBefore(open) || request.getEndTime().isAfter(close)) {
            throw new RuntimeException("Slots must be between 9:00 AM and 8:00 PM");
        }

        long duration = java.time.Duration.between(request.getStartTime(), request.getEndTime()).toMinutes();

        if (duration < 60) throw new RuntimeException("Minimum slot duration is 1 hour");
        if (duration > 120) throw new RuntimeException("Maximum slot duration is 2 hours");
    }

    // ====================== GET AVAILABLE SLOTS (Improved Response) ======================
    public List<AvailabilitySlot> getAvailableSlots(Long serviceProviderId, LocalDate date) {
        if (date == null) date = LocalDate.now().plusDays(1);

        return availabilitySlotRepository.findByServiceProviderIdAndDateAndBookedFalse(serviceProviderId, date);
    }

    // ====================== SERVICE PROVIDER: Get My Slots ======================
    public List<AvailabilitySlot> getMyAvailableSlots(LocalDate date) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.SERVICE_PROVIDER) {
            throw new RuntimeException("Access denied");
        }

        if (date == null) date = LocalDate.now().plusDays(1);

        ServiceProvider sp = serviceProviderRepository.findByTenantIdAndActiveTrue(user.getTenant().getId())
            .stream().findFirst()
            .orElseThrow(() -> new RuntimeException("Service provider profile not found"));

        return availabilitySlotRepository.findByServiceProviderIdAndDateAndBookedFalse(sp.getId(), date);
    }

    // ====================== USER: My Bookings ======================
    public List<BookingResponse> getMyBookings() {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(currentUserEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.USER) {
            throw new RuntimeException("Only regular users can view their bookings");
        }

        List<Appointment> appointments = appointmentRepository.findByUserId(user.getId());

        return appointments.stream().map(appointment -> BookingResponse.builder()
            .appointmentId(appointment.getId())
            .serviceProviderId(appointment.getServiceProvider().getId())
            .serviceProviderName(appointment.getServiceProvider().getName())
            .serviceProviderProfession(appointment.getServiceProvider().getProfession())
            .appointmentDate(appointment.getAppointmentDate())
            .startTime(appointment.getStartTime())
            .endTime(appointment.getEndTime())
            .status(appointment.getStatus().name())
            .createdAt(appointment.getCreatedAt() != null ? appointment.getCreatedAt().toString() : null)
            .build()
        ).toList();
    }

    // ====================== SERVICE PROVIDER DASHBOARD ======================
    public ProviderDashboardResponse getProviderDashboard() {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(currentUserEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.SERVICE_PROVIDER) {
            throw new RuntimeException("Access denied. Only service providers can view dashboard");
        }

        ServiceProvider sp = serviceProviderRepository.findByTenantIdAndActiveTrue(user.getTenant().getId())
            .stream().findFirst()
            .orElseThrow(() -> new RuntimeException("Service provider profile not found"));

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        // Get data
        List<AvailabilitySlot> slots = availabilitySlotRepository
            .findByServiceProviderIdAndDateAndBookedFalse(sp.getId(), tomorrow);

        List<Appointment> bookings = appointmentRepository.findByServiceProviderId(sp.getId());

        return ProviderDashboardResponse.builder()
            .serviceProviderId(sp.getId())
            .businessName(sp.getName())
            .profession(sp.getProfession())
            .totalSlotsToday(slots.size())
            .upcomingBookings(bookings.size())
            .availableSlots(slots.stream().map(SlotResponse::from).toList())
            .recentBookings(bookings.stream()
                .limit(5)
                .map(appointment -> BookingResponse.builder()
                    .appointmentId(appointment.getId())
                    .serviceProviderId(sp.getId())
                    .serviceProviderName(sp.getName())
                    .appointmentDate(appointment.getAppointmentDate())
                    .startTime(appointment.getStartTime())
                    .endTime(appointment.getEndTime())
                    .status(appointment.getStatus().name())
                    .build())
                .toList())
            .build();
    }

    // =============================================
    // 3. Book Appointment (User only)
    // =============================================
    @Transactional
    public Appointment bookAppointment(Long slotId) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(currentUserEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.USER) {
            throw new RuntimeException("Only regular users can book appointments");
        }

        AvailabilitySlot slot = availabilitySlotRepository.findById(slotId)
            .orElseThrow(() -> new RuntimeException("Slot not found"));

        if (!slot.getTenant().getSchemaName().equals(TenantContext.getCurrentTenant())) {
            throw new RuntimeException("Access denied: Invalid tenant");
        }

        if (slot.isBooked()) {
            throw new OverbookingException("This slot is already booked");
        }

        Appointment appointment = Appointment.builder()
            .tenant(slot.getTenant())
            .user(user)
            .serviceProvider(slot.getServiceProvider())
            .availabilitySlot(slot)
            .appointmentDate(slot.getDate())
            .startTime(slot.getStartTime())
            .endTime(slot.getEndTime())
            .status(AppointmentStatus.CONFIRMED)
            .build();

        slot.setBooked(true);
        availabilitySlotRepository.save(slot);

        return appointmentRepository.save(appointment);
    }

    // Business Rules Method
    private void validateSlotTiming(AvailabilitySlotRequest request) {
        LocalTime openTime = LocalTime.of(9, 0);   // 9:00 AM
        LocalTime closeTime = LocalTime.of(20, 0); // 8:00 PM

        if (request.getStartTime().isBefore(openTime)) {
            throw new RuntimeException("Service opens at 9:00 AM. Cannot create slot before opening time.");
        }

        if (request.getEndTime().isAfter(closeTime)) {
            throw new RuntimeException("Service closes at 8:00 PM. Cannot create slot after closing time.");
        }

        if (request.getStartTime().isAfter(request.getEndTime()) ||
            request.getStartTime().equals(request.getEndTime())) {
            throw new RuntimeException("End time must be after start time");
        }

        long durationMinutes = java.time.Duration.between(request.getStartTime(), request.getEndTime()).toMinutes();

        if (durationMinutes < 60) {
            throw new RuntimeException("Minimum appointment duration is 1 hour");
        }

        if (durationMinutes > 120) {
            throw new RuntimeException("Maximum appointment duration is 2 hours");
        }
    }

    @Transactional
    public void cancelAppointment(Long appointmentId) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(currentUserEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        // Security Check: Only the owner can cancel
        if (!appointment.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only cancel your own appointments");
        }

        // Business Rule: Cannot cancel completed or already cancelled appointments
        if (appointment.getStatus() == AppointmentStatus.COMPLETED ||
            appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new RuntimeException("This appointment cannot be cancelled");
        }

        // Cancel the appointment
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);

        // Free up the slot
        AvailabilitySlot slot = appointment.getAvailabilitySlot();
        slot.setBooked(false);
        availabilitySlotRepository.save(slot);

        System.out.println("✅ Appointment cancelled: ID " + appointmentId);
    }

    // Convert to nice response
    public List<SlotResponse> getAvailableSlotsResponse(Long serviceProviderId, LocalDate date) {
        List<AvailabilitySlot> slots = getAvailableSlots(serviceProviderId, date);
        return slots.stream().map(SlotResponse::from).toList();
    }

    public List<SlotResponse> getMyAvailableSlotsResponse(LocalDate date) {
        List<AvailabilitySlot> slots = getMyAvailableSlots(date);
        return slots.stream().map(SlotResponse::from).toList();
    }
}
