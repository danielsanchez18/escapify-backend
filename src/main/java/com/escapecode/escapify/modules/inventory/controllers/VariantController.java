package com.escapecode.escapify.modules.inventory.controllers;

import com.escapecode.escapify.modules.inventory.dto.ProductDTO;
import com.escapecode.escapify.modules.inventory.dto.VariantDTO;
import com.escapecode.escapify.modules.inventory.services.VariantService;
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
@RequestMapping("/variants")
public class VariantController {

    @Autowired
    private VariantService variantService;

    @PostMapping
    public ResponseEntity<?> create(
            @RequestPart("variant") String variantJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        ObjectMapper om = new ObjectMapper();
        VariantDTO variant = om.readValue(variantJson, VariantDTO.class);

        try {
            VariantDTO createdVariant = variantService.create(variant, image);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseUtil.successResponse("Variante creada exitosamente", createdVariant));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al crear la variante"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        try {
            VariantDTO variant = variantService.getById(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Variante encontrada", variant));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.errorResponse(ex.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        try {
            Page<VariantDTO> variants = variantService.getAll(pageable);
            return ResponseEntity.ok(ResponseUtil.successResponse("Variantes encontradas exitosamente", variants));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.errorResponse("Error al buscar las variantes"));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sku,
            @RequestParam(required = false) UUID productId,
            @RequestParam(required = false) UUID subcategoryId,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate,
            @RequestParam(required = false) Boolean enabled,
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        try {
            Page<VariantDTO> variants = variantService.search(name, sku, productId, subcategoryId, startDate, endDate, enabled, pageable);
            return ResponseEntity.ok(ResponseUtil.successResponse("Variantes encontradas exitosamente", variants));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.errorResponse("Error al buscar las variantes"));
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getByProductId(@PathVariable UUID productId, @PageableDefault(size = 10, page = 0) Pageable pageable) {
        try {
            Page<VariantDTO> variants = variantService.getByProductId(productId, pageable);
            return ResponseEntity.ok(ResponseUtil.successResponse("Variantes encontradas exitosamente", variants));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.errorResponse("Error al buscar las variantes por producto"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") UUID id,
            @RequestPart("variant") String variantJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        ObjectMapper om = new ObjectMapper();
        VariantDTO variant = om.readValue(variantJson, VariantDTO.class);

        try {
            VariantDTO updatedVariant = variantService.update(id, variant, image);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseUtil.successResponse("Variante actualizada exitosamente", updatedVariant));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al actualizar la variante"));
        }
    }

    @PutMapping("/change-status/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable UUID id) {
        try {
            VariantDTO updatedVariant = variantService.changeStatus(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Estado de la variante cambiado exitosamente", updatedVariant));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al cambiar el estado de la variante"));
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            variantService.delete(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Variante eliminada exitosamente", null));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al eliminar la variante"));
        }
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restore(@PathVariable UUID id) {
        try {
            variantService.restore(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Variante restaurada exitosamente", null));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al restaurar la variante"));
        }
    }
}