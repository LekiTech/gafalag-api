package org.lekitech.gafalag.dto.security;

/**
 * Data Transfer Object (DTO) for representing user login information.
 * This DTO encapsulates the user's email address and password, which are typically used for
 * authenticating a user during the login process.
 *
 * <p>It is commonly used to transfer login-related data between different layers of an application,
 * such as from the frontend to the backend for authentication.</p>
 *
 * @param email    The email address of the user for login.
 * @param password The password associated with the user's account.
 */
public record UserLoginDto(
        String email,
        String password
) { }
