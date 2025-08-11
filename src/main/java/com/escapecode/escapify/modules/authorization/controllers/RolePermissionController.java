package com.escapecode.escapify.modules.authorization.controllers;

import com.escapecode.escapify.modules.authorization.entities.RolePermission;
import com.escapecode.escapify.modules.authorization.services.RolePermissionService;
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
@RequestMapping("/role-permissions")
public class RolePermissionController {

    @Autowired
    private RolePermissionService rolePermissionService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody RolePermission rolePermission) {
        try {
            RolePermission created = rolePermissionService.create(rolePermission);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseUtil.successResponse("Asociación rol-permiso creada exitosamente", created));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al crear la asociación rol-permiso"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        try {
            RolePermission rolePermission = rolePermissionService.getById(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Asociación rol-permiso encontrada", rolePermission));
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
            Page<RolePermission> rp = rolePermissionService.getByRoleId(roleId, pageable);
            return ResponseEntity.ok(ResponseUtil.successResponse("Asociaciones rol-permiso encontradas exitosamente", rp));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        }
    }

    @PutMapping("/remove/{id}")
    public ResponseEntity<?> remove(@PathVariable UUID id) {
        try {
            rolePermissionService.remove(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Rol-permiso eliminado exitosamente", null));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al eliminar el rol-permiso"));
        }
    }

}
