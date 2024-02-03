package org.lekitech.gafalag.configuration.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for managing RSA key properties.
 * <p>
 * This class is responsible for loading RSA key configurations from
 * environment variables or application properties. It provides access
 * to the RSA private and public keys, encoded as Base64 strings, which
 * are necessary for cryptographic operations within the application.
 * <p>
 * The RSA keys should be defined in the environment or application.properties
 * file with the keys 'RSA_PRIVATE_KEY' and 'RSA_PUBLIC_KEY'. These keys
 * are then injected into this configuration class at runtime by Spring's
 * {@link org.springframework.beans.factory.annotation.Value} annotation.
 * <p>
 * Usage of this class allows for a centralized point of access for RSA keys
 * throughout the application, enhancing maintainability and security by
 * avoiding hard-coded keys or multiple points of configuration.
 * <p>
 * Example:
 * <p>
 * application.properties:
 * RSA_PRIVATE_KEY=base64EncodedPrivateKey
 * RSA_PUBLIC_KEY=base64EncodedPublicKey
 * <p>
 * Ensure the RSA keys are properly secured and managed in the environment
 * to prevent unauthorized access.
 */
@Getter
@Configuration
public class KeyConfiguration {

    /**
     * The RSA private key, encoded as a Base64 string.
     * This key is necessary for operations such as digital signing.
     */
    @Value("${RSA_PRIVATE_KEY}")
    private String privateKeyBase64;

    /**
     * The RSA public key, encoded as a Base64 string.
     * This key can be used for operations like data encryption and signature verification.
     */
    @Value("${RSA_PUBLIC_KEY}")
    private String publicKeyBase64;

}
