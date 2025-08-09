package com.escapecode.escapify.modules.users.servicesImpl;

import com.escapecode.escapify.modules.users.dto.UserDTO;
import com.escapecode.escapify.modules.users.entities.User;
import com.escapecode.escapify.modules.users.mappers.UserMapper;
import com.escapecode.escapify.modules.users.repositories.UserRepository;
import com.escapecode.escapify.modules.users.services.UserService;
import com.escapecode.escapify.modules.users.validators.UserValidator;
import com.escapecode.escapify.shared.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

@Service
public class UserServicesImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserValidator validator;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDTO create(UserDTO userDTO, MultipartFile image) {

        userDTO.setProvider("ESCAPIFY");
        userDTO.setEnabled(true);
        userDTO.setDeleted(false);

        validator.validateCreate(userDTO);

        User user = mapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encriptar la contraseña

        // Si la imagen no está vacía, almacenamos la imagen
        if (image != null && !image.isEmpty()) {
            String imagePath = fileStorageService.storeImage(image, "IMG_users");
            user.setPhotoUrl(imagePath);  // Asignamos la ruta de la imagen del usuario
        } else {
            // Si no se recibe imagen, asignamos la imagen predeterminada desde el directorio de recursos
            user.setPhotoUrl("static/IMG_users/user-default.png");
        }

        User savedUser = repository.save(user);
        return mapper.toDTO(savedUser);
    }

    @Override
    public UserDTO findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID:" + id));
    }

    @Override
    public Page<UserDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    @Override
    public Page<UserDTO> search(String fullName, String email, String provider, Date startDate, Date endDate, Boolean enabled, Pageable pageable) {
        Page<User> users = repository.search(fullName, email, provider, startDate, endDate, enabled, pageable);
        return users.map(user -> mapper.toDTO(user));
    }

    @Override
    public UserDTO update(UUID id, UserDTO userDTO, MultipartFile image) {
        validator.validateUpdate(id, userDTO);

        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        // Validar que el email no se repita
        if (userDTO.getName() != null) user.setName(userDTO.getName());
        if (userDTO.getLastname() != null) user.setLastname(userDTO.getLastname());
        if (userDTO.getPhoneNumber() != null) user.setPhoneNumber(userDTO.getPhoneNumber());
        if (userDTO.getEmail() != null) user.setEmail(userDTO.getEmail());

        // Si se proporciona una nueva imagen
        if (image != null && !image.isEmpty()) {
            // Elimina la imagen anterior si existe
            if (user.getPhotoUrl() != null) {
                String oldFileName = user.getPhotoUrl().replace("IMG_users/", "");
                fileStorageService.deleteImage(oldFileName, "IMG_users");
            }

            // Guarda la nueva imagen
            String imagePath = fileStorageService.storeImage(image, "IMG_users");
            user.setPhotoUrl(imagePath);
        }

        repository.save(user);
        return mapper.toDTO(user);
    }

    @Override
    public UserDTO changeStatus(UUID id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (user.getDeleted()) {
            throw new IllegalArgumentException("No se puede cambiar el estado de un usuario eliminado");
        }

        user.setEnabled(!user.getEnabled());
        return mapper.toDTO(repository.save(user));
    }

    @Override
    public void delete(UUID id) {
        User existingUser = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        if (existingUser.getDeleted()) {
            throw new RuntimeException("El usuario ya está eliminado.");
        }

        existingUser.setDeleted(true);
        mapper.toDTO(repository.save(existingUser));
    }

    @Override
    public void restore(UUID id) {
        User existingUser = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        if (!existingUser.getDeleted()) {
            throw new RuntimeException("El usuario no está eliminado.");
        }

        existingUser.setDeleted(false);
        mapper.toDTO(repository.save(existingUser));
    }

}
