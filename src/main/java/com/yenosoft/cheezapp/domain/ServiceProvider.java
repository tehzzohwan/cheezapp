package com.yenosoft.cheezapp.domain;

import com.yenosoft.cheezapp.domain.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "service_provider")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceProvider extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @Column(nullable = false)
    private String name;

    private String profession;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean active = true;
}
