package com.escapecode.escapify.modules.authentication.services;

import com.escapecode.escapify.modules.authentication.dto.AuthRequest;
import com.escapecode.escapify.modules.authentication.dto.AuthResponse;
import com.escapecode.escapify.modules.authentication.dto.CurrentUserResponse;
import com.escapecode.escapify.modules.authorization.entities.Permission;
import com.escapecode.escapify.modules.authorization.entities.Role;
import com.escapecode.escapify.modules.authorization.entities.RolePermission;
import com.escapecode.escapify.modules.authorization.entities.UserRole;
import com.escapecode.escapify.modules.authorization.repositories.PermissionRepository;
import com.escapecode.escapify.modules.authorization.repositories.RolePermissionRepository;
import com.escapecode.escapify.modules.authorization.repositories.RoleRepository;
import com.escapecode.escapify.modules.authorization.repositories.UserRoleRepository;
import com.escapecode.escapify.modules.users.entities.User;
import com.escapecode.escapify.modules.users.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;

    public AuthResponse login(AuthRequest request) {
        try {
            // 1. Autenticación de credenciales
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // 2. Buscar usuario activo
            User user = userRepository.findByEmailAndDeletedFalse(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado o inactivo"));

            // 3. Obtener roles del usuario
            List<UUID> roleIds = userRoleRepository.findByUserIdAndDeletedFalse(user.getId())
                    .stream()
                    .map(UserRole::getRoleId)
                    .collect(Collectors.toList());

            List<Role> roles = roleRepository.findByIdInAndDeletedFalse(roleIds);
            List<String> roleNames = roles.stream()
                    .map(Role::getName)
                    .collect(Collectors.toList());

            // Scope: tomamos el del primer rol si existe, o uno por defecto
            String scope = roles.isEmpty() ? "DEFAULT" : roles.get(0).getScope();

            // 4. Obtener permisos de todos los roles
            List<UUID> permissionIds = rolePermissionRepository.findByRoleIdInAndDeletedFalse(roleIds)
                    .stream()
                    .map(RolePermission::getPermissionId)
                    .distinct()
                    .collect(Collectors.toList());

            List<String> permissions = permissionRepository.findByIdIn(permissionIds)
                    .stream()
                    .map(Permission::getCode)
                    .collect(Collectors.toList());

            // 5. Generar Access Token
            String accessToken = jwtService.generateAccessToken(
                    user.getId().toString(),
                    user.getEmail(),
                    scope,
                    roleNames,
                    permissions
            );

            // 6. Generar Refresh Token solo si rememberMe = true
            String refreshToken = request.isRememberMe()
                    ? jwtService.generateRefreshToken(user.getId().toString())
                    : null;

            // 7. Respuesta
            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

        } catch (AuthenticationException e) {
            throw new RuntimeException("Credenciales inválidas", e);
        }
    }

    public CurrentUserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No hay un usuario autenticado");
        }

        // Datos del principal
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Extraer authorities
        List<String> authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        // Separar roles y permisos (los roles tienen prefijo ROLE_)
        List<String> roles = authorities.stream()
                .filter(auth -> auth.startsWith("ROLE_"))
                .map(auth -> auth.substring(5)) // quitar ROLE_
                .toList();

        List<String> permissions = authorities.stream()
                .filter(auth -> !auth.startsWith("ROLE_"))
                .toList();

        // Buscar el usuario por email
        User user = userRepository.findByEmailAndDeletedFalse(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return CurrentUserResponse.builder()
                .id(user.getId().toString())
                .name(user.getName())
                .lastname(user.getLastname())
                .email(userDetails.getUsername())
                .roles(roles)
                .permissions(permissions)
                .build();
    }

}
