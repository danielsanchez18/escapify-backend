package com.escapecode.escapify.modules.authorization.entities;

import com.escapecode.escapify.shared.model.Audit;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "role_permissions")
@Data
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID roleId;

    @Column(nullable = false)
    private UUID permissionId;

    @Column(nullable = false)
    private Boolean deleted;

    @Embedded
    private Audit audit = new Audit();

}
