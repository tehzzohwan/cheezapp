package com.yenosoft.cheezapp.repository;

import com.yenosoft.cheezapp.config.TenantContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.io.Serializable;

public class TenantAwareRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> {

    @PersistenceContext
    private EntityManager entityManager;

    public TenantAwareRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public <S extends T> S save(S entity) {
        setTenantFilter();
        return super.save(entity);
    }

    private void setTenantFilter() {
        String currentTenant = TenantContext.getCurrentTenant();
        if (currentTenant != null) {
            entityManager.unwrap(org.hibernate.Session.class)
                .enableFilter("tenantFilter")
                .setParameter("tenantSchema", currentTenant);
        }
    }
}
