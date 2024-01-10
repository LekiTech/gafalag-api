package org.lekitech.gafalag.dto.security;

import org.lekitech.gafalag.entity.v2.security.User;

public record LoginResponseDto(
        User user,
        String jwt
) { }
