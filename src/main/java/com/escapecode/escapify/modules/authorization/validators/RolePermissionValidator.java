package com.escapecode.escapify.modules.authorization.validators;

import com.escapecode.escapify.modules.authorization.entities.RolePermission;
import com.escapecode.escapify.modules.authorization.repositories.PermissionRepository;
import com.escapecode.escapify.modules.authorization.repositories.RolePermissionRepository;
import com.escapecode.escapify.modules.authorization.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RolePermissionValidator {

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    /* Validaciones para la creación de RolePermission:
    1. Validar que el rol que se asigne exista o esté activo.
    2. Validar que el permiso que se asigne exista.
    3. Validar que (roleId y permissionId) no se dupliquen. */

    public void validateCreate(RolePermission rolePermission) {

        // 1. Validar que el rol que se asigne exista o esté activo
        if (rolePermission.getRoleId() == null || !roleRepository.existsById(rolePermission.getRoleId())) {
            throw new IllegalArgumentException("El rol asignado no existe.");
        }

        if (!roleRepository.existsByIdAndEnabledTrueAndDeletedFalse(rolePermission.getRoleId())) {
            throw new IllegalArgumentException("El rol asignado está inactivo o eliminado.");
        }

        // 2. Validar que el permiso que se asigne exista
        if (rolePermission.getPermissionId() == null || !permissionRepository.existsById(rolePermission.getPermissionId())) {
            throw new IllegalArgumentException("El permiso asignado no existe.");
        }

        // 3. Validar que (roleId y permissionId) no se dupliquen
        if (rolePermissionRepository.existsByRoleIdAndPermissionIdAndDeletedFalse(rolePermission.getRoleId(), rolePermission.getPermissionId())) {
            throw new IllegalArgumentException("La combinación de rol y permiso ya existe.");
        }
    }

}
