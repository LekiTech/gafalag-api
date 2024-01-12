package org.lekitech.gafalag.service.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.repository.v2.security.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service class that implements {@link UserDetailsService} for user authentication.
 * This service is responsible for retrieving user details from the database, primarily
 * for use in the Spring Security authentication process.
 *
 * <p>This implementation uses a {@link UserRepository} to fetch user data based on the email address.</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads the user-specific data from the database.
     *
     * @param email The email address of the user to be loaded. In this implementation, email is used
     *              as the primary credential instead of a username.
     * @return UserDetails containing user information if the user is found.
     * @throws UsernameNotFoundException if the user with the given email is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}
