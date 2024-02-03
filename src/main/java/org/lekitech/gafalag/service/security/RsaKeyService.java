package org.lekitech.gafalag.service.security;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.lekitech.gafalag.configuration.security.KeyConfiguration;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Service component responsible for initializing RSA public and private keys.
 * <p>
 * This service reads the RSA keys configured in the {@link KeyConfiguration} class,
 * decodes them from Base64 format, and initializes {@link RSAPublicKey} and
 * {@link RSAPrivateKey} instances for use in cryptographic operations. The RSA
 * keys are expected to be provided as Base64-encoded strings to accommodate
 * environment variable storage and to ensure compatibility across different
 * deployment environments.
 * <p>
 * The RSA key pair is essential for various security operations, including but not
 * limited to, signing data, verifying signatures, encrypting data, and decrypting data.
 * <p>
 * Usage of this service allows for a centralized and consistent approach to managing
 * and accessing RSA keys within the application, promoting best practices in security
 * and code maintainability.
 */
@Slf4j
@Getter
@Component
public class RsaKeyService {

    /**
     * The RSA public key used for verifying signatures and encrypting data.
     * Initialized from the Base64-encoded string provided in the application's
     * configuration.
     */
    private final RSAPublicKey publicKey;

    /**
     * The RSA private key used for signing data and decrypting data.
     * Initialized from the Base64-encoded string provided in the application's
     * configuration.
     */
    private final RSAPrivateKey privateKey;

    /**
     * Constructs a new RsaKeyService instance by decoding and initializing the RSA
     * public and private keys from the provided {@link KeyConfiguration}.
     *
     * @param keyConfig The configuration holding the Base64-encoded RSA key strings.
     * @throws IllegalStateException If there is an error during key initialization,
     *                               indicating a problem with the provided key data
     *                               or the key initialization process itself.
     */
    public RsaKeyService(KeyConfiguration keyConfig) {
        try {
            final byte[] encodedPublicKey = Base64.getDecoder().decode(keyConfig.getPublicKeyBase64());
            final byte[] encodedPrivateKey = Base64.getDecoder().decode(keyConfig.getPrivateKeyBase64());

            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            final X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
            publicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);

            final PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
            privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
        } catch (Exception e) {
            log.error("Failed to initialize RSA keys: {}", e.getMessage(), e);
            throw new IllegalStateException("Failed to initialize RSA keys", e);
        }
    }

}
