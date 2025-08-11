package com.escapecode.escapify.modules.authorization.services;

import com.escapecode.escapify.modules.authorization.entities.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserRoleService {

    UserRole create(UserRole userRole);

    UserRole getById(UUID id);

    Page<UserRole> getByUserId(UUID userId, Pageable pageable);

    Page<UserRole> getByRoleId(UUID roleId, Pageable pageable);

    void remove(UUID id);

    // Método para manejar la eliminación de un usuario
    void handleUserDeletion(UUID userId);

    // Método para manejar la eliminación de un rol
    void handleRoleDeletion(UUID roleId);

}
