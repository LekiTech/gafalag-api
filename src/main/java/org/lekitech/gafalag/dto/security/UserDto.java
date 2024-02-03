package org.lekitech.gafalag.dto.security;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) for representing user information.
 * This DTO encapsulates details about a user, including their unique identifier, name, email,
 * verification status, associated roles, and timestamps indicating creation and last update.
 *
 * <p>It is commonly used to transfer user-related data between different layers of an application,
 * such as between the frontend and backend or within different services.</p>
 *
 * @param id        The unique identifier (UUID) of the user.
 * @param firstName The first name of the user.
 * @param lastName  The last name of the user.
 * @param email     The email address of the user.
 * @param verified  A boolean indicating whether the user's email has been verified.
 * @param roles     A set of {@link RoleDto} objects representing the roles associated with the user.
 * @param createdAt A timestamp indicating when the user was created.
 * @param updatedAt A timestamp indicating when the user was last updated.
 */
public record UserDto(
        UUID id,
        String firstName,
        String lastName,
        String email,
        Boolean verified,
        Set<RoleDto> roles,
        Timestamp createdAt,
        Timestamp updatedAt
) { }
