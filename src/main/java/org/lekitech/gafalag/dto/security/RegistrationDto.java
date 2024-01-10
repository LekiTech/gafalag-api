package org.lekitech.gafalag.dto.security;

public record RegistrationDto(
        String username,
        String password,
        String email
) { }
