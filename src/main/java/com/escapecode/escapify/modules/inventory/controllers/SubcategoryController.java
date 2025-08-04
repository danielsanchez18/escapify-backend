package com.escapecode.escapify.modules.inventory.controllers;

import com.escapecode.escapify.modules.inventory.dto.SubcategoryDTO;
import com.escapecode.escapify.modules.inventory.services.SubcategoryService;
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
@RequestMapping("/subcategories")
public class SubcategoryController {

    @Autowired
    private SubcategoryService subcategoryService;

    @PostMapping
    public ResponseEntity<?> create(
            @RequestPart("subcategory") String subcategoryJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        ObjectMapper om = new ObjectMapper();
        SubcategoryDTO subcategory = om.readValue(subcategoryJson, SubcategoryDTO.class);

        try {
            SubcategoryDTO createdSubcategory = subcategoryService.create(subcategory, image);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseUtil.successResponse("Subcategoría creada exitosamente", createdSubcategory));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al crear la subcategoría"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        try {
            SubcategoryDTO subcategory = subcategoryService.getById(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Subcategoría encontrada", subcategory));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.errorResponse(ex.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        try {
            Page<SubcategoryDTO> subcategories = subcategoryService.getAll(pageable);
            return ResponseEntity.ok(ResponseUtil.successResponse("Subcategorías encontradas exitosamente", subcategories));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.errorResponse("Error al buscar las subcategorías"));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sku,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate,
            @RequestParam(required = false) Boolean enabled,
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        try {
            Page<SubcategoryDTO> subcategories = subcategoryService.search(name, sku, categoryId, startDate, endDate, enabled, pageable);
            return ResponseEntity.ok(ResponseUtil.successResponse("Subcategorías encontradas exitosamente", subcategories));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.errorResponse("Error al buscar las subcategorías"));
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> getByCategoryId(@PathVariable UUID categoryId, @PageableDefault(size = 10, page = 0) Pageable pageable) {
        try {
            Page<SubcategoryDTO> subcategories = subcategoryService.getByCategoryId(categoryId, pageable);
            return ResponseEntity.ok(ResponseUtil.successResponse("Subcategorías encontradas exitosamente", subcategories));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.errorResponse("Error al buscar las subcategorías por categoría"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") UUID id,
            @RequestPart("subcategory") String subcategoryJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        ObjectMapper om = new ObjectMapper();
        SubcategoryDTO subcategory = om.readValue(subcategoryJson, SubcategoryDTO.class);

        try {
            SubcategoryDTO updatedSubcategory = subcategoryService.update(id, subcategory, image);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseUtil.successResponse("Subcategoría actualizada exitosamente", updatedSubcategory));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al actualizar la subcategoría"));
        }
    }

    @PutMapping("/change-status/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable UUID id) {
        try {
            SubcategoryDTO updatedSubcategory = subcategoryService.changeStatus(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Estado de la subcategoría cambiado exitosamente", updatedSubcategory));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al cambiar el estado de la subcategoría"));
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            subcategoryService.delete(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Subcategoría eliminada exitosamente", null));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al eliminar la subcategoría"));
        }
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restore(@PathVariable UUID id) {
        try {
            subcategoryService.restore(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Subcategoría restaurada exitosamente", null));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al restaurar la subcategoría"));
        }
    }

}
