package com.escapecode.escapify.modules.inventory.repositories;

import com.escapecode.escapify.modules.inventory.entities.Subcategory;
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
public interface SubcategoryRepository extends JpaRepository<Subcategory, UUID> {

    boolean existsByIdAndEnabledTrueAndDeletedFalse(UUID categoryId);

    Page<Subcategory> findAllByCategoryIdAndDeletedFalse(UUID categoryId, Pageable pageable);

    // Consulta de búsqueda dinámica
    @Query("SELECT s FROM Subcategory s WHERE "
            + "(:name IS NULL OR s.name LIKE %:name%) "
            + "AND (:sku IS NULL OR s.sku LIKE %:sku%) "
            + "AND (:categoryId IS NULL OR s.category.id = :categoryId) "
            + "AND (:startDate IS NULL OR s.audit.createdAt >= :startDate) "
            + "AND (:endDate IS NULL OR s.audit.createdAt <= :endDate)"
            + "AND (:enabled IS NULL OR s.enabled = :enabled)"
            + "AND s.deleted = false")
    Page<Subcategory> search(
            @Param("name") String name,
            @Param("sku") String sku,
            @Param("categoryId") UUID categoryId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("enabled") Boolean enabled,
            Pageable pageable
    );

    // Consulta para verificar si existe una subcategoría por nombre, categoría y sucursal
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END "
            + "FROM Subcategory s "
            + " WHERE s.name = :name "
            + " AND s.category.id = :categoryId "
            + " AND s.category.branch.id = :branchId "
            + " AND s.deleted = false"
    )
    boolean existsByNameAndCategoryIdAndBranchId(String name, UUID categoryId, UUID branchId);

    // Consulta para verificar si existe una subcategoría por SKU, categoría y sucursal
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END "
            + "FROM Subcategory s "
            + " WHERE s.sku = :sku "
            + " AND s.category.id = :categoryId "
            + " AND s.category.branch.id = :branchId"
    )
    boolean existsBySkuAndCategoryIdAndBranchId(String sku, UUID categoryId, UUID branchId);

    // Consulta para verificar si existe una subcategoría por SKU y sucursal
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END "
            + "FROM Subcategory s "
            + "WHERE s.sku = :sku "
            + "AND s.category.branch.id = :branchId "
    )
    boolean existsBySkuAndBranchId(String sku, UUID branchId);

    Optional<Subcategory> findByIdAndDeletedFalse(UUID subcategoryId);

}
