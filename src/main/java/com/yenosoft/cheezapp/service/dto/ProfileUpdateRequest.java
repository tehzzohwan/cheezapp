package com.yenosoft.cheezapp.service.dto;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileUpdateRequest {
    @Size(min = 2, max = 100, message = "Full name must be more than 2")
    private String firstName;

    private String lastName;

    private String bio;           // Useful for Service Providers

    private String profession;    // Useful for Service Providers
}
