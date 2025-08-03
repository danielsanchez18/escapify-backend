package com.escapecode.escapify.modules.enterprise.entities;

import com.escapecode.escapify.shared.model.Audit;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "companies")
@Data
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 100, nullable = false)
    private String name;

    private String description;

    @Column(length = 20, nullable = false)
    private String tag;

    @Column(length = 20)
    private String phoneNumber;

    @Column(length = 20)
    private String country;

    @Column(length = 100)
    private String email;

    private String website;
    private String logoUrl;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(nullable = false)
    private Boolean deleted = false;

    @Embedded
    private Audit audit = new Audit();

}
