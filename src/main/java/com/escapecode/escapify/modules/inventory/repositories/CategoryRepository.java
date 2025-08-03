package com.escapecode.escapify.modules.inventory.repositories;

import com.escapecode.escapify.modules.inventory.entities.Category;
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
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    boolean existsByNameAndBranchIdAndDeletedFalse(String name, UUID branchId);

    boolean existsBySkuAndBranchIdAndDeletedFalse(String sku, UUID branchId);

    Optional<Category> findByNameAndBranchIdAndDeletedFalse(String name, UUID branchId);

    Page<Category> findAllByBranchIdAndDeletedFalse(UUID branchId, Pageable pageable);

    // Consulta de búsqueda dinámica
    @Query("SELECT c FROM Category c WHERE "
            + "(:name IS NULL OR c.name LIKE %:name%) "
            + "AND (:sku IS NULL OR c.sku LIKE %:sku%) "
            + "AND (:branchId IS NULL OR c.branch.id = :branchId) "
            + "AND (:companyId IS NULL OR c.branch.company.id = :companyId) "
            + "AND (:startDate IS NULL OR c.audit.createdAt >= :startDate) "
            + "AND (:endDate IS NULL OR c.audit.createdAt <= :endDate)"
            + "AND (:enabled IS NULL OR c.enabled = :enabled)"
            + "AND c.deleted = false")
    Page<Category> search(
            @Param("name") String name,
            @Param("sku") String sku,
            @Param("branchId") UUID branchId,
            @Param("companyId") UUID companyId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("enabled") Boolean enabled,
            Pageable pageable
    );

}
