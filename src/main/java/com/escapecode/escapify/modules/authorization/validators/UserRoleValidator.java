package com.escapecode.escapify.modules.authorization.validators;

import com.escapecode.escapify.modules.authorization.entities.UserRole;
import com.escapecode.escapify.modules.authorization.repositories.RoleRepository;
import com.escapecode.escapify.modules.authorization.repositories.UserRoleRepository;
import com.escapecode.escapify.modules.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRoleValidator {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    /* Validaciones para crear la entidad UserRole:
    1. Validar que el userId exista y esté activo.
    2. Validar que el roleId exista y esté activo.
    3. Verificar que (userId y roleId) no se dupliquen. */

    public void validateCreate (UserRole userRole) {

        // 1. Validar que el userId exista y esté activo
        if (userRole.getUserId() == null || !userRepository.existsById(userRole.getUserId())) {
            throw new IllegalArgumentException("El usuario no existe.");
        }

        if (!userRepository.existsByIdAndEnabledTrueAndDeletedFalse(userRole.getUserId())) {
            throw new IllegalArgumentException("El usuario está inactivo o eliminado.");
        }

        // 2. Validar que el roleId exista y esté activo
        if (userRole.getRoleId() == null || !roleRepository.existsById(userRole.getRoleId())) {
            throw new IllegalArgumentException("El rol asignado no existe.");
        }

        if (!roleRepository.existsByIdAndEnabledTrueAndDeletedFalse(userRole.getRoleId())) {
            throw new IllegalArgumentException("El rol asignado está inactivo o eliminado.");
        }

        // 3. Verificar que (userId y roleId) no se dupliquen
        if (userRoleRepository.existsByUserIdAndRoleIdAndDeletedFalse(userRole.getUserId(), userRole.getRoleId())) {
            throw new IllegalArgumentException("La combinación de usuario y rol ya existe.");
        }

    }

}
