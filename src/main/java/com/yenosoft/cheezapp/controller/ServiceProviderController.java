package com.yenosoft.cheezapp.controller;

import com.yenosoft.cheezapp.domain.ServiceProvider;
import com.yenosoft.cheezapp.service.ServiceProviderService;
import com.yenosoft.cheezapp.service.dto.AuthResponse;
import com.yenosoft.cheezapp.service.dto.ServiceProviderRegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/service-providers")
@RequiredArgsConstructor
public class ServiceProviderController {

    private final ServiceProviderService serviceProviderService;

    @Operation(summary = "Register a new Service Provider")
    @PostMapping("/service-providers/register")
    public ResponseEntity<AuthResponse> registerServiceProvider(
        @RequestBody ServiceProviderRegisterRequest request) {

        AuthResponse sp = serviceProviderService.registerServiceProvider(request);
        return ResponseEntity.ok(sp);
    }

    @Operation(summary = "Get all active service providers for the current tenant")
    @GetMapping
    public ResponseEntity<List<ServiceProvider>> getAllServiceProviders() {
        return ResponseEntity.ok(serviceProviderService.getAllActiveServiceProviders());
    }

    @Operation(summary = "Get a service provider by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ServiceProvider> getServiceProviderById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceProviderService.getServiceProviderById(id));
    }
}
