package com.yenosoft.cheezapp.service.dto;

import com.yenosoft.cheezapp.domain.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
}
