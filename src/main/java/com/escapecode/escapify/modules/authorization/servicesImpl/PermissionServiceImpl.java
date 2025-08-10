package com.escapecode.escapify.modules.authorization.servicesImpl;

import com.escapecode.escapify.modules.authorization.dto.PermissionDTO;
import com.escapecode.escapify.modules.authorization.entities.Permission;
import com.escapecode.escapify.modules.authorization.mappers.PermissionMapper;
import com.escapecode.escapify.modules.authorization.repositories.PermissionRepository;
import com.escapecode.escapify.modules.authorization.services.PermissionService;
import com.escapecode.escapify.modules.authorization.validators.PermissionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository repository;

    @Autowired
    private PermissionMapper mapper;

    @Autowired
    private PermissionValidator validator;

    @Override
    public PermissionDTO create(PermissionDTO permissionDTO) {
        validator.validateCreate(permissionDTO);

        Permission permission = mapper.toEntity(permissionDTO);

        Permission savedPermission = repository.save(permission);
        return mapper.toDTO(savedPermission);
    }

    @Override
    public PermissionDTO findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado con ID:" + id));
    }

    @Override
    public Page<PermissionDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    @Override
    public Page<PermissionDTO> search(String code, Date startDate, Date endDate, Pageable pageable) {
        Page<Permission> permissions = repository.search(code, startDate, endDate, pageable);
        return permissions.map(permission -> mapper.toDTO(permission));
    }

    @Override
    public PermissionDTO update(UUID id, PermissionDTO permissionDTO) {
        validator.validateUpdate(id, permissionDTO);

        Permission existingPermission = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado con ID:" + id));

        if (permissionDTO.getCode() != null) existingPermission.setCode(permissionDTO.getCode());

        repository.save(existingPermission);
        return mapper.toDTO(existingPermission);
    }

    @Override
    public void delete(UUID id) {
        repository.findById(id).orElseThrow(() -> new RuntimeException("Permiso no encontrado"));
        repository.deleteById(id);
    }
}
