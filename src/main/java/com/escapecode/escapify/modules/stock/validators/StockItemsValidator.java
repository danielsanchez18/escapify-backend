package com.escapecode.escapify.modules.stock.validators;

import com.escapecode.escapify.modules.inventory.repositories.ProductRepository;
import com.escapecode.escapify.modules.inventory.repositories.VariantRepository;
import com.escapecode.escapify.modules.stock.dto.StockItemsDTO;
import com.escapecode.escapify.modules.stock.repositories.StockItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StockItemsValidator {

    @Autowired
    private StockItemsRepository stockItemsRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VariantRepository variantRepository;

    /* Validaciones para crear la entidad StockItems
    1. Validar que el tipo de item sea válido (PRODUCT o VARIANT).
    2. Validar que el item al que pertenece exista y esté activo ('enabled = true' y 'deleted = false').
    3. Validar que no se intente crear dos stocks a un mismo item (typeItem y itemId).
    4. Validar que la cantidad no sea negativa. ('quantity' debe ser >= 0) */

    public void validateCreate (StockItemsDTO dto) {

        // 1. Validar que el tipo de item sea válido
        if (!"PRODUCT".equals(dto.getTypeItem()) && !"VARIANT".equals(dto.getTypeItem())) {
            throw new IllegalArgumentException("El tipo de item debe ser 'PRODUCT' o 'VARIANT'.");
        }

        // 2. Validar que el item al que pertenece exista y esté activo
        if (dto.getItemId() == null) {
            throw new IllegalArgumentException("El campo itemId es obligatorio.");
        }

        switch (dto.getTypeItem().toUpperCase()) {
            case "PRODUCT":
                if (!productRepository.existsByIdAndEnabledTrueAndDeletedFalse(dto.getItemId())) {
                    throw new IllegalArgumentException("El producto no existe o no está activo.");
                }
                break;
            case "VARIANT":
                if (!variantRepository.existsByIdAndEnabledTrueAndDeletedFalse(dto.getItemId())) {
                    throw new IllegalArgumentException("La variante no existe o no está activa.");
                }
                break;
            default:
                throw new IllegalArgumentException("Tipo de item no reconocido.");
        }

        // 3. Validar que no se intente crear dos stocks a un mismo item (typeItem y itemId).
        boolean stockExists = stockItemsRepository.existsByItemIdAndTypeItem(dto.getItemId(), dto.getTypeItem());
        if (stockExists) {
            throw new IllegalStateException("Ya existe un stock registrado para este item.");
        }

        // 4. Validar que la cantidad no sea negativa. ('quantity' debe ser >= 0)
        if (dto.getQuantity() == null || dto.getQuantity().doubleValue() <= 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa.");
        }
    }

}
