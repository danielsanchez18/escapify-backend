package com.escapecode.escapify.modules.stock.services;

import com.escapecode.escapify.modules.stock.dto.StockItemsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface StockItemsServices {

    StockItemsDTO create(StockItemsDTO stockItemsDTO);

    StockItemsDTO getById(UUID id);

    StockItemsDTO getByItem(UUID itemId, String typeItem);

    // Método para obtener alertas de stock bajo
    Page<StockItemsDTO> getLowStockAlerts(UUID branchId, Pageable pageable);

    // Método para cambiar la cantidad de stock de un item ('quantity')
    void adjustStock(UUID itemId, String typeItem, int amount);

    // Método para verificar si hay suficiente stock disponible
    boolean isAvailable(UUID itemId, String typeItem, int requiredQuantity);

    StockItemsDTO update(UUID id, StockItemsDTO stockItemsDTO);

    StockItemsDTO changeStatus(UUID id);

}