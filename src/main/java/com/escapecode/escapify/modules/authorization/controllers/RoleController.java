package com.escapecode.escapify.modules.authorization.controllers;

import com.escapecode.escapify.modules.authorization.dto.RoleDTO;
import com.escapecode.escapify.modules.authorization.services.RoleService;
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
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody RoleDTO roleDTO) {
        try {
            RoleDTO created = roleService.createRole(roleDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseUtil.successResponse("Rol creado exitosamente", created));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al crear el rol"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        try {
            RoleDTO role = roleService.findById(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Rol encontrado", role));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        try {
            Page<RoleDTO> roles = roleService.findAll(pageable);
            return ResponseEntity.ok(ResponseUtil.successResponse("Roles encontrados exitosamente", roles));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.errorResponse("Error al buscar los roles"));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String scope,
            @RequestParam(required = false) UUID scopeId,
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate,
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        try {
            Page<RoleDTO> roles = roleService.search(name, scope, scopeId, enabled, startDate, endDate, pageable);
            return ResponseEntity.ok(ResponseUtil.successResponse("Búsqueda de roles exitosa", roles));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.errorResponse("Error al buscar los roles"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody RoleDTO roleDTO) {
        try {
            RoleDTO updated = roleService.update(id, roleDTO);
            return ResponseEntity.ok(ResponseUtil.successResponse("Rol actualizado exitosamente", updated));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al actualizar el rol"));
        }
    }

    @PutMapping("/change-status/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable UUID id) {
        try {
            RoleDTO changed = roleService.changeStatus(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Estado del rol cambiado exitosamente", changed));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al cambiar el estado del rol"));
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            roleService.delete(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Rol eliminado exitosamente", null));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al eliminar el rol"));
        }
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restore(@PathVariable UUID id) {
        try {
            roleService.restore(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Rol restaurado exitosamente", null));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al restaurar el rol"));
        }
    }
}