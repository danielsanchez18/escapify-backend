package com.escapecode.escapify.modules.stock.dto;

import com.escapecode.escapify.shared.model.Audit;
import lombok.Data;

import java.util.UUID;

@Data
public class StockItemsDTO {

    private UUID id;
    private String typeItem;
    private UUID itemId;
    private Integer quantity;
    private Integer reserved;
    private Boolean enabled;
    private Integer oversold;
    private Audit audit;

}