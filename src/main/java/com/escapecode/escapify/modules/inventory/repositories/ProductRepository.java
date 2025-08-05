package com.escapecode.escapify.modules.inventory.repositories;

import com.escapecode.escapify.modules.inventory.entities.Product;
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
public interface ProductRepository extends JpaRepository<Product, UUID> {

    boolean existsByNameAndSubcategoryIdAndDeletedFalse(String name, UUID subcategoryId);

    boolean existsBySkuAndSubcategoryIdAndDeletedFalse(String sku, UUID subcategoryId);

    Optional<Product> findByNameAndSubcategoryIdAndDeletedFalse(String name, UUID subcategoryId);

    Optional<Product> findBySkuAndSubcategoryIdAndDeletedFalse(String sku, UUID subcategoryId);

    Page<Product> findAllBySubcategoryIdAndDeletedFalse(UUID subcategoryId, Pageable pageable);

    // Consulta de búsqueda dinámica
    @Query("SELECT p FROM Product p WHERE "
            + "(:name IS NULL OR p.name LIKE %:name%) "
            + "AND (:sku IS NULL OR p.sku LIKE %:sku%) "
            + "AND (:subcategoryId IS NULL OR p.subcategory.id = :subcategoryId) "
            + "AND (:categoryId IS NULL OR p.subcategory.category.id = :categoryId) "
            + "AND (:startDate IS NULL OR p.audit.createdAt >= :startDate) "
            + "AND (:endDate IS NULL OR p.audit.createdAt <= :endDate)"
            + "AND (:enabled IS NULL OR p.enabled = :enabled)"
            + "AND p.deleted = false")
    Page<Product> search(
            @Param("name") String name,
            @Param("sku") String sku,
            @Param("subcategoryId") UUID subcategoryId,
            @Param("categoryId") UUID categoryId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("enabled") Boolean enabled,
            Pageable pageable
    );

}
