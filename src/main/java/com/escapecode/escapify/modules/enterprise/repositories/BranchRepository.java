package com.escapecode.escapify.modules.enterprise.repositories;

import com.escapecode.escapify.modules.enterprise.entities.Branch;
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
public interface BranchRepository extends JpaRepository<Branch, UUID> {

    boolean existsByCompanyIdAndEnabledTrueAndDeletedFalse(UUID companyId);

    boolean existsByNameAndCompanyIdAndDeletedFalse(String name, UUID companyId);

    boolean existsByIdAndDeletedFalse(UUID id);

    Page<Branch> findByCompanyIdAndDeletedFalse(UUID companyId, Pageable pageable);

    // Consulta de búsqueda dinámica
    @Query("SELECT b FROM Branch b WHERE "
            + "(:name IS NULL OR b.name LIKE %:name%) "
            + "AND (:address IS NULL OR b.address LIKE %:address%) "
            + "AND (:city IS NULL OR b.city LIKE %:city%) "
            + "AND (:country IS NULL OR b.country LIKE %:country%) "
            + "AND (:companyId IS NULL OR b.company.id = :companyId) "
            + "AND (:startDate IS NULL OR b.audit.createdAt >= :startDate) "
            + "AND (:endDate IS NULL OR b.audit.createdAt <= :endDate)"
            + "AND (:enabled IS NULL OR b.enabled = :enabled)"
            + "AND b.deleted = false")
    Page<Branch> search(
            @Param("name") String name,
            @Param("address") String address,
            @Param("city") String city,
            @Param("country") String country,
            @Param("companyId") UUID companyId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("enabled") Boolean enabled,
            Pageable pageable
    );

    Optional<Branch> findByNameAndCompanyIdAndDeletedFalse(String name, UUID companyId);

    boolean existsByIdAndEnabledTrueAndDeletedFalse(UUID branchId);

}
