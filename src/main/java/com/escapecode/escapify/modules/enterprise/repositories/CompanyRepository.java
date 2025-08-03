package com.escapecode.escapify.modules.enterprise.repositories;

import com.escapecode.escapify.modules.enterprise.entities.Company;
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
public interface CompanyRepository extends JpaRepository<Company, UUID> {

    boolean existsByNameAndDeletedFalse(String name);

    Optional<Company> findByNameAndDeletedFalse(String name);

    // Consulta de búsqueda dinámica
    @Query("SELECT c FROM Company c WHERE "
            + "(:name IS NULL OR c.name LIKE %:name%) "
            + "AND (:tag IS NULL OR c.tag LIKE %:tag%) "
            + "AND (:country IS NULL OR c.country LIKE %:country%) "
            + "AND (:startDate IS NULL OR c.audit.createdAt >= :startDate) "
            + "AND (:endDate IS NULL OR c.audit.createdAt <= :endDate)"
            + "AND (:enabled IS NULL OR c.enabled = :enabled)"
            + "AND c.deleted = false")
    Page<Company> search(
            @Param("name") String name,
            @Param("tag") String tag,
            @Param("country") String country,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("enabled") Boolean enabled,
            Pageable pageable
    );

}
