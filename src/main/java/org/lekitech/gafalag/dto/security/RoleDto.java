package org.lekitech.gafalag.dto.security;

import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) for representing roles.
 * This DTO encapsulates information about a role, such as its unique identifier, name, and a list
 * of associated permissions.
 *
 * <p>Roles are often used in the context of role-based access control (RBAC) to define sets of
 * permissions or rights that are granted to users or entities within an application.</p>
 *
 * @param id          The unique identifier (UUID) of the role.
 * @param name        The name or title of the role.
 * @param permissions A list of {@link PermissionDto} objects representing the permissions
 *                    associated with the role.
 */
public record RoleDto(
        UUID id,
        String name,
        List<PermissionDto> permissions
) { }
