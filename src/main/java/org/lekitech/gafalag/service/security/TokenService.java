package org.lekitech.gafalag.service.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.entity.v2.security.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    /**
     * Generates a JWT token for the given authentication object.
     *
     * @param auth The Authentication object containing the user's details and authorities.
     * @return A JWT token as a String.
     * @throws JwtException if there is an error during the token generation process.
     */
    public String generateJwt(Authentication auth) throws JwtException {
        try {
            final Instant now = Instant.now();

            /* Extracting essential information from UserDetails */
            final User user = (User) auth.getPrincipal();
            final String userId = user.getId().toString();
            final Set<String> roles = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());

            /* Building the JWT Claims set */
            final JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(now)
                    .subject(userId)
                    .claim("roles", roles) /* Custom claim for roles */
                    .build();

            /* Encoding the JWT Claims to generate the token */
            return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        } catch (Exception e) {
            log.error("Error generating JWT token: {}", e.getMessage(), e);
            throw new JwtException("Error generating JWT token: " + e.getMessage(), e);
        }
    }

    //TODO Authentication#decodeJwt needs to implement

    /**
     * Decodes a JWT token and extracts the authentication information.
     *
     * @param token The JWT token to be decoded.
     * @return An Authentication object containing the information extracted from the token.
     * @throws JwtException If the token cannot be decoded or is invalid.
     */
    public Authentication decodeJwt(String token) throws JwtException {
        try {
            // Decode the token using the jwtDecoder
            Jwt jwt = jwtDecoder.decode(token);

            // Extract claims or other necessary information
            // For example, getting the subject from the JWT
            String subject = jwt.getClaim("sub");

            // Construct and return the Authentication object based on the decoded data
            // Depending on your security setup, you may need to convert the extracted information into an Authentication object
            // This is a placeholder for your actual implementation
            return new UsernamePasswordAuthenticationToken(subject, null, Collections.emptyList());
        } catch (JwtException e) {
            log.error("Error decoding JWT token: {}", e.getMessage(), e);
            throw new JwtException("Error decoding JWT token: " + e.getMessage(), e);
        }
    }

}
