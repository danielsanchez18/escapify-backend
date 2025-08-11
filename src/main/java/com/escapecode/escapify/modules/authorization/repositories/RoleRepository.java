package com.escapecode.escapify.modules.authorization.repositories;

import com.escapecode.escapify.modules.authorization.entities.Role;
import com.escapecode.escapify.modules.users.entities.User;
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
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByIdAndDeletedFalse(UUID id);

    boolean existsByNameAndScopeAndScopeIdAndDeletedFalse(String name, String scope, UUID scopeId);

    Optional<Role> findByNameAndScopeAndScopeIdAndDeletedFalse(String name, String scope, UUID scopeId);

    // Búsqueda dinámica de roles
    @Query("SELECT r FROM Role r WHERE " +
            "(:name IS NULL OR r.name LIKE %:name%) " +
            "AND (:scope IS NULL OR r.scope LIKE %:scope%) " +
            "AND (:scopeId IS NULL OR r.scopeId = :scopeId) " +
            "AND (:startDate IS NULL OR r.audit.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR r.audit.createdAt <= :endDate) " +
            "AND (:enabled IS NULL OR r.enabled = :enabled) " +
            "AND r.deleted = false")
    Page<Role> search(
            @Param("name") String name,
            @Param("scope") String scope,
            @Param("scopeId") UUID scopeId,
            @Param("enabled") Boolean enabled,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            Pageable pageable
    );

    boolean existsByIdAndEnabledTrueAndDeletedFalse(UUID permissionId);

}
