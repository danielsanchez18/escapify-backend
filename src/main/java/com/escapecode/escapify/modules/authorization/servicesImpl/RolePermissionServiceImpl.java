package com.escapecode.escapify.modules.authorization.servicesImpl;

import com.escapecode.escapify.modules.authorization.entities.Role;
import com.escapecode.escapify.modules.authorization.entities.RolePermission;
import com.escapecode.escapify.modules.authorization.repositories.RolePermissionRepository;
import com.escapecode.escapify.modules.authorization.services.RolePermissionService;
import com.escapecode.escapify.modules.authorization.validators.RolePermissionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private RolePermissionRepository repository;

    @Autowired
    private RolePermissionValidator validator;

    @Override
    public RolePermission create(RolePermission rolePermission) {
        rolePermission.setDeleted(false);
        validator.validateCreate(rolePermission);

        return repository.save(rolePermission);
    }

    @Override
    public RolePermission getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("RolePermission no encontrado"));
    }

    @Override
    public Page<RolePermission> getByRoleId(UUID roleId, Pageable pageable) {
        return repository.findByRoleId(roleId, pageable);
    }

    @Override
    public void remove(UUID id) {
        RolePermission rp= repository.findById(id)
                .orElseThrow(() -> new RuntimeException("RolePermission no encontrado"));

        rp.setDeleted(true);
        repository.save(rp);
    }

    @Override
    public void handleRoleDeletion(UUID roleId) {
        repository.softDeleteByRoleId(roleId);
    }

    @Override
    public void handlePermissionDeletion(UUID permissionId) {
        repository.softDeleteByPermissionId(permissionId);
    }
}
