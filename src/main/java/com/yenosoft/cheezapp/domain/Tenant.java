package com.yenosoft.cheezapp.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tenant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "schema_name", nullable = false, unique = true)
    private String schemaName;

    @Column(nullable = false)
    private boolean active = true;
}
