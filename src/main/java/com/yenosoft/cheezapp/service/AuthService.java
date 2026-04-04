package com.yenosoft.cheezapp.service;

import com.yenosoft.cheezapp.config.TenantContext;
import com.yenosoft.cheezapp.domain.Role;
import com.yenosoft.cheezapp.domain.ServiceProvider;
import com.yenosoft.cheezapp.domain.Tenant;
import com.yenosoft.cheezapp.domain.User;
import com.yenosoft.cheezapp.repository.ServiceProviderRepository;
import com.yenosoft.cheezapp.repository.UserRepository;
import com.yenosoft.cheezapp.repository.TenantRepository;
import com.yenosoft.cheezapp.security.JwtService;
import com.yenosoft.cheezapp.service.dto.AuthRequest;
import com.yenosoft.cheezapp.service.dto.AuthResponse;
import com.yenosoft.cheezapp.service.dto.RegisterRequest;
import com.yenosoft.cheezapp.service.dto.ServiceProviderRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final TenantRepository tenantRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final TenantService tenantService;

    private final ServiceProviderRepository serviceProviderRepository;

    @Transactional
    public AuthResponse register(RegisterRequest request, Role role) {
        Tenant tenant = tenantService.getCurrentTenant();   // We'll inject TenantService

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
            .tenant(tenant)
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .role(role)                    // ← Dynamic role
            .enabled(true)
            .build();

        user = userRepository.save(user);

        TenantContext.setCurrentTenant(tenant.getSchemaName());

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .role(user.getRole())
            .build();
    }

    @Transactional
    public AuthResponse registerServiceProvider(ServiceProviderRegisterRequest request) {
        Tenant tenant = tenantService.getCurrentTenant();

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Create User with SERVICE_PROVIDER role
        User user = User.builder()
            .tenant(tenant)
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .role(Role.SERVICE_PROVIDER)
            .enabled(true)
            .build();

        user = userRepository.save(user);

        // Create Service Provider linked to User
        ServiceProvider serviceProvider = ServiceProvider.builder()
            .user(user)
            .tenant(tenant)
            .name(request.getBusinessName())
            .profession(request.getProfession())
            .bio(request.getBio())
            .active(true)
            .build();

        serviceProviderRepository.save(serviceProvider);

        // Generate token
        TenantContext.setCurrentTenant(tenant.getSchemaName());
        String token = jwtService.generateToken(user);

        System.out.println("✅ Service Provider fully registered: " + request.getBusinessName());

        return AuthResponse.builder()
            .token(token)
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .role(user.getRole())
            .build();
    }

    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

        TenantContext.setCurrentTenant(user.getTenant().getSchemaName());

        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
            .token(token)
            .build();
    }



    private Tenant createDefaultTenant() {
        Tenant tenant = Tenant.builder()
            .name("Default Tenant")
            .schemaName("public")
            .active(true)
            .build();
        return tenantRepository.save(tenant);
    }
}
