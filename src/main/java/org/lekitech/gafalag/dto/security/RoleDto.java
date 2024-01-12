package org.lekitech.gafalag.dto.security;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) for the Role entity.
 * This DTO is utilized to transfer role-related data between different layers of the application,
 * especially between the service layer and the controllers.
 *
 * <p>It encapsulates details about a role, including its authority, permission level, and associated language.</p>
 *
 * @param id         The unique identifier of the role.
 * @param authority  The name representing the role, typically used for identifying the role in security contexts.
 * @param permission The level of permission granted by this role, such as 'EDIT' or 'VIEW'.
 * @param language   The identifier of the language associated with this role, indicating the scope or context where the role is applicable.
 */
public record RoleDto(
        UUID id,
        String authority,
        String permission,
        String language
) { }
