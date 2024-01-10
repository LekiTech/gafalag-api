package org.lekitech.gafalag.service.security;

import lombok.RequiredArgsConstructor;
import org.lekitech.gafalag.dto.security.LoginResponseDto;
import org.lekitech.gafalag.entity.v2.security.Role;
import org.lekitech.gafalag.entity.v2.security.User;
import org.lekitech.gafalag.repository.v2.security.RoleRepository;
import org.lekitech.gafalag.repository.v2.security.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public User registerUser(String username, String password, String email) {

        try {

            final String encodedPassword = passwordEncoder.encode(password);
            final Role userRole = getOrCreate(Role.RoleType.USER);

            final Set<Role> authorities = new HashSet<>();
            authorities.add(userRole);

            return userRepository.save(new User(username, encodedPassword, email, authorities));
        } catch (Exception e) {
            throw new UsernameNotFoundException("Invalid user.");
        }
    }

    public LoginResponseDto loginUser(String username, String password) {

        try {
            final Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            final String token = tokenService.generateJwt(auth);

            return new LoginResponseDto(userRepository.findByUsername(username).get(), token);

        } catch (AuthenticationException e) {
            return new LoginResponseDto(null, "");
        }
    }

    private Role getOrCreate(Role.RoleType type) {
        final Optional<Role> optional = roleRepository.findByAuthority(type);
        return optional.orElseGet(() -> roleRepository.save(new Role(Role.RoleType.USER)));
    }

}
