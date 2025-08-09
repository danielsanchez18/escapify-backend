package com.escapecode.escapify.modules.users.mappers;

import com.escapecode.escapify.modules.users.dto.UserDTO;
import com.escapecode.escapify.modules.users.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setLastname(user.getLastname());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setPhotoUrl(user.getPhotoUrl());
        dto.setProvider(user.getProvider());
        dto.setProviderId(user.getProviderId());
        dto.setEnabled(user.getEnabled());
        dto.setDeleted(user.getDeleted());
        dto.setAudit(user.getAudit());
        return dto;
    }

    public User toEntity(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setLastname(dto.getLastname());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setPhotoUrl(dto.getPhotoUrl());
        user.setProvider(dto.getProvider());
        user.setProviderId(dto.getProviderId());
        user.setEnabled(dto.getEnabled());
        user.setDeleted(dto.getDeleted());
        return user;
    }

}