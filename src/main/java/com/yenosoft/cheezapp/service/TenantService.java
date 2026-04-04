package com.yenosoft.cheezapp.service;

import com.yenosoft.cheezapp.config.TenantContext;
import com.yenosoft.cheezapp.domain.Tenant;
import com.yenosoft.cheezapp.exception.ResourceNotFoundException;
import com.yenosoft.cheezapp.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TenantService {

    private final TenantRepository tenantRepository;

    /**
     * Get current tenant based on TenantContext (set from JWT)
     */
    public Tenant getCurrentTenant() {
        String schemaName = TenantContext.getCurrentTenant();
        if (schemaName == null || schemaName.isBlank()) {
            schemaName = "public"; // fallback
        }
        final String finalSchemaName = schemaName;
        return tenantRepository.findBySchemaName(finalSchemaName)
            .orElseThrow(() -> new ResourceNotFoundException("Tenant not found for schema: " + finalSchemaName));
    }

    /**
     * Get tenant by ID (for admin use later)
     */
    public Tenant getTenantById(Long id) {
        return tenantRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with id: " + id));
    }

    /**
     * Get tenant by schema name
     */
    public Tenant getTenantBySchemaName(String schemaName) {
        return tenantRepository.findBySchemaName(schemaName)
            .orElseThrow(() -> new ResourceNotFoundException("Tenant not found with schema: " + schemaName));
    }
}
