package com.escapecode.escapify.modules.authorization.entities;

import com.escapecode.escapify.shared.model.Audit;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "roles")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String scope;

    private UUID scopeId;

    @Column(nullable = false)
    private Boolean isCustom;

    @Column(nullable = false)
    private Boolean enabled;

    @Column(nullable = false)
    private Boolean deleted;

    @Embedded
    private Audit audit = new Audit();

}
