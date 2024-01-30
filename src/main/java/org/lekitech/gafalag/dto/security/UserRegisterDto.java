package org.lekitech.gafalag.dto.security;

/**
 * Data Transfer Object (DTO) for representing user registration information.
 * This DTO encapsulates the user's first name, last name, email address, password, and preferred language
 * for the web interface, all of which are typically required during the user registration process.
 *
 * <p>It is commonly used to transfer user registration data between different layers of an application,
 * such as from the frontend to the backend for creating a new user account.</p>
 *
 * @param firstName The first name of the user.
 * @param lastName  The last name of the user.
 * @param email     The email address of the user for registration.
 * @param password  The password associated with the user's account.
 * @param language  The preferred language chosen by the user for the web interface.
 */
public record UserRegisterDto(
        String firstName,
        String lastName,
        String email,
        String password,
        String language
) { }
