package com.escapecode.escapify.modules.stock.repositories;

import com.escapecode.escapify.modules.stock.dto.StockItemsDTO;
import com.escapecode.escapify.modules.stock.entities.StockItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockItemsRepository extends JpaRepository<StockItems, UUID> {

    boolean existsByItemIdAndTypeItem(UUID itemId, String typeItem);

    Optional<StockItems> findByItemIdAndTypeItem(UUID idItem, String typeItem);

    Optional<StockItems> findByItemIdAndTypeItemAndEnabledTrue(UUID idItem, String typeItem);

    @Query("SELECT s FROM StockItems s " +
            "WHERE s.enabled = true " +
            "AND (s.quantity - s.reserved) <= :threshold " +
            "AND s.audit = :branchId")
    Page<StockItems> findLowStockByBranch(
            @Param("branchId") UUID branchId,
            @Param("threshold") int threshold,
            Pageable pageable
    );

}
