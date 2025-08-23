package com.escapecode.escapify.modules.enterprise.entities;

import com.escapecode.escapify.shared.model.Audit;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@EntityListeners(AuditingEntityListener.class)
@Data
@Entity
@Table(name = "branches")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 100, nullable = false)
    private String address;

    @Column(length = 20,  nullable = false)
    private String city;

    @Column(length = 20,  nullable = false)
    private String country;

    private String logoUrl;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(nullable = false)
    private Boolean deleted = false;

    @Embedded
    private Audit audit = new Audit();

}

