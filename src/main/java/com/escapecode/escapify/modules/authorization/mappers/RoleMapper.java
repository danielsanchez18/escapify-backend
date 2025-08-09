package com.escapecode.escapify.modules.authorization.mappers;

import com.escapecode.escapify.modules.authorization.dto.RoleDTO;
import com.escapecode.escapify.modules.authorization.entities.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleDTO toDTO (Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setScope(role.getScope());
        dto.setScopeId(role.getScopeId());
        dto.setIsCustom(role.getIsCustom());
        dto.setEnabled(role.getEnabled());
        dto.setDeleted(role.getDeleted());
        dto.setAudit(role.getAudit());
        return dto;
    }

    public Role toEntity (RoleDTO roleDTO) {
        Role role = new Role();
        role.setId(roleDTO.getId());
        role.setName(roleDTO.getName());
        role.setScope(roleDTO.getScope());
        role.setScopeId(roleDTO.getScopeId());
        role.setIsCustom(roleDTO.getIsCustom());
        role.setEnabled(roleDTO.getEnabled());
        role.setDeleted(roleDTO.getDeleted());
        return role;
    }

}
