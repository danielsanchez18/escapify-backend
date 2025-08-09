package com.escapecode.escapify.modules.authorization.validators;

import com.escapecode.escapify.modules.authorization.dto.RoleDTO;
import com.escapecode.escapify.modules.authorization.entities.Role;
import com.escapecode.escapify.modules.authorization.repositories.RoleRepository;
import com.escapecode.escapify.modules.enterprise.repositories.BranchRepository;
import com.escapecode.escapify.modules.enterprise.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

@Component
public class RoleValidator {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private BranchRepository branchRepository;

    /* Validaciones para crear la entidad Role:
    1. Validar que los campos obligatorios no sean nulos o vacíos.
    2. Validar que el scope del rol sea válido (ESCAPIFY, COMPANY, BRANCH)
    3. Validar que si el scopeId no es nulo, sea válido o exista.
    4. Validar que el nombre no se repita a nivel scope y scopeId (solo se puede repetir si el rol está marcado como 'deleted') */

    public void validateCreate (RoleDTO dto) {

        // 1. Validar que los campos obligatorios no sean nulos o vacíos
        if (dto.getName() == null || dto.getName().isEmpty()) throw new IllegalArgumentException("El campo nombre es obligatorio.");

        // 2. Validar que el scope del rol sea válido
        if (!dto.getScope().equalsIgnoreCase("ESCAPIFY") &&
            !dto.getScope().equalsIgnoreCase("COMPANY") &&
            !dto.getScope().equalsIgnoreCase("BRANCH")) {
            throw new IllegalArgumentException("El scope debe ser 'ESCAPIFY', 'COMPANY' o 'BRANCH'.");
        }

        // 3. Validar que si el scopeId no es nulo, sea válido o exista
        if (dto.getScopeId() != null) {
            if (dto.getScope().equalsIgnoreCase("COMPANY")) {
                if (!companyRepository.existsByIdAndEnabledTrueAndDeletedFalse(dto.getScopeId())) {
                    throw new IllegalArgumentException("El scopeId no corresponde a una empresa válida.");
                }
            } else if (dto.getScope().equalsIgnoreCase("BRANCH")) {
                if (!branchRepository.existsByIdAndEnabledTrueAndDeletedFalse(dto.getScopeId())) {
                    throw new IllegalArgumentException("El scopeId no corresponde a una sucursal válida.");
                }
            }
        }


        // 4. Validar que el nombre no se repita a nivel scope y scopeId
        if (roleRepository.existsByNameAndScopeAndScopeIdAndDeletedFalse(dto.getName(), dto.getScope(), dto.getScopeId())) {
            throw new IllegalArgumentException("Ya existe un rol con el mismo nombre en el mismo scope y scopeId.");
        }
    }

    /* Validaciones para actualizar la entidad Role:
    1. Validar que el rol exista y no esté eliminado
    2. Validar que los campos obligatorios no sean nulos o vacíos.
    3. Validar que el nombre no se repita a nivel scope y scopeId (solo se puede repetir si el rol está marcado como 'deleted') */

    public void validateUpdate (UUID id, RoleDTO dto) {

        // 1. Validar que el rol exista y no esté eliminado
        Role role = roleRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("El rol no existe o ha sido eliminado."));

        // 2. Validar que los campos obligatorios no sean nulos o vacíos
        String name = StringUtils.hasText(dto.getName()) ? dto.getName() : role.getName();

        if (!StringUtils.hasText(name)) throw new IllegalArgumentException("El campo nombre es obligatorio.");

        // 3. Validar que el nombre no se repita a nivel scope y scopeId
        Optional<Role> otherName = roleRepository.findByNameAndScopeAndScopeIdAndDeletedFalse(name, role.getScope(), role.getScopeId());

        if (otherName.isPresent() && !otherName.get().getId().equals(id)) {
            throw new IllegalArgumentException("Ya existe un rol con el mismo nombre en el mismo scope y scopeId.");
        }
    }


}
