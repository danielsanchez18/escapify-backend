package com.escapecode.escapify.modules.stock.controllers;

import com.escapecode.escapify.modules.stock.dto.StockItemsDTO;
import com.escapecode.escapify.modules.stock.services.StockItemsServices;
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
@RequestMapping("/stock")
public class StockItemsController {

    @Autowired
    private StockItemsServices stockItemsService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody StockItemsDTO stockItemsDTO) {
        try {
            StockItemsDTO created = stockItemsService.create(stockItemsDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseUtil.successResponse("Stock creado exitosamente", created));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al crear el stock"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        try {
            StockItemsDTO stock = stockItemsService.getById(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Stock encontrado", stock));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        }
    }

    @GetMapping("/item")
    public ResponseEntity<?> getByItem(
            @RequestParam UUID itemId,
            @RequestParam String typeItem
    ) {
        try {
            StockItemsDTO stock = stockItemsService.getByItem(itemId, typeItem);
            return ResponseEntity.ok(ResponseUtil.successResponse("Stock encontrado", stock));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        }
    }

    @GetMapping("/low-stock/{branchId}")
    public ResponseEntity<?> getLowStockAlerts(
            @PathVariable UUID branchId,
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        try {
            Page<StockItemsDTO> alerts = stockItemsService.getLowStockAlerts(branchId, pageable);
            return ResponseEntity.ok(ResponseUtil.successResponse("Alertas de bajo stock encontradas", alerts));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseUtil.errorResponse("Error al buscar alertas de bajo stock"));
        }
    }

    @PutMapping("/adjust-stock")
    public ResponseEntity<?> adjustStock(
            @RequestParam UUID itemId,
            @RequestParam String typeItem,
            @RequestParam int amount
    ) {
        try {
            stockItemsService.adjustStock(itemId, typeItem, amount);
            return ResponseEntity.ok(ResponseUtil.successResponse("Stock ajustado exitosamente", null));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al ajustar el stock"));
        }
    }

    @GetMapping("/check-availability")
    public ResponseEntity<?> checkAvailability(
            @RequestParam UUID itemId,
            @RequestParam String typeItem,
            @RequestParam int requiredQuantity
    ) {
        try {
            boolean available = stockItemsService.isAvailable(itemId, typeItem, requiredQuantity);
            return ResponseEntity.ok(ResponseUtil.successResponse("Disponibilidad de stock verificada", available));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al verificar la disponibilidad del stock"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody StockItemsDTO dto) {
        try {
            StockItemsDTO updated = stockItemsService.update(id, dto);
            return ResponseEntity.ok(ResponseUtil.successResponse("Stock actualizado exitosamente", updated));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al actualizar el stock"));
        }
    }

    @PutMapping("/change-status/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable UUID id) {
        try {
            StockItemsDTO changed = stockItemsService.changeStatus(id);
            return ResponseEntity.ok(ResponseUtil.successResponse("Estado de stock cambiado exitosamente", changed));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseUtil.errorResponse(ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseUtil.errorResponse("Error al cambiar el estado del stock"));
        }
    }
}