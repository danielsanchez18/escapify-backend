package com.escapecode.escapify.modules.authorization.services;

import com.escapecode.escapify.modules.authorization.dto.RoleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.UUID;

public interface RoleService {

    RoleDTO createRole(RoleDTO roleDTO);

    RoleDTO findById(UUID id);

    Page<RoleDTO> findAll(Pageable pageable);

    Page<RoleDTO> search(
            String name,
            String scope,
            UUID scopeId,
            Boolean enabled,
            Date startDate,
            Date endDate,
            Pageable pageable
    );

    RoleDTO update(UUID id, RoleDTO roleDTO);

    RoleDTO changeStatus(UUID id);

    void delete(UUID id);

    void restore(UUID id);

}
