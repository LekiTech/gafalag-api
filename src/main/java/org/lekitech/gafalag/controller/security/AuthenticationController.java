package org.lekitech.gafalag.controller.security;

import lombok.RequiredArgsConstructor;
import org.lekitech.gafalag.dto.security.LoginResponseDto;
import org.lekitech.gafalag.dto.security.RegistrationDto;
import org.lekitech.gafalag.entity.v2.security.User;
import org.lekitech.gafalag.service.security.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public User registerUser(@RequestBody RegistrationDto dto) {
        return authenticationService.registerUser(dto.username(), dto.password(), dto.email());
    }

    @PostMapping("/login")
    public LoginResponseDto loginUser(@RequestBody RegistrationDto dto) {
        return authenticationService.loginUser(dto.username(), dto.password());
    }
}
