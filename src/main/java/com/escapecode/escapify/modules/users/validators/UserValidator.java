package com.escapecode.escapify.modules.users.validators;

import com.escapecode.escapify.modules.users.dto.UserDTO;
import com.escapecode.escapify.modules.users.entities.User;
import com.escapecode.escapify.modules.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserValidator {

    @Autowired
    private UserRepository userRepository;

    /* Validaciones para la creación de usuarios.
    1. Validar que los campos obligatorios no sean nulos o vacíos.
    2. Validar que el email no esté ya registrado en la base de datos.
    3. Validar que el provider sea válido. (ESCAPIFY, GOOGLE, FACEBOOK) */

    public void validateCreate (UserDTO dto) {

        // 1. Validar campos obligatorios
        if (dto.getName() == null || dto.getName().isEmpty()) throw new IllegalArgumentException("El campo nombre es obligatorio.");
        if (dto.getPhoneNumber() == null || dto.getPhoneNumber().isEmpty()) throw new IllegalArgumentException("El campo número de teléfono es obligatorio.");
        if (dto.getEmail() == null || dto.getEmail().isEmpty()) throw new IllegalArgumentException("El campo email es obligatorio.");
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) throw new IllegalArgumentException("El campo contraseña es obligatorio.");

        // 2. Validar que el email no esté ya registrado
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

        // 3. Validar que el provider sea válido
        if (!dto.getProvider().equalsIgnoreCase("ESCAPIFY") &&
            !dto.getProvider().equalsIgnoreCase("GOOGLE") &&
            !dto.getProvider().equalsIgnoreCase("FACEBOOK")) {

            throw new IllegalArgumentException("El proveedor debe ser 'ESCAPIFY', 'GOOGLE' o 'FACEBOOK'.");
        }
    }

    /* Validaciones para la actualización de usuarios.
    1. Validar que el usuario exista y no esté eliminado.
    2. Validar que los campos obligatorios no sean nulos o vacíos.
    3. Validar que el email no esté ya registrado por otro usuario (excepto el propio usuario). */

    public void validateUpdate(UUID id, UserDTO dto) {

        // 1. Validar que el usuario exista y no esté eliminado
        User user = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no existe o ha sido eliminado."));

        // 2. Validar campos obligatorios
        String name = StringUtils.hasText(dto.getName()) ? dto.getName() : user.getName();
        String lastname = StringUtils.hasText(dto.getLastname()) ? dto.getLastname() : user.getLastname();
        String phoneNumber = StringUtils.hasText(dto.getPhoneNumber()) ? dto.getPhoneNumber() : user.getPhoneNumber();
        String email = StringUtils.hasText(dto.getEmail()) ? dto.getEmail() : user.getEmail();

        if (!StringUtils.hasText(name)) throw new IllegalArgumentException("El campo nombre es obligatorio.");
        if (!StringUtils.hasText(lastname)) throw new IllegalArgumentException("El campo apellido es obligatorio.");
        if (!StringUtils.hasText(phoneNumber)) throw new IllegalArgumentException("El campo número de teléfono es obligatorio.");
        if (!StringUtils.hasText(email)) throw new IllegalArgumentException("El campo email es obligatorio.");

        // 3. Validar que el email no esté ya registrado por otro usuario
        User otherEmail = userRepository.findByEmail(email);

        if (otherEmail != null && !otherEmail.getId().equals(dto.getId())) {
            throw new IllegalArgumentException("El email ya está registrado por otro usuario.");

        }
    }






}
