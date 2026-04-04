package com.yenosoft.cheezapp.config;

import com.yenosoft.cheezapp.domain.*;
import com.yenosoft.cheezapp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final ServiceProviderRepository serviceProviderRepository;
    private final AvailabilitySlotRepository availabilitySlotRepository;

    @Override
    public void run(String... args) {

        // === 1. Create Default Tenant ===
        Tenant tenant = tenantRepository.findBySchemaName("public")
            .orElseGet(() -> {
                Tenant t = Tenant.builder()
                    .name("CheezApp Main Tenant")
                    .schemaName("public")
                    .active(true)
                    .build();
                return tenantRepository.save(t);
            });

        System.out.println("✅ Tenant loaded: " + tenant.getName());

        // === 2. Create Test User ===
        if (userRepository.findByEmail("bobby@yenosoft.com").isEmpty()) {
            User user = User.builder()
                .tenant(tenant)
                .email("bobby@yenosoft.com")
                .password("$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi") // password = "password"
                .firstName("Bobby")
                .lastName("Enomate")
                .role(Role.USER)
                .enabled(true)
                .build();
            userRepository.save(user);
            System.out.println("✅ Test User created → bobby@yenosoft.com / password");
        }

        // === 3. Create Sample Service Providers ===
        List<ServiceProvider> existingProviders = serviceProviderRepository.findByTenantIdAndActiveTrue(tenant.getId());

        if (existingProviders.isEmpty()) {
            ServiceProvider sp1 = ServiceProvider.builder()
                .tenant(tenant)
                .name("Dr. Adebayo Okafor")
                .profession("Professional Barber")
                .bio("Expert barber with 10+ years experience specializing in fades and beard grooming.")
                .active(true)
                .build();

            ServiceProvider sp2 = ServiceProvider.builder()
                .tenant(tenant)
                .name("Ms. Fatima Bello")
                .profession("Makeup Artist")
                .bio("Professional makeup artist specializing in bridal, party, and editorial makeup.")
                .active(true)
                .build();

            serviceProviderRepository.saveAll(List.of(sp1, sp2));

            System.out.println("✅ 2 Service Providers created successfully!");
        } else {
            System.out.println("✅ Service Providers already exist (" + existingProviders.size() + " found)");
        }

        // === 4. Create Sample Availability Slots for Tomorrow ===
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        List<ServiceProvider> providers = serviceProviderRepository.findByTenantIdAndActiveTrue(tenant.getId());

        for (ServiceProvider sp : providers) {
            createSampleSlotsForProvider(sp, tomorrow);
        }

        System.out.println("🎉 Sample data initialization completed!");
        System.out.println("   Login with: bobby@yenosoft.com / password");
    }

    private void createSampleSlotsForProvider(ServiceProvider sp, LocalDate date) {
        LocalTime[] startTimes = {
            LocalTime.of(9, 0), LocalTime.of(10, 0), LocalTime.of(11, 0),
            LocalTime.of(14, 0), LocalTime.of(15, 0), LocalTime.of(16, 0)
        };

        for (LocalTime start : startTimes) {
            LocalTime end = start.plusHours(1);

            // Check if slot already exists to avoid duplicates
            boolean exists = availabilitySlotRepository.findByServiceProviderIdAndDateAndBookedFalse(sp.getId(), date)
                .stream()
                .anyMatch(slot -> slot.getStartTime().equals(start));

            if (!exists) {
                AvailabilitySlot slot = AvailabilitySlot.builder()
                    .tenant(sp.getTenant())
                    .serviceProvider(sp)
                    .date(date)
                    .startTime(start)
                    .endTime(end)
                    .booked(false)
                    .build();

                availabilitySlotRepository.save(slot);
            }
        }

        System.out.println("   → Created slots for " + sp.getName() + " on " + date);
    }
}
