package com.escapecode.escapify.modules.authorization.servicesImpl;

import com.escapecode.escapify.modules.authorization.entities.UserRole;
import com.escapecode.escapify.modules.authorization.repositories.UserRoleRepository;
import com.escapecode.escapify.modules.authorization.services.UserRoleService;
import com.escapecode.escapify.modules.authorization.validators.UserRoleValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleRepository repository;

    @Autowired
    private UserRoleValidator validator;

    @Override
    public UserRole create(UserRole userRole) {
        userRole.setDeleted(false);
        validator.validateCreate(userRole);

        return repository.save(userRole);
    }

    @Override
    public UserRole getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol de usuario no encontrado"));
    }

    @Override
    public Page<UserRole> getByUserId(UUID userId, Pageable pageable) {
        return repository.findByUserId(userId, pageable);
    }

    @Override
    public Page<UserRole> getByRoleId(UUID roleId, Pageable pageable) {
        return repository.findByRoleId(roleId, pageable);
    }

    @Override
    public void remove(UUID id) {
        UserRole userRole = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol de usuario no encontrado"));

        userRole.setDeleted(true);
        repository.save(userRole);
    }

    @Override
    public void handleUserDeletion(UUID userId) {
        repository.softDeleteByUserId(userId);
    }

    @Override
    public void handleRoleDeletion(UUID roleId) {
        repository.softDeleteByRoleId(roleId);
    }
}
