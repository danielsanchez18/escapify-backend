package com.escapecode.escapify.modules.authorization.services;

import com.escapecode.escapify.modules.authorization.dto.PermissionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.UUID;

public interface PermissionService {

    PermissionDTO create(PermissionDTO permissionDTO);

    PermissionDTO findById(UUID id);

    Page<PermissionDTO> findAll(Pageable pageable);

    Page<PermissionDTO> search(
            String code,
            Date startDate,
            Date endDate,
            Pageable pageable
    );

    PermissionDTO update(UUID id, PermissionDTO permissionDTO);

    void delete(UUID id);

}
