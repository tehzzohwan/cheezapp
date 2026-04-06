package com.yenosoft.cheezapp.repository;

import com.yenosoft.cheezapp.entity.AvailabilitySlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {

    List<AvailabilitySlot> findByServiceProviderIdAndDateAndBookedFalse(
        Long serviceProviderId, LocalDate date);

    boolean existsByServiceProviderIdAndDateAndStartTimeLessThanAndEndTimeGreaterThan(
        Long serviceProviderId, LocalDate date, LocalTime startTime, LocalTime endTime);
}
