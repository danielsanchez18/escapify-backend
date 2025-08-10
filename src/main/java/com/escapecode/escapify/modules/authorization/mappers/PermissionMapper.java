package com.escapecode.escapify.modules.authorization.mappers;

import com.escapecode.escapify.modules.authorization.dto.PermissionDTO;
import com.escapecode.escapify.modules.authorization.entities.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {

    public PermissionDTO toDTO (Permission permission) {
        PermissionDTO dto = new PermissionDTO();
        dto.setId(permission.getId());
        dto.setCode(permission.getCode());
        dto.setDescription(permission.getDescription());
        dto.setAudit(permission.getAudit());
        return dto;
    }

    public Permission toEntity (PermissionDTO permissionDTO) {
        Permission permission = new Permission();
        permission.setId(permissionDTO.getId());
        permission.setCode(permissionDTO.getCode());
        permission.setDescription(permissionDTO.getDescription());
        return permission;
    }

}
