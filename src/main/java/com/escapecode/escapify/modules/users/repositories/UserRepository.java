package com.escapecode.escapify.modules.users.repositories;

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
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // Búsqueda dinámica de usuarios
    @Query("SELECT u FROM User u WHERE " +
            "(:fullName IS NULL OR CONCAT(u.name, ' ', u.lastname) LIKE %:fullName%) " +
            "AND (:email IS NULL OR u.email LIKE %:email%) " +
            "AND (:provider IS NULL OR u.provider = :provider) " +
            "AND (:startDate IS NULL OR u.audit.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR u.audit.createdAt <= :endDate) " +
            "AND (:enabled IS NULL OR u.enabled = :enabled) " +
            "AND u.deleted = false")
    Page<User> search(
            @Param("fullName") String fullName,
            @Param("email") String email,
            @Param("provider") String provider,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("enabled") Boolean enabled,
            Pageable pageable
    );

    Optional<User> findByIdAndDeletedFalse(UUID id);

    boolean existsByIdAndEnabledTrueAndDeletedFalse(UUID userId);

    Optional<User> findByEmailAndDeletedFalse(String email);

}