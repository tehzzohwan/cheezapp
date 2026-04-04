package com.yenosoft.cheezapp.repository;

import com.yenosoft.cheezapp.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByUserId(Long userId);

    List<Appointment> findByServiceProviderId(Long serviceProviderId);

    boolean existsByAvailabilitySlotId(Long availabilitySlotId);
}
