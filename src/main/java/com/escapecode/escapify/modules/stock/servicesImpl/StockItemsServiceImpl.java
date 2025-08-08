package com.escapecode.escapify.modules.stock.servicesImpl;

import com.escapecode.escapify.modules.stock.dto.StockItemsDTO;
import com.escapecode.escapify.modules.stock.entities.StockItems;
import com.escapecode.escapify.modules.stock.mappers.StockItemsMapper;
import com.escapecode.escapify.modules.stock.repositories.StockItemsRepository;
import com.escapecode.escapify.modules.stock.services.StockItemsServices;
import com.escapecode.escapify.modules.stock.validators.StockItemsValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class StockItemsServiceImpl implements StockItemsServices {

    @Autowired
    private StockItemsRepository repository;

    @Autowired
    private StockItemsMapper mapper;

    @Autowired
    private StockItemsValidator validator;

    @Override
    public StockItemsDTO create(StockItemsDTO stockItemsDTO) {

        stockItemsDTO.setReserved(0);
        stockItemsDTO.setOversold(0);
        stockItemsDTO.setEnabled(true);

        validator.validateCreate(stockItemsDTO);

        StockItems stockItems = mapper.toEntity(stockItemsDTO);
        StockItems savedStockItems = repository.save(stockItems);

        return mapper.toDTO(savedStockItems);
    }

    @Override
    public StockItemsDTO getById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Stock no encontrado"));
    }

    @Override
    public StockItemsDTO getByItem(UUID itemId, String typeItem) {
        return repository.findByItemIdAndTypeItemAndEnabledTrue(itemId, typeItem)
                .map(mapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Stock no encontrado para el item y tipo especificados"));
    }

    @Override
    public Page<StockItemsDTO> getLowStockAlerts(UUID branchId, Pageable pageable) {
        int threshold = 5; // Definir el umbral de stock bajo
        Page<StockItems> stockPage = repository.findLowStockByBranch(branchId, threshold, pageable);
        return stockPage.map(mapper::toDTO);
    }

    @Override
    public void adjustStock(UUID itemId, String typeItem, int amount) {
        StockItems stockItem = repository.findByItemIdAndTypeItem(itemId, typeItem)
                .orElseThrow(() -> new IllegalArgumentException("Stock no encontrado para el item y tipo especificados"));

        if (!stockItem.getEnabled()) throw new IllegalStateException("El stock está inactivo.");

        int newQuantity = stockItem.getQuantity() + amount;

        if (newQuantity < 0) throw new IllegalArgumentException("La cantidad resultante de stock no puede ser negativa.");

        stockItem.setQuantity(newQuantity);
        repository.save(stockItem);
    }


    @Override
    public boolean isAvailable(UUID itemId, String typeItem, int requiredQuantity) {
        StockItems stockItem = repository.findByItemIdAndTypeItem(itemId, typeItem)
                .orElseThrow(() -> new IllegalArgumentException("Stock no encontrado para el item y tipo especificados"));

        if (requiredQuantity < 0) throw new IllegalArgumentException("La cantidad requerida no puede ser negativa.");
        if (!stockItem.getEnabled()) throw new IllegalStateException("El stock está inactivo.");

        return stockItem.getQuantity() >= requiredQuantity;
    }

    @Override
    public StockItemsDTO update(UUID id, StockItemsDTO dto) {
        StockItems stockItem = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Stock no encontrado con el ID proporcionado."));

        if (!stockItem.getEnabled()) throw new IllegalStateException("El stock está inactivo y no puede ser actualizado.");

        // Actualizar los campos editables
        if (dto.getQuantity() != null && dto.getQuantity() >= 0) stockItem.setQuantity(dto.getQuantity());
        if (dto.getReserved() != null && dto.getReserved() >= 0) stockItem.setReserved(dto.getReserved());
        if (dto.getOversold() != null && dto.getOversold() >= 0) stockItem.setOversold(dto.getOversold());

        StockItems updated = repository.save(stockItem);
        return mapper.toDTO(updated);
    }


    @Override
    public StockItemsDTO changeStatus(UUID id) {
        StockItems stockItem = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Stock no encontrado"));

        stockItem.setEnabled(!stockItem.getEnabled());
        return mapper.toDTO(repository.save(stockItem));
    }
}
