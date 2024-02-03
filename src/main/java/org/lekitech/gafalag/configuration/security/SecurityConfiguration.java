package org.lekitech.gafalag.configuration.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.lekitech.gafalag.service.security.RsaKeyService;
import org.lekitech.gafalag.utils.RsaKeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for setting up Spring Security in the application.
 * This class configures various components required for securing the application,
 * such as the authentication manager, password encoder, JWT encoder/decoder, and
 * security filter chain.
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final RsaKeyService keyService;

    /**
     * Creates a {@link PasswordEncoder} bean that uses the BCrypt hashing algorithm.
     * This bean is used for encoding passwords in the application.
     *
     * @return A BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Sets up the {@link AuthenticationManager} with a {@link DaoAuthenticationProvider}
     * using the provided {@link UserDetailsService}.
     *
     * @param detailsService The UserDetailsService to be used for loading user-specific data.
     * @return An instance of AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authManager(UserDetailsService detailsService) {
        final DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(detailsService);
        daoProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoProvider);
    }

    /**
     * Configures the HTTP security chain. It sets up the authorization rules, CSRF protection,
     * session management policy, and the resource server with JWT authentication converter.
     *
     * @param httpSecurity The HttpSecurity to configure.
     * @return The configured SecurityFilterChain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(@NonNull HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.regexMatchers(".*").permitAll();
                    auth.anyRequest().authenticated();
                });

        httpSecurity
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter());

        return httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    /**
     * Creates a {@link JwtDecoder} bean for decoding JWT tokens.
     * This decoder uses the public key from {@link RsaKeyProperties}.
     *
     * @return A NimbusJwtDecoder instance.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(keyService.getPublicKey()).build();
    }

    /**
     * Creates a {@link JwtEncoder} bean for encoding JWT tokens.
     * This encoder uses RSA keys defined in {@link RsaKeyProperties}.
     *
     * @return A NimbusJwtEncoder instance.
     */
    @Bean
    public JwtEncoder jwtEncoder() {
        final JWK jwk = new RSAKey.Builder(keyService.getPublicKey()).privateKey(keyService.getPrivateKey()).build();
        final JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwkSource);
    }

    /**
     * Configures a {@link JwtAuthenticationConverter} for extracting user roles from JWT claims.
     * Sets up a {@link JwtGrantedAuthoritiesConverter} to map 'roles' claims to roles, prefixing them with 'ROLE_'.
     *
     * @return A configured JwtAuthenticationConverter.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

}
