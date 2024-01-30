package org.lekitech.gafalag.service.security;

import lombok.RequiredArgsConstructor;
import org.lekitech.gafalag.dto.security.AuthResponseDto;
import org.lekitech.gafalag.dto.security.UserDto;
import org.lekitech.gafalag.dto.security.mapper.SecurityMapper;
import org.lekitech.gafalag.entity.v2.security.User;
import org.lekitech.gafalag.exception.security.UserAlreadyExistsException;
import org.lekitech.gafalag.repository.v2.security.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for handling authentication-related operations such as user registration
 * and login. This service interacts with user and role repositories and manages
 * authentication and token generation.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final SecurityMapper securityMapper;

    /**
     * Registers a new user with the provided user details.
     *
     * @param firstName The first name of the user.
     * @param lastName  The last name of the user.
     * @param email     The email address of the user.
     * @param password  The password of the user.
     * @return An {@link AuthResponseDto} containing the user DTO and authentication token.
     * @throws UserAlreadyExistsException If a user with the same email already exists.
     * @throws RuntimeException           If an error occurs during user registration.
     */
    public AuthResponseDto registerUser(String firstName,
                                        String lastName,
                                        String email,
                                        String password) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Email already in use: " + email);
        }

        try {
            final String encodedPassword = passwordEncoder.encode(password);

            final User newUser = new User(firstName, lastName, email, encodedPassword);
            final User savedUser = userRepository.save(newUser);

            final Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            final String token = tokenService.generateJwt(auth);
            final UserDto userDto = securityMapper.toDto(savedUser);

            return new AuthResponseDto(userDto, token);

        } catch (Exception e) {
            throw new RuntimeException("Error during user registration.", e);
        }
    }

    /**
     * Logs in a user with the provided email and password.
     *
     * @param email    The email address of the user.
     * @param password The password of the user.
     * @return An {@link AuthResponseDto} containing the user DTO and authentication token.
     * @throws BadCredentialsException   If the provided email or password is invalid.
     * @throws UsernameNotFoundException If no user is found with the provided email.
     */
    public AuthResponseDto loginUser(String email, String password) {
        try {
            final Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            final String token = tokenService.generateJwt(auth);

            final User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

            final UserDto userDto = securityMapper.toDto(user);

            return new AuthResponseDto(userDto, token);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password.");
        }
    }
}
