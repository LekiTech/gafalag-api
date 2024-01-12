package org.lekitech.gafalag.dto.security;

/**
 * Data Transfer Object (DTO) for representing the response of a login attempt.
 * This DTO encapsulates the user details along with a JSON Web Token (JWT) that
 * is used for authenticating subsequent requests.
 *
 * <p>The record is used as part of the authentication process, typically returned
 * by the login endpoint upon successful authentication.</p>
 *
 * @param user The {@link UserDto} object containing the authenticated user's details.
 * @param jwt  The JSON Web Token (JWT) string generated during the authentication process,
 *             to be used for validating the user's subsequent requests.
 */
public record LoginResponseDto(
        UserDto user,
        String jwt
) { }
