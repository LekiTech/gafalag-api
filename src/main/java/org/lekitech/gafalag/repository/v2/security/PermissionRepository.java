package org.lekitech.gafalag.repository.v2.security;

import org.lekitech.gafalag.entity.v2.Language;
import org.lekitech.gafalag.entity.v2.security.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, UUID> {

    Optional<Permission> findByRightAndComponentAndLanguage(
            @NonNull Permission.Right right,
            @NonNull Permission.Component component,
            @NonNull Language language
    );
}
