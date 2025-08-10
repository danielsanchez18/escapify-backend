package com.escapecode.escapify.modules.authorization.controllers;

import com.escapecode.escapify.modules.authorization.dto.PermissionDTO;
import com.escapecode.escapify.modules.authorization.services.PermissionService;
import com.escapecode.escapify.shared.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PermissionDTO permissionDTO) {
        try {
            PermissionDTO created = permissionService.create(permissionDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseUtil.successResponse("Permiso creado exitosamente", created));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al crear el permiso"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        try {
            PermissionDTO permission = permissionService.findById(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Permiso encontrado", permission));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        try {
            Page<PermissionDTO> permissions = permissionService.findAll(pageable);
            return ResponseEntity.ok(ResponseUtil.successResponse("Permisos encontrados exitosamente", permissions));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.errorResponse("Error al buscar los permisos"));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate,
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        try {
            Page<PermissionDTO> permissions = permissionService.search(code, startDate, endDate, pageable);
            return ResponseEntity.ok(ResponseUtil.successResponse("Búsqueda de permisos exitosa", permissions));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.errorResponse("Error al buscar los permisos"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody PermissionDTO permissionDTO) {
        try {
            PermissionDTO updated = permissionService.update(id, permissionDTO);
            return ResponseEntity.ok(ResponseUtil.successResponse("Permiso actualizado exitosamente", updated));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al actualizar el permiso"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            permissionService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ResponseUtil.successResponse("Permiso eliminado exitosamente", null));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al eliminar el permiso"));
        }
    }

}