package com.yenosoft.cheezapp.service;

import com.yenosoft.cheezapp.domain.Role;
import com.yenosoft.cheezapp.domain.ServiceProvider;
import com.yenosoft.cheezapp.domain.User;
import com.yenosoft.cheezapp.exception.ResourceNotFoundException;
import com.yenosoft.cheezapp.repository.ServiceProviderRepository;
import com.yenosoft.cheezapp.repository.UserRepository;
import com.yenosoft.cheezapp.service.dto.ProfileUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileService {

    private final UserRepository userRepository;
    private final ServiceProviderRepository serviceProviderRepository;

    public Map<String, Object> getCurrentUserProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Map<String, Object> profile = new HashMap<>();
        profile.put("userId", user.getId());
        profile.put("email", user.getEmail());
        profile.put("firstName", user.getFirstName());
        profile.put("lastName", user.getLastName());
        profile.put("role", user.getRole().name());
        profile.put("tenantName", user.getTenant().getName());
        profile.put("enabled", user.isEnabled());

        if (user.getRole() == Role.SERVICE_PROVIDER) {
            // You can expand this later when User is properly linked to ServiceProvider
            profile.put("isServiceProvider", true);
        }

        return profile;
    }

    public Map<String, Object> updateProfile(ProfileUpdateRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Update common fields
        if ((request.getFirstName() != null) || (request.getLastName() != null)) {

            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
        }

        userRepository.save(user);

        // If user is Service Provider, update their business profile
        if (user.getRole() == Role.SERVICE_PROVIDER) {
            // For now, we update the first ServiceProvider linked to tenant
            serviceProviderRepository.findByTenantIdAndActiveTrue(user.getTenant().getId())
                .stream().findFirst().ifPresent(sp -> {
                    if (request.getProfession() != null) sp.setProfession(request.getProfession());
                    if (request.getBio() != null) sp.setBio(request.getBio());
                    serviceProviderRepository.save(sp);
                });
        }

        // Return updated profile
        return getCurrentUserProfile();
    }
}
