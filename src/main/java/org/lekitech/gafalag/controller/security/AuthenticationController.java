package org.lekitech.gafalag.controller.security;

import lombok.RequiredArgsConstructor;
import org.lekitech.gafalag.dto.security.AuthResponseDto;
import org.lekitech.gafalag.dto.security.UserLoginDto;
import org.lekitech.gafalag.dto.security.UserRegisterDto;
import org.lekitech.gafalag.service.security.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling authentication-related requests.
 * This controller provides endpoints for user registration and login.
 *
 * <p>This controller relies on {@link AuthenticationService} to perform the actual
 * authentication logic and user management.</p>
 *
 * @see AuthenticationService
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Endpoint for registering a new user.
     *
     * @param registerDto The {@link UserRegisterDto} containing user registration information.
     * @return A {@link ResponseEntity} with an {@link AuthResponseDto} representing the registered user.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> registerUser(@RequestBody UserRegisterDto registerDto) {
        final AuthResponseDto registeredUser = authenticationService.registerUser(
                registerDto.firstName(),
                registerDto.lastName(),
                registerDto.email(),
                registerDto.password()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    /**
     * Endpoint for user login.
     *
     * @param loginDto The {@link UserLoginDto} containing user login information.
     * @return A {@link ResponseEntity} with an {@link AuthResponseDto} representing the authenticated user.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> loginUser(@RequestBody UserLoginDto loginDto) {
        final AuthResponseDto response = authenticationService
                .loginUser(loginDto.email(), loginDto.password());
        return ResponseEntity.ok(response);
    }
}
