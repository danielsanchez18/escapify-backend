package com.escapecode.escapify.modules.inventory.repositories;

import com.escapecode.escapify.modules.inventory.entities.Variant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VariantRepository extends JpaRepository<Variant, UUID> {

    boolean existsByNameAndProductIdAndDeletedFalse(String name, UUID productId);

    boolean existsBySkuAndProductId(String sku, UUID productId);

    Optional<Variant> findByNameAndProductIdAndDeletedFalse(String name, UUID productId);

    Optional<Variant> findBySkuAndProductIdAndDeletedFalse(String sku, UUID productId);

    @Query("SELECT v FROM Variant v WHERE "
            + "(:name IS NULL OR v.name LIKE %:name%) "
            + "AND (:sku IS NULL OR v.sku LIKE %:sku%) "
            + "AND (:productId IS NULL OR v.product.id = :productId) "
            + "AND (:subcategoryId IS NULL OR v.product.subcategory.id = :subcategoryId) "
            + "AND (:startDate IS NULL OR v.audit.createdAt >= :startDate) "
            + "AND (:endDate IS NULL OR v.audit.createdAt <= :endDate)"
            + "AND (:enabled IS NULL OR v.enabled = :enabled)"
            + "AND v.deleted = false")
    Page<Variant> search(
            @Param("name") String name,
            @Param("sku") String sku,
            @Param("productId") UUID productId,
            @Param("subcategoryId") UUID subcategoryId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("enabled") Boolean enabled,
            Pageable pageable
    );
    // Consulta de búsqueda dinámica

    Page<Variant> findAllByProductIdAndDeletedFalse(UUID productId, Pageable pageable);

}
