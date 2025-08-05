package com.escapecode.escapify.modules.inventory.controllers;

import com.escapecode.escapify.modules.inventory.dto.ProductDTO;
import com.escapecode.escapify.modules.inventory.services.ProductService;
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
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<?> create(
            @RequestPart("product") String productJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        ObjectMapper om = new ObjectMapper();
        ProductDTO product = om.readValue(productJson, ProductDTO.class);

        try {
            ProductDTO createdProduct = productService.create(product, image);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseUtil.successResponse("Producto creado exitosamente", createdProduct));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al crear el producto"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        try {
            ProductDTO product = productService.getById(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Producto encontrado", product));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.errorResponse(ex.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll(@PageableDefault(size = 10, page = 0) Pageable pageable) {
        try {
            Page<ProductDTO> products = productService.getAll(pageable);
            return ResponseEntity.ok(ResponseUtil.successResponse("Productos encontrados exitosamente", products));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.errorResponse("Error al buscar los productos"));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sku,
            @RequestParam(required = false) UUID subcategoryId,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate,
            @RequestParam(required = false) Boolean enabled,
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        try {
            Page<ProductDTO> products = productService.search(name, sku, subcategoryId, categoryId, startDate, endDate, enabled, pageable);
            return ResponseEntity.ok(ResponseUtil.successResponse("Productos encontrados exitosamente", products));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.errorResponse("Error al buscar los productos"));
        }
    }

    @GetMapping("/subcategory/{subcategoryId}")
    public ResponseEntity<?> getBySubcategoryId(@PathVariable UUID subcategoryId, @PageableDefault(size = 10, page = 0) Pageable pageable) {
        try {
            Page<ProductDTO> products = productService.getBySubcategoryId(subcategoryId, pageable);
            return ResponseEntity.ok(ResponseUtil.successResponse("Productos encontrados exitosamente", products));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseUtil.errorResponse("Error al buscar los productos por subcategoría"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") UUID id,
            @RequestPart("product") String productJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        ObjectMapper om = new ObjectMapper();
        ProductDTO product = om.readValue(productJson, ProductDTO.class);

        try {
            ProductDTO updatedProduct = productService.update(id, product, image);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseUtil.successResponse("Producto actualizado exitosamente", updatedProduct));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al actualizar el producto"));
        }
    }

    @PutMapping("/change-status/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable UUID id) {
        try {
            ProductDTO updatedProduct = productService.changeStatus(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Estado del producto cambiado exitosamente", updatedProduct));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al cambiar el estado del producto"));
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            productService.delete(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Producto eliminado exitosamente", null));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al eliminar el producto"));
        }
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restore(@PathVariable UUID id) {
        try {
            productService.restore(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Producto restaurado exitosamente", null));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al restaurar el producto"));
        }
    }
}