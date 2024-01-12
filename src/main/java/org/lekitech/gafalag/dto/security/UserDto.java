package org.lekitech.gafalag.dto.security;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) for User entity.
 * This DTO is used for transferring user data across different layers of the application,
 * particularly between the service layer and the API endpoints.
 *
 * <p>It encapsulates user-related information such as personal details, authentication data,
 * and assigned roles or authorities.</p>
 *
 * @param id                    Unique identifier of the user.
 * @param firstName             First name of the user.
 * @param lastName              Last name of the user.
 * @param email                 Email address of the user, used as the primary identifier for login.
 * @param password              Encrypted password of the user.
 * @param verified              Boolean flag indicating whether the user's email is verified.
 * @param authorities           Set of roles or authorities assigned to the user.
 * @param createdAt             Timestamp when the user was created.
 * @param updatedAt             Timestamp when the user was last updated.
 * @param enabled               Boolean flag indicating whether the user account is enabled.
 * @param accountNonExpired     Boolean flag indicating whether the user account is non-expired.
 * @param accountNonLocked      Boolean flag indicating whether the user account is non-locked.
 * @param credentialsNonExpired Boolean flag indicating whether the user's credentials are non-expired.
 */
public record UserDto(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String password,
        Boolean verified,
        Set<RoleDto> authorities,
        Timestamp createdAt,
        Timestamp updatedAt,
        Boolean enabled,
        Boolean accountNonExpired,
        Boolean accountNonLocked,
        Boolean credentialsNonExpired
) { }
