package com.escapecode.escapify.modules.authorization.repositories;

import com.escapecode.escapify.modules.authorization.entities.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {

    boolean existsByUserIdAndRoleIdAndDeletedFalse(UUID userId, UUID roleId);

    Page<UserRole> findByUserId(UUID userId, Pageable pageable);

    Page<UserRole> findByRoleId(UUID roleId, Pageable pageable);

    @Modifying
    @Query("UPDATE UserRole ur SET ur.deleted = true " +
            "WHERE ur.userId = :userId")
    void softDeleteByUserId(@Param("userId") UUID userId);

    @Modifying
    @Query("UPDATE UserRole ur SET ur.deleted = true " +
            "WHERE ur.roleId = :roleId")
    void softDeleteByRoleId(@Param("roleId") UUID roleId);

    @Query("SELECT ur FROM UserRole ur WHERE ur.userId = :userId AND ur.deleted = false")
    List<UserRole> findByUserIdAndDeletedFalse(UUID userId);




}
