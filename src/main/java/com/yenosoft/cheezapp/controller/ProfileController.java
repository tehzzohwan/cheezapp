package com.yenosoft.cheezapp.controller;

import com.yenosoft.cheezapp.repository.UserRepository;
import com.yenosoft.cheezapp.service.ProfileService;
import com.yenosoft.cheezapp.service.dto.ProfileUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final UserRepository userRepository;

    @Operation(summary = "Get current user's full profile")
    @GetMapping("/get")
    public ResponseEntity<Map<String, Object>> getMyProfile() {
        return ResponseEntity.ok(profileService.getCurrentUserProfile());
    }

    @Operation(summary = "Update current user's profile")
    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateProfile(
        @Valid @RequestBody ProfileUpdateRequest request) {

        Map<String, Object> updatedProfile = profileService.updateProfile(request);
        return ResponseEntity.ok(updatedProfile);
    }
}
