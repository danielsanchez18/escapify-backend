package com.escapecode.escapify.modules.inventory.controllers;

import com.escapecode.escapify.modules.inventory.dto.AttributeDTO;
import com.escapecode.escapify.modules.inventory.services.AttributeService;
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
@RequestMapping("/attributes")
public class AttributeController {

    @Autowired
    private AttributeService attributeService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody AttributeDTO attributeDTO) {
        try {
            AttributeDTO createdAttribute = attributeService.create(attributeDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseUtil.successResponse("Atributo creada exitosamente", createdAttribute));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al crear el atributo"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        try {
            AttributeDTO attribute = attributeService.getById(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Atributo encontrado", attribute));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.errorResponse(ex.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        try {
            Page<AttributeDTO> products = attributeService.getAll(pageable);
            return ResponseEntity.ok(ResponseUtil.successResponse("Atributos encontrados exitosamente", products));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.errorResponse("Error al buscar los atributos"));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sku,
            @RequestParam(required = false) UUID subcategoryId,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate,
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        try {
            Page<AttributeDTO> attributes = attributeService.search(name, sku, subcategoryId, startDate, endDate, pageable);
            return ResponseEntity.ok(ResponseUtil.successResponse("Atributos encontrados exitosamente", attributes));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.errorResponse("Error al buscar los atributos"));
        }
    }

    @GetMapping("/subcategory/{subcategoryId}")
    public ResponseEntity<?> getBySubcategoryId(@PathVariable UUID subcategoryId, @PageableDefault(size = 10, page = 0) Pageable pageable) {
        try {
            Page<AttributeDTO> attributes = attributeService.getBySubcategoryId(subcategoryId, pageable);
            return ResponseEntity.ok(ResponseUtil.successResponse("Atributos encontrados exitosamente", attributes));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.errorResponse("Error al buscar los atributos por subcategoría"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody AttributeDTO attributeDTO) {
        try {
            AttributeDTO updatedAttribute = attributeService.update(id, attributeDTO);
            return ResponseEntity.ok(ResponseUtil.successResponse("Atributo actualizado exitosamente", updatedAttribute));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al actualizar el atributo"));
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            attributeService.delete(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Atributo eliminado exitosamente", null));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al eliminar el atributo"));
        }
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restore(@PathVariable UUID id) {
        try {
            attributeService.restore(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Atributo restaurado exitosamente", null));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al restaurar el atributo"));
        }
    }
}