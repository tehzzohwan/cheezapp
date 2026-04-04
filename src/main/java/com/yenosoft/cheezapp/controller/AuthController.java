package com.yenosoft.cheezapp.controller;

import com.yenosoft.cheezapp.domain.Role;
import com.yenosoft.cheezapp.service.ServiceProviderService;
import com.yenosoft.cheezapp.service.dto.AuthRequest;
import com.yenosoft.cheezapp.service.dto.AuthResponse;
import com.yenosoft.cheezapp.service.dto.RegisterRequest;
import com.yenosoft.cheezapp.service.AuthService;
import com.yenosoft.cheezapp.service.dto.ServiceProviderRegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final ServiceProviderService serviceProviderService;


    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request, Role.USER));
    }

    @Operation(summary = "Register a new Service Provider")
    @PostMapping("/register-sp")
    public ResponseEntity<AuthResponse> registerServiceProvider(
        @RequestBody ServiceProviderRegisterRequest request) {

        AuthResponse sp = serviceProviderService.registerServiceProvider(request);
        return ResponseEntity.ok(sp);
    }

    @Operation(summary = "Login user")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

//    @Operation(summary = "Login user")
//    @GetMapping("/info")
//    public ResponseEntity<AuthResponse> profileInfo() {
//        return ResponseEntity.ok(authService.getProfileInfo());
//    }
}
