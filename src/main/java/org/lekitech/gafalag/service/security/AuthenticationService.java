package org.lekitech.gafalag.service.security;

import lombok.RequiredArgsConstructor;
import org.lekitech.gafalag.dto.security.LoginResponseDto;
import org.lekitech.gafalag.dto.security.UserDto;
import org.lekitech.gafalag.dto.security.mapper.SecurityMapper;
import org.lekitech.gafalag.entity.v2.Language;
import org.lekitech.gafalag.entity.v2.security.Role;
import org.lekitech.gafalag.entity.v2.security.User;
import org.lekitech.gafalag.exception.security.UserAlreadyExistsException;
import org.lekitech.gafalag.repository.v2.LanguageRepositoryV2;
import org.lekitech.gafalag.repository.v2.security.RoleRepository;
import org.lekitech.gafalag.repository.v2.security.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final LanguageRepositoryV2 languageRepositoryV2;
    private final SecurityMapper securityMapper;

    public static final String INITIAL_AUTHORITY = "USER";
    public static final Role.Permission INITIAL_PERMISSION = Role.Permission.VIEW;

    /**
     * Registers a new user in the system with the provided details.
     *
     * @param firstName The user's first name.
     * @param lastName  The user's last name.
     * @param email     The user's email address.
     * @param password  The user's password.
     * @return A UserDto object containing the details of the newly created user.
     * @throws UserAlreadyExistsException if a user with the given email already exists.
     * @throws RuntimeException           if an unexpected error occurs during registration.
     */
    public UserDto registerUser(String firstName,
                                String lastName,
                                String email,
                                String password) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Email already in use: " + email);
        }

        try {
            final String encodedPassword = passwordEncoder.encode(password);
            final Language language = languageRepositoryV2
                    .getById("lez"); //TODO urgently need to resolve

            final Role userRole = getOrCreate(INITIAL_AUTHORITY, language, INITIAL_PERMISSION);

            final Set<Role> authorities = new HashSet<>();
            authorities.add(userRole);

            final User newUser = new User(firstName, lastName, email, encodedPassword, authorities);
            final User savedUser = userRepository.save(newUser);
            return securityMapper.toDto(savedUser);
        } catch (Exception e) {
            throw new RuntimeException("Error during user registration.", e);
        }
    }

    /**
     * Authenticates a user with the provided email and password.
     *
     * @param email    The user's email address.
     * @param password The user's password.
     * @return A LoginResponseDto object containing the authenticated user's details and a JWT token.
     * @throws BadCredentialsException if the credentials are invalid.
     */
    public LoginResponseDto loginUser(String email, String password) {
        try {
            final Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            final String token = tokenService.generateJwt(auth);

            final User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

            final UserDto userDto = securityMapper.toDto(user);

            return new LoginResponseDto(userDto, token);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password.");
        }
    }


    /**
     * Retrieves an existing role or creates a new one based on the given parameters.
     *
     * @param authority  The name of the authority.
     * @param lang       The language associated with the role.
     * @param permission The permission associated with the role.
     * @return A Role entity.
     */
    private Role getOrCreate(String authority, Language lang, Role.Permission permission) {
        final Optional<Role> optional = roleRepository.findByAuthorityAndLanguage(authority, lang);
        return optional.orElseGet(() -> roleRepository.save(new Role(authority, lang, permission)));
    }

}
