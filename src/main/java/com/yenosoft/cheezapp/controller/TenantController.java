package com.yenosoft.cheezapp.controller;

import com.yenosoft.cheezapp.entity.Tenant;
import com.yenosoft.cheezapp.service.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @Operation(summary = "Get information about the current tenant")
    @GetMapping("/current")
    public ResponseEntity<Tenant> getCurrentTenant() {
        return ResponseEntity.ok(tenantService.getCurrentTenant());
    }
}
