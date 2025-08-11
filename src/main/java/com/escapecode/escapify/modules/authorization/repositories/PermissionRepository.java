package com.escapecode.escapify.modules.authorization.repositories;

import com.escapecode.escapify.modules.authorization.entities.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, UUID> {

    boolean existsByCode(String code);

    Optional<Permission> findByCode(String code);

    // Búsqueda dinámica de permisos
    @Query("SELECT p FROM Permission p WHERE " +
            "(:code IS NULL OR p.code LIKE %:code%) " +
            "AND (:startDate IS NULL OR p.audit.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR p.audit.createdAt <= :endDate)")
    Page<Permission> search(
            @Param("code") String code,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            Pageable pageable
    );

    List<Permission> findByIdIn(List<UUID> ids);

}
