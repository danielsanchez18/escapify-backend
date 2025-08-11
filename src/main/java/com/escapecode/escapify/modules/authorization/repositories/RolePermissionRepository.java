package com.escapecode.escapify.modules.authorization.repositories;

import com.escapecode.escapify.modules.authorization.entities.Role;
import com.escapecode.escapify.modules.authorization.entities.RolePermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, UUID> {

    boolean existsByRoleIdAndPermissionIdAndDeletedFalse(UUID roleId, UUID permissionId);

    @Modifying
    @Query("UPDATE RolePermission rp SET rp.deleted = true " +
            "WHERE rp.roleId = :roleId")
    void softDeleteByRoleId(@Param("roleId") UUID roleId);

    @Modifying
    @Query("UPDATE RolePermission rp SET rp.deleted = true " +
            "WHERE rp.permissionId = :permissionId")
    void softDeleteByPermissionId(@Param("permissionId") UUID permissionId);

    Page<RolePermission> findByRoleId(UUID roleId, Pageable pageable);

}
