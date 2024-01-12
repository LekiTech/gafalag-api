package org.lekitech.gafalag.controller.security;

import lombok.RequiredArgsConstructor;
import org.lekitech.gafalag.dto.security.LoginResponseDto;
import org.lekitech.gafalag.dto.security.UserDto;
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
     * Registers a new user into the system.
     *
     * <p>This method accepts a {@link UserDto} object containing user details,
     * registers the user, and returns the created user data.
     * The user's password is encrypted before storing in the database.</p>
     *
     * @param dto Data Transfer Object containing user registration information.
     * @return A {@link ResponseEntity} containing the registered {@link UserDto}
     * with a status of {@link HttpStatus#CREATED}.
     */
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto dto) {
        final UserDto registeredUser = authenticationService
                .registerUser(dto.firstName(), dto.lastName(), dto.email(), dto.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    /**
     * Authenticates a user and generates a login token.
     *
     * <p>This method accepts a {@link UserDto} object containing user login credentials,
     * authenticates the user, and returns a {@link LoginResponseDto} containing
     * a JWT token and user details.</p>
     *
     * @param dto Data Transfer Object containing user login credentials.
     * @return A {@link ResponseEntity} containing the {@link LoginResponseDto}
     * with an authentication token and a status of {@link HttpStatus#OK}.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody UserDto dto) {
        final LoginResponseDto response = authenticationService
                .loginUser(dto.email(), dto.password());
        return ResponseEntity.ok(response);
    }
}
