package com.escapecode.escapify.modules.authorization.services;

import com.escapecode.escapify.modules.authorization.entities.RolePermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface RolePermissionService {

    RolePermission create(RolePermission rolePermission);

    RolePermission getById(UUID id);

    Page<RolePermission> getByRoleId(UUID roleId, Pageable pageable);

    void remove(UUID id);

    // Método para manejar la eliminación de un rol
    void handleRoleDeletion(UUID roleId);

    // Método para manejar la eliminación de un permiso
    void handlePermissionDeletion(UUID permissionId);

}
