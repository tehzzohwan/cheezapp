package com.yenosoft.cheezapp.repository;

import com.yenosoft.cheezapp.domain.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

    Optional<Tenant> findBySchemaName(String schemaName);
}
