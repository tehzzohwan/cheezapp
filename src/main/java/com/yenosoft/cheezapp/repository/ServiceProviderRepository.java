package com.yenosoft.cheezapp.repository;

import com.yenosoft.cheezapp.domain.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {

    List<ServiceProvider> findByTenantIdAndActiveTrue(Long tenantId);

    Optional<ServiceProvider> findByIdAndTenantId(Long id, Long tenantId);
}
