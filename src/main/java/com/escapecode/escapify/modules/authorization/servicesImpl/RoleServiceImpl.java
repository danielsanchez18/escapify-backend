package com.escapecode.escapify.modules.authorization.servicesImpl;

import com.escapecode.escapify.modules.authorization.dto.RoleDTO;
import com.escapecode.escapify.modules.authorization.entities.Role;
import com.escapecode.escapify.modules.authorization.mappers.RoleMapper;
import com.escapecode.escapify.modules.authorization.repositories.RoleRepository;
import com.escapecode.escapify.modules.authorization.services.RoleService;
import com.escapecode.escapify.modules.authorization.validators.RoleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository repository;

    @Autowired
    private RoleMapper mapper;

    @Autowired
    private RoleValidator validator;

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {

        // Si el scope es ESCAPIFY, el scopeId debe ser nulo
        if (roleDTO.getScope().equalsIgnoreCase("ESCAPIFY")) roleDTO.setScopeId(null);

        roleDTO.setEnabled(true);
        roleDTO.setDeleted(false);

        validator.validateCreate(roleDTO);

        Role role = mapper.toEntity(roleDTO);

        Role savedRole = repository.save(role);
        return mapper.toDTO(savedRole);
    }

    @Override
    public RoleDTO findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID:" + id));
    }

    @Override
    public Page<RoleDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    @Override
    public Page<RoleDTO> search(String name, String scope, UUID scopeId, Boolean enabled, Date startDate, Date endDate, Pageable pageable) {
        Page<Role> roles = repository.search(name, scope, scopeId, enabled, startDate, endDate, pageable);
        return roles.map(role -> mapper.toDTO(role));
    }

    @Override
    public RoleDTO update(UUID id, RoleDTO roleDTO) {
        validator.validateUpdate(id, roleDTO);

        Role existingRole = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID:" + id));

        if (roleDTO.getName() != null) existingRole.setName(roleDTO.getName());

        repository.save(existingRole);
        return mapper.toDTO(existingRole);
    }

    @Override
    public RoleDTO changeStatus(UUID id) {
        Role role = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        if (role.getDeleted()) throw new IllegalArgumentException("No se puede cambiar el estado de un rol eliminado");

        role.setEnabled(!role.getEnabled());
        return mapper.toDTO(repository.save(role));
    }

    @Override
    public void delete(UUID id) {
        Role role = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        if (role.getDeleted()) throw new IllegalArgumentException("El rol ya está eliminado");

        role.setDeleted(true);
        mapper.toDTO(repository.save(role));
    }

    @Override
    public void restore(UUID id) {
        Role role = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        if (!role.getDeleted()) throw new IllegalArgumentException("El rol no está eliminado");

        role.setDeleted(true);
        mapper.toDTO(repository.save(role));
    }
}
