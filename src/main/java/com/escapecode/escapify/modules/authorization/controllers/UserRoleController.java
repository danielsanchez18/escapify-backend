package com.escapecode.escapify.modules.authorization.controllers;

import com.escapecode.escapify.modules.authorization.entities.UserRole;
import com.escapecode.escapify.modules.authorization.services.UserRoleService;
import com.escapecode.escapify.shared.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user-roles")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserRole userRole) {
        try {
            UserRole created = userRoleService.create(userRole);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseUtil.successResponse("Rol de usuario creado exitosamente", created));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al crear el rol de usuario"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        try {
            UserRole userRole = userRoleService.getById(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Rol de usuario encontrado", userRole));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getByUserId(
            @PathVariable UUID userId,
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        try {
            Page<UserRole> userRoles = userRoleService.getByUserId(userId, pageable);
            return ResponseEntity.ok(ResponseUtil.successResponse("Roles de usuario encontrados exitosamente", userRoles));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        }
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<?> getByRoleId(
            @PathVariable UUID roleId,
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        try {
            Page<UserRole> userRoles = userRoleService.getByRoleId(roleId, pageable);
            return ResponseEntity.ok(ResponseUtil.successResponse("Roles de usuario encontrados exitosamente", userRoles));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable UUID id) {
        try {
            userRoleService.remove(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Rol de usuario eliminado exitosamente", null));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al eliminar el rol de usuario"));
        }
    }
}
