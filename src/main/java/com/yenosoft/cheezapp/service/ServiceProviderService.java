package com.yenosoft.cheezapp.service;

import com.yenosoft.cheezapp.entity.*;
import com.yenosoft.cheezapp.service.dto.AuthResponse;
import com.yenosoft.cheezapp.service.dto.RegisterRequest;
import com.yenosoft.cheezapp.service.dto.ServiceProviderRegisterRequest;
import com.yenosoft.cheezapp.exception.ResourceNotFoundException;
import com.yenosoft.cheezapp.repository.ServiceProviderRepository;
import com.yenosoft.cheezapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceProviderService {

    private final ServiceProviderRepository serviceProviderRepository;
    private final UserRepository userRepository;
    private final TenantService tenantService;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    public List<ServiceProvider> getAllActiveServiceProviders() {
        Tenant currentTenant = tenantService.getCurrentTenant();
        return serviceProviderRepository.findByTenantIdAndActiveTrue(currentTenant.getId());
    }

    public ServiceProvider getServiceProviderById(Long id) {
        Tenant currentTenant = tenantService.getCurrentTenant();
        return serviceProviderRepository.findByIdAndTenantId(id, currentTenant.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Service Provider not found"));
    }

    public AuthResponse registerServiceProvider(ServiceProviderRegisterRequest request) {
        // First register as user with SERVICE_PROVIDER role
        RegisterRequest userRequest = RegisterRequest.builder()
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .password(request.getPassword())
            .build();

        AuthResponse authResponse = authService.register(userRequest, Role.SERVICE_PROVIDER);

        // Then create ServiceProvider profile
        Tenant tenant = tenantService.getCurrentTenant();

        ServiceProvider serviceProvider = ServiceProvider.builder()
            .tenant(tenant)
            .name(request.getBusinessName())
            .profession(request.getProfession())
            .bio(request.getBio())
            .email(request.getEmail())
            .active(true)
            .build();

        serviceProviderRepository.save(serviceProvider);

        System.out.println("✅ Service Provider registered and linked: " + request.getBusinessName());

        return authResponse;
    }
}
