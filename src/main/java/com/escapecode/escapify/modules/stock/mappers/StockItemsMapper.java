package com.escapecode.escapify.modules.stock.mappers;

import com.escapecode.escapify.modules.enterprise.entities.Branch;
import com.escapecode.escapify.modules.stock.dto.StockItemsDTO;
import com.escapecode.escapify.modules.stock.entities.StockItems;
import org.springframework.stereotype.Component;

@Component
public class StockItemsMapper {

    public StockItemsDTO toDTO(StockItems stockItems) {
        StockItemsDTO dto = new StockItemsDTO();
        dto.setId(stockItems.getId());
        dto.setTypeItem(stockItems.getTypeItem());
        dto.setItemId(stockItems.getItemId());
        dto.setQuantity(stockItems.getQuantity());
        dto.setReserved(stockItems.getReserved());
        dto.setEnabled(stockItems.getEnabled());
        dto.setOversold(stockItems.getOversold());
        dto.setAudit(stockItems.getAudit());

        return dto;
    }

    public StockItems toEntity(StockItemsDTO dto) {
        StockItems stockItems = new StockItems();
        stockItems.setId(dto.getId());
        stockItems.setTypeItem(dto.getTypeItem());
        stockItems.setItemId(dto.getItemId());
        stockItems.setQuantity(dto.getQuantity());
        stockItems.setReserved(dto.getReserved());
        stockItems.setEnabled(dto.getEnabled());
        stockItems.setOversold(dto.getOversold());

        return stockItems;
    }

}
