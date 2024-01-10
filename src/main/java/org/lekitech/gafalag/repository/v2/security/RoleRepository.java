package org.lekitech.gafalag.repository.v2.security;

import org.lekitech.gafalag.entity.v2.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByAuthority(@NonNull Role.RoleType authority);
}
