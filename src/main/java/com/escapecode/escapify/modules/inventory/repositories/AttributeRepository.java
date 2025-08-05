package com.escapecode.escapify.modules.inventory.repositories;

import com.escapecode.escapify.modules.inventory.entities.Attribute;
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
public interface AttributeRepository extends JpaRepository<Attribute, UUID> {

    boolean existsByNameAndSubcategoryIdAndDeletedFalse(String name, UUID subcategoryId);

    boolean existsBySkuAndSubcategoryIdAndDeletedFalse(String sku, UUID subcategoryId);

    Optional<Attribute> findByNameAndSubcategoryIdAndDeletedFalse(String name, UUID subcategoryId);

    Optional<Attribute> findBySkuAndSubcategoryIdAndDeletedFalse(String name, UUID subcategoryId);

    Page<Attribute> findAllBySubcategoryIdAndDeletedFalse(UUID subcategoryId, Pageable pageable);

    // Consulta de búsqueda dinámica
    @Query("SELECT a FROM Attribute a WHERE "
            + "(:name IS NULL OR a.name LIKE %:name%) "
            + "AND (:sku IS NULL OR a.sku LIKE %:sku%) "
            + "AND (:subcategoryId IS NULL OR a.subcategory.id = :subcategoryId) "
            + "AND (:startDate IS NULL OR a.audit.createdAt >= :startDate) "
            + "AND (:endDate IS NULL OR a.audit.createdAt <= :endDate)"
            + "AND a.deleted = false")
    Page<Attribute> search(
            @Param("name") String name,
            @Param("sku") String sku,
            @Param("subcategoryId") UUID subcategoryId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            Pageable pageable
    );

}
