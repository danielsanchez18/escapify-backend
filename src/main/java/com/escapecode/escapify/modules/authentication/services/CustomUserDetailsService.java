package com.escapecode.escapify.modules.authentication.services;

import com.escapecode.escapify.modules.authentication.dto.CustomUserDetailsImpl;
import com.escapecode.escapify.modules.users.entities.User;
import com.escapecode.escapify.modules.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new CustomUserDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                List.of() // No necesitas authorities aquí porque vienen en el JWT
        );
    }

}
