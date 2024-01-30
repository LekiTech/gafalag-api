package org.lekitech.gafalag.dto.security;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) for representing permissions.
 * This DTO encapsulates information about a permission, such as its unique identifier, rights,
 * associated component, and language.
 *
 * <p>Permissions are often used in the context of security and access control to determine
 * what actions a user is allowed to perform on specific components or resources.</p>
 *
 * @param id        The unique identifier (UUID) of the permission.
 * @param right     The specific right or privilege associated with the permission.
 * @param component The component or resource to which the permission is related.
 * @param language  The language for which the permission applies.
 */
public record PermissionDto(
        UUID id,
        String right,
        String component,
        String language
) { }
