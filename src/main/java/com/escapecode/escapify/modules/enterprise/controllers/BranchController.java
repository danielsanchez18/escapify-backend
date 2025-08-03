package com.escapecode.escapify.modules.enterprise.controllers;

import com.escapecode.escapify.modules.enterprise.dto.BranchDTO;
import com.escapecode.escapify.modules.enterprise.services.BranchService;
import com.escapecode.escapify.shared.utils.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/branches")
public class BranchController {

    @Autowired
    private BranchService branchService;

    @PostMapping
    public ResponseEntity<?> create(
            @RequestPart("branch") String branchJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        ObjectMapper om = new ObjectMapper();
        BranchDTO branch = om.readValue(branchJson, BranchDTO.class);

        try {
            BranchDTO createdBranch = branchService.create(branch, image);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseUtil.successResponse("Sucursal creada exitosamente", createdBranch));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al crear la sucursal"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        try {
            BranchDTO branch = branchService.getById(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Sucursal encontrada", branch));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.errorResponse(ex.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        try {
            Page<BranchDTO> branches = branchService.getAll(pageable);
            return ResponseEntity.ok(ResponseUtil.successResponse("Sucursales encontradas exitosamente", branches));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.errorResponse("Error al buscar las sucursales"));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate,
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(required = false) UUID companyId,
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        try {
            Page<BranchDTO> branches = branchService.search(name, address, city, country, companyId, startDate, endDate, enabled, pageable);
            return ResponseEntity.ok(ResponseUtil.successResponse("Sucursales encontradas exitosamente", branches));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.errorResponse(ex.getMessage()));
        }
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<?> getByCompanyId(
            @PathVariable UUID companyId,
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        try {
            Page<BranchDTO> branches = branchService.getByCompanyId(companyId, pageable);
            return ResponseEntity.ok(ResponseUtil.successResponse("Sucursales de la empresa encontradas exitosamente", branches));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.errorResponse(ex.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") UUID id,
            @RequestPart("branch") String branchJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        ObjectMapper om = new ObjectMapper();
        BranchDTO branch = om.readValue(branchJson, BranchDTO.class);

        try {
            BranchDTO updatedBranch = branchService.update(id, branch, image);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseUtil.successResponse("Sucursal actualizada exitosamente", updatedBranch));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al actualizar la sucursal"));
        }
    }

    @PutMapping("/change-status/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable UUID id) {
        try {
            BranchDTO updatedBranch = branchService.changeStatus(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Estado de la sucursal cambiado exitosamente", updatedBranch));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al cambiar el estado de la sucursal"));
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            branchService.delete(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Sucursal eliminada exitosamente", null));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al eliminar la sucursal"));
        }
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restore(@PathVariable UUID id) {
        try {
            branchService.restore(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Sucursal restaurada exitosamente", null));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al restaurar la sucursal"));
        }
    }

}