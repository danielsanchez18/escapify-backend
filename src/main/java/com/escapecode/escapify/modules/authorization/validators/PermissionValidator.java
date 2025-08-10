package com.escapecode.escapify.modules.authorization.validators;

import com.escapecode.escapify.modules.authorization.dto.PermissionDTO;
import com.escapecode.escapify.modules.authorization.entities.Permission;
import com.escapecode.escapify.modules.authorization.repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

@Component
public class PermissionValidator {

    @Autowired
    private PermissionRepository permissionRepository;

    /* Validaciones para crear la entidad Permission:
    1. Validar que los campos obligatorios no sean nulos.
    2. Validar que no se repita el code */

    public void validateCreate(PermissionDTO dto) {

        // 1. Validar que el code no sea nulo
        if (dto.getCode() == null || dto.getCode().isEmpty()) throw new IllegalArgumentException("El campo code es obligatorio.");

        // 2. Validar que el code no se repita
        if (permissionRepository.existsByCode(dto.getCode())) {
            throw new IllegalArgumentException("Ya existe un permiso con ese code.");
        }

    }

    /* Validaciones para crear la entidad Permission:
    1. Validar que el permiso exista.
    2. Validar que los campos obligatorios no sean nulos.
    3. Validar que no se repita el code */

    public void validateUpdate(UUID id, PermissionDTO dto) {

        // 1. Validar que el permiso exista
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El permiso no existe."));

        // 2. Validar que los campos obligatorios no sean nulos
        String code = StringUtils.hasText(dto.getCode()) ? dto.getCode() : permission.getCode();

        if (!StringUtils.hasText(code)) throw new IllegalArgumentException("El campo code es obligatorio.");

        // 3. Validar que no se repita el code
        Optional<Permission> otherCode = permissionRepository.findByCode(dto.getCode());

        if (otherCode.isPresent() && !otherCode.get().getId().equals(id)) {
            throw new IllegalArgumentException("Ya existe un permiso con el mismo code.");
        }
    }

}
