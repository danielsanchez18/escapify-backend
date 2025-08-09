package com.escapecode.escapify.modules.users.services;

import com.escapecode.escapify.modules.users.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

public interface UserService {

    UserDTO create(UserDTO userDTO, MultipartFile image);

    UserDTO findById(UUID id);

    Page<UserDTO> findAll(Pageable pageable);

    Page<UserDTO> search(
            String fullName,
            String email,
            String provider,
            Date startDate,
            Date endDate,
            Boolean enabled,
            Pageable pageable
    );

    UserDTO update(UUID id, UserDTO updatedUserDTO, MultipartFile image);

    UserDTO changeStatus(UUID id);

    void delete(UUID id);

    void restore(UUID id);

}
